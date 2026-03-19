package org.mynthon.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "remote_schema")
@Entity(name = "server_log_connection")
public class ServerLogConnection extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private UUID clientId;

    @Column(name = "connection_id")
    private Integer connectionId;

    @Column(name = "connection_create_time")
    @CreationTimestamp
    private LocalDateTime connectionCreateTime;
}
