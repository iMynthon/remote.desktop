package org.mynthon.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;

@Slf4j
@ApplicationScoped
public class SecurityFilter {

    @Inject
    private SecurityApproval securityApproval;


    @ServerRequestFilter
    public Response interceptorSecurityFilter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();

        log.info("Request: {} - {}", method, path);

        if (path.contains("/public/")) {
            return null;
        }

        String clientName = requestContext.getHeaderString("Authorization");

        if (clientName != null && securityApproval.checkAuthorizationDB(clientName)) {
            return null;
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Access denied. Valid Authorization header required.")
                .build();
    }
}
