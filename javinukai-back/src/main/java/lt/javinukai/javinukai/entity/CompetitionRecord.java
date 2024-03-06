package lt.javinukai.javinukai.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "competition_record")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompetitionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contest_id", referencedColumnName = "id")
//    @JsonIgnore
    private Contest contest;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
//    @JsonIgnore
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    @JsonIgnore
    private User user;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "competitionRecord", cascade = CascadeType.MERGE)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "competitionRecord", cascade = CascadeType.ALL)
    private List<PhotoCollection> entries;

    @Setter
    @Column(name = "max_photos")
    private long maxPhotos;

    @CreatedDate
    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private ZonedDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = ZonedDateTime.now();
    }
}
