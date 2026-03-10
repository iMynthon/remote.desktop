package org.mynthon.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "remote_client")
@Table(schema = "remote_schema")
public class RemoteClient extends PanacheEntityBase {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(name = "connection_id",insertable = false)
    private Integer connectionId;

    @Column(name = "name_client")
    private String nameClient;

    private String password;

    @CreationTimestamp
    private LocalDateTime created;
}
