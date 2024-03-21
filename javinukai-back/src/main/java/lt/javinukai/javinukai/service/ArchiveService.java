package lt.javinukai.javinukai.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.response.ArchivingResponse;
import lt.javinukai.javinukai.entity.CompetitionRecord;
import lt.javinukai.javinukai.entity.PastCompetitionRecord;
import lt.javinukai.javinukai.repository.ArchiveRepository;
import lt.javinukai.javinukai.repository.CompetitionRecordRepository;
import lt.javinukai.javinukai.repository.ContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ArchiveService {

    private final ContestRepository contestRepository;
    private final ContestService contestService;
    private final CompetitionRecordRepository competitionRecordRepository;
    private final ArchiveRepository archiveRepository;

    @Autowired
    public ArchiveService(ArchiveRepository archiveRepository,
                          CompetitionRecordRepository competitionRecordRepository,
                          ContestRepository contestRepository,
                          ContestService contestService) {
        this.archiveRepository = archiveRepository;
        this.competitionRecordRepository = competitionRecordRepository;
        this.contestRepository = contestRepository;
        this.contestService = contestService;
    }

    @Transactional
    public ArchivingResponse endAndAddToArchive(UUID contestID) {

        if (contestRepository.existsById(contestID)) {

            final List<CompetitionRecord> competitionRecords = competitionRecordRepository.findByContestId(contestID);
            final List<PastCompetitionRecord> pastCompetitionRecords = new ArrayList<>();

            for (CompetitionRecord r : competitionRecords) {
                PastCompetitionRecord pastCompetition = PastCompetitionRecord.builder()
                        .contestID(contestID)
                        .firstName(r.getUser().getName())
                        .lastName(r.getUser().getSurname())
                        .email(r.getUser().getEmail())
                        .categoryName(r.getCategory().getName())
                        .contestName(r.getContest().getName())
                        .contestDescription(r.getContest().getDescription())
                        .startDate(r.getContest().getStartDate())
                        .endDate(r.getContest().getEndDate())
                        .build();
                archiveRepository.save(pastCompetition);
                pastCompetitionRecords.add(pastCompetition);
            }
            log.info("{}: Contest was archived with ID: {}", this.getClass().getName(), contestID);

            contestService.deleteContest(contestID);

            return ArchivingResponse.builder()
                    .pastCompetitionRecords(pastCompetitionRecords)
                    .httpStatus(HttpStatus.CREATED)
                    .message("Request for ending a contest")
                    .build();
        } else {
            throw new EntityNotFoundException("Contest was not found with ID: " + contestID);
        }
    }

    public Page<PastCompetitionRecord> retrieveAllRecords(Pageable pageable, String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            log.info("{}: Retrieving all past competition record list from database", this.getClass());
            return archiveRepository.findAll(pageable);
        } else {
            log.info("{}: Retrieving past competition records by name", this.getClass().getName());
            return archiveRepository.findByContestNameContainingIgnoreCase(keyword, pageable);
        }
    }

    public List<PastCompetitionRecord> retrieveContestRecords(UUID contestID) {
        log.info("{}: Retrieving contest competition records by ID", this.getClass().getName());
        return archiveRepository.findByContestID(contestID);
    }
}
