package lt.javinukai.javinukai.mapper;

import lombok.extern.slf4j.Slf4j;
import lt.javinukai.javinukai.dto.request.contest.ArchivedContestDTO;
import lt.javinukai.javinukai.entity.PastCompetitionRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class ArchiveMapper {

    public static Page<ArchivedContestDTO> contestRecordsToDTO(Page<PastCompetitionRecord> page) {
        List<ArchivedContestDTO> dto = page.getContent().stream()
                .map(ArchiveMapper::mapToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dto, page.getPageable(), page.getTotalElements());
    }

    public static Page<ArchivedContestDTO> contestRecordsToDTONoDup(Page<PastCompetitionRecord> page) {
        Set<UUID> processedContestIDs = new HashSet<>();
        List<ArchivedContestDTO> dto = page.getContent().stream()
                .filter(record -> processedContestIDs.add(record.getContestID()))
                .map(ArchiveMapper::mapToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dto, page.getPageable(), page.getTotalElements());
    }

    private static ArchivedContestDTO mapToDTO(PastCompetitionRecord record) {
        ArchivedContestDTO dto = new ArchivedContestDTO();
        dto.setContestID(record.getContestID());
        dto.setContestName(record.getContestName());
        dto.setContestDescription(record.getContestDescription());
        dto.setStartDate(record.getStartDate());
        dto.setEndDate(record.getEndDate());
        return dto;
    }
}
