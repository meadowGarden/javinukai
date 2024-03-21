package lt.javinukai.javinukai.repository;

import lt.javinukai.javinukai.entity.Category;
import lt.javinukai.javinukai.entity.PastCompetitionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface ArchiveRepository extends JpaRepository <PastCompetitionRecord, UUID> {
    Page<PastCompetitionRecord> findByContestNameContainingIgnoreCase(String contestName, Pageable pageable);
}
