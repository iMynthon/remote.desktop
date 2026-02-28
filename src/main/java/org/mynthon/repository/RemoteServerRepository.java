package org.mynthon.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.mynthon.model.RemoteServer;

@ApplicationScoped
public class RemoteServerRepository implements PanacheRepository<RemoteServer> {

    @Transactional
    public RemoteServer saveEntity(RemoteServer server){
        persist(server);
        return server;
    }
}
