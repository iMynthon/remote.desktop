package org.mynthon.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.mynthon.model.ServerLogConnection;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ServerRepository implements PanacheRepositoryBase<ServerLogConnection,Long> {

    @Transactional
    public void saveLogConnection(ServerLogConnection serverLogConnection){
        persist(serverLogConnection);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<ServerLogConnection> findLogConnectionByClientId(UUID clientId){
        return getEntityManager()
                .createQuery("SELECT l FROM server_log_connection l WHERE l.clientId = :clientId",ServerLogConnection.class)
                .setParameter("clientId",clientId)
                .getResultList();
    }

}
