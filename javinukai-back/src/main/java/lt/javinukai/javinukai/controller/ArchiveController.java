package lt.javinukai.javinukai.controller;

import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.response.ArchivingResponse;
import lt.javinukai.javinukai.entity.Contest;
import lt.javinukai.javinukai.entity.PastCompetitionRecord;
import lt.javinukai.javinukai.service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(path="/api/v1")
@Slf4j
public class ArchiveController {

    private final ArchiveService archiveService;

    @Autowired
    public ArchiveController(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @PostMapping(path = "/archive/{contestID}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> endContest(@PathVariable @NotNull UUID contestID) {
        final ArchivingResponse archivingResponse = archiveService.endAndAddToArchive(contestID);
        final List<PastCompetitionRecord> pastCompetitionRecords= archivingResponse.getPastCompetitionRecords();
        final HttpStatus httpStatus = archivingResponse.getHttpStatus();
        final String message = archivingResponse.getMessage();
        log.info(message);
        return new ResponseEntity<>(pastCompetitionRecords, httpStatus);
    }

    @GetMapping(path="/archive")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public ResponseEntity<?> retrieveArchive(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "25") int limit,
                                             @RequestParam(required = false) String contains,
                                             @RequestParam(defaultValue = "name") String sortBy,
                                             @RequestParam(defaultValue = "false") boolean sortDesc) {

        log.info("Request for retrieving archive records");
        Sort.Direction direction = sortDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);

        final Pageable pageable = PageRequest.of(page, limit, sort);
        final Page<PastCompetitionRecord> pastCompetitionRecords = archiveService.retrieveAllRecords(pageable, contains);
        log.info("Request for retrieving all categories, {} record(s) found", pastCompetitionRecords.getTotalElements());
        return new ResponseEntity<>(pastCompetitionRecords, HttpStatus.OK);
    }

    @GetMapping(path = "/archive/{contestID}")
    public ResponseEntity<List<PastCompetitionRecord>> retrieveContest(@PathVariable @NotNull UUID contestID) {
        log.info("Request for retrieving contest with ID: {}", contestID);
        final List<PastCompetitionRecord> foundContest = archiveService.retrieveContestRecords(contestID);
        return new ResponseEntity<>(foundContest, HttpStatus.OK);
    }

}
