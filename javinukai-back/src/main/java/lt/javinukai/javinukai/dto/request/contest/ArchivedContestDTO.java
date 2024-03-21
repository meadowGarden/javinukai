package lt.javinukai.javinukai.dto.request.contest;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArchivedContestDTO {
    private UUID contestID;
    private String contestName;
    private String contestDescription;
    private List<String> categories;
    private int participantCount;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
}
