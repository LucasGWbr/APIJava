package br.univates.api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Inscriptions", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "event_id"}) // Mapeia a restrição UNIQUE
})
public class inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inscription_id")
    private int inscriptionId;

    @JoinColumn(name = "user_id")
    private int userId;

    @JoinColumn(name = "event_id")
    private int eventId;

    @Column(name = "status")
    private String status;

}