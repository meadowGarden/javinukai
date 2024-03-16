package lt.javinukai.javinukai.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class PhotoCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Setter
    @Column(name = "name", columnDefinition = "TEXT")
    private String name;

    @Setter
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_id")
    private User author;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "collection", cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<Photo> images;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "competition_record_id", referencedColumnName = "id")
    private CompetitionRecord competitionRecord;

    @Setter
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    @JoinTable(
            name = "collection_jury",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "jury_id")
    )
    private List<User> likes;

    public void addLike(User jury) {
        if (likes == null) {
            likes = new ArrayList<>();
        }
        likes.add(jury);
    }

    public void removeLike(User jury) {
        if (likes == null) {
            return;
        }
        likes.remove(jury);
    }
    public void removeLikesFromCollection(){
        if (likes == null) {
            return;
        }
        likes.clear();
    }

    private ZonedDateTime createdAt;
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
