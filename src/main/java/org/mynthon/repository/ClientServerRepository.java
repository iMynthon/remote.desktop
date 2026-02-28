package org.mynthon.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.transaction.Transactional;
import org.mynthon.model.RemoteClient;

public class ClientServerRepository implements PanacheRepository<RemoteClient> {

    @Transactional
    public RemoteClient saveEntity(RemoteClient client){
        persist(client);
        return client;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public RemoteClient getName(String nameClient){
        return find("name_client",nameClient).firstResult();
    }
}
