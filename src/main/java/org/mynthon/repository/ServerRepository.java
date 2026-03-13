package org.mynthon.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.mynthon.model.ServerLogConnection;

@ApplicationScoped
public class ServerRepository implements PanacheRepositoryBase<ServerLogConnection,Long> {

    @Transactional
    public void saveLogConnection(ServerLogConnection serverLogConnection){
        persist(serverLogConnection);
    }
}
