package org.mynthon.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.mynthon.model.RemoteServer;

import java.util.UUID;

@ApplicationScoped
public class RemoteServerRepository implements PanacheRepositoryBase<RemoteServer,UUID> {

    @Transactional
    public RemoteServer saveEntity(RemoteServer server){
        persist(server);
        return server;
    }

    public RemoteServer findByUUIDID(UUID id){
        return findById(id);
    }

}
