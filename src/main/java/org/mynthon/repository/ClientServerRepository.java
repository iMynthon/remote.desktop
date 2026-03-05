package org.mynthon.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.FlushModeType;
import jakarta.transaction.Transactional;
import org.mynthon.model.RemoteClient;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static jakarta.transaction.Transactional.TxType.REQUIRES_NEW;
import static jakarta.transaction.Transactional.TxType.SUPPORTS;

@ApplicationScoped
public class ClientServerRepository implements PanacheRepositoryBase<RemoteClient, UUID> {

    @Transactional
    public RemoteClient saveEntity(RemoteClient client){
        getEntityManager().persist(client);
        getEntityManager().flush();
        getEntityManager().refresh(client);
        return client;
    }

    @Transactional(SUPPORTS)
    public Optional<RemoteClient> findByName(String nameClient){
        return find("nameClient",nameClient).firstResultOptional();
    }

    @Transactional(SUPPORTS)
    public Optional<RemoteClient> findByUUIDID(UUID id){
        return findByIdOptional(id);
    }

    @Transactional(SUPPORTS)
    public Boolean existsByName(String nameClient, String password) {
        return getEntityManager()
                .createQuery("SELECT COUNT(c) > 0 FROM remote_client c WHERE c.nameClient = :nameClient AND c.password = :password", Boolean.class)
                .setParameter("nameClient", nameClient)
                .setParameter("password",password)
                .getSingleResult();
    }

    @Transactional
    public Boolean existsByConnectionId(Integer connectionId) {
        return getEntityManager()
                .createQuery("SELECT COUNT(c) > 0 FROM remote_client c WHERE c.connectionId = :connectionId", Boolean.class)
                .setParameter("connectionId", connectionId)
                .setFlushMode(FlushModeType.COMMIT)
                .getSingleResult();
    }

}
