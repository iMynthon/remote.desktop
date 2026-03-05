package org.mynthon.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


/**
 * A security filter that intercepts incoming HTTP requests and performs Basic authentication.
 * <p>
 * This filter is applied to all REST endpoints (except public paths) and expects an
 * {@code Authorization} header with Basic scheme. The header value must be a Base64-encoded
 * string of {@code username:password}. The filter decodes the credentials, validates them
 * against a database via {@link SecurityApproval}, and either allows the request to proceed
 * or returns an HTTP 401 Unauthorized response.
 * </p>
 *
 * <p>
 * Public paths (containing "{@code /public/}") are exempt from authentication.
 * </p>
 *
 * @author Your Name
 * @version 1.0
 * @see SecurityApproval
 * @see ServerRequestFilter
 */
@Slf4j
@ApplicationScoped
public class SecurityFilter {

    private static final String BASIC_PREFIX = "Basic ";

    @Inject
    private SecurityApproval securityApproval;

    /**
     * Intercepts each incoming request, performs Basic authentication, and decides whether
     * to allow the request to continue.
     *
     * @param requestContext the context of the intercepted request, providing access to
     *                       headers, URI info, and other request data.
     * @return {@code null} if the request is allowed to proceed (authentication succeeded or
     *         the path is public), otherwise a {@link Response} with HTTP 401 Unauthorized
     *         and a JSON error message.
     */
    @ServerRequestFilter
    public Response interceptorSecurityFilter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();

        log.info("Request: {} - {}", method, path);

        if (path.contains("/public/")) {
            return null;
        }

        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith(BASIC_PREFIX)) {
            log.warn("Missing or invalid Authorization header");
            return unauthorizedResponse("Missing or invalid Authorization header");
        }

        String base64Credentials = authHeader.substring(BASIC_PREFIX.length());
        if (base64Credentials.isBlank()) {
            log.warn("Empty Basic credentials");
            return unauthorizedResponse("Empty credentials");
        }

        String[] credentials;
        try {
            credentials = extractCredentials(base64Credentials);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid Base64 in Authorization header", e);
            return unauthorizedResponse("Malformed Basic auth token");
        } catch (SecurityException e) {
            log.warn("Invalid Basic auth format: {}", e.getMessage());
            return unauthorizedResponse(e.getMessage());
        }
        String nameClient = credentials[0];
        String pass = credentials[1];

        if (isValidCredentials(nameClient, pass) && securityApproval.checkAuthorizationDB(nameClient, pass)) {
            return null;
        }

        log.warn("Authentication failed for user: {}", nameClient);
        return unauthorizedResponse("Invalid username or password");
    }

    /**
     * Validates that both username and password are non-null and not blank.
     * <p>
     * Note: The current implementation returns {@code true} if at least one of the strings
     * is not blank (using logical OR). This may be a bug; typically both should be non-blank.
     * </p>
     *
     * @param name the username to check
     * @param pass the password to check
     * @return {@code true} if both name and pass are non-null and at least one is not blank;
     *         {@code false} otherwise.
     */
    private boolean isValidCredentials(String name, String pass) {
        return name != null && pass != null && !name.isBlank() && !pass.isBlank();
    }

    /**
     * Builds an HTTP 401 Unauthorized response with a JSON error message.
     *
     * @param message the error description to include in the JSON body.
     * @return a {@link Response} with status 401 and a JSON object containing the error message.
     */
    private Response unauthorizedResponse(String message) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\": \"" + message + "\"}")
                .build();
    }

    /**
     * Decodes a Base64 string and extracts the username and password.
     * <p>
     * The input string is expected to be the Base64-encoded value of a string in the format
     * {@code username:password}. After decoding, the method splits the string at the first
     * colon character to obtain the credentials.
     * </p>
     *
     * @param base64 the Base64-encoded credentials (without the "Basic " prefix).
     * @return a two-element string array where index 0 is the username and index 1 is the password.
     * @throws IllegalArgumentException if the input string is not a valid Base64 representation.
     * @throws SecurityException        if the decoded string does not contain a colon (":") separator.
     */
    private String[] extractCredentials(String base64) {
        byte[] decoded = Base64.getDecoder().decode(base64);
        String decodedString = new String(decoded, StandardCharsets.UTF_8);

        int colonIndex = decodedString.indexOf(":");
        if (colonIndex == -1) {
            throw new SecurityException("Invalid Basic auth format (no colon)");
        }

        String login = decodedString.substring(0, colonIndex);
        String password = decodedString.substring(colonIndex + 1);
        return new String[]{login, password};
    }

}
