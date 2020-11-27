package pl.programowaniezespolowe.planner.dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    private CalendarEventDto calendarEvent;
    private Integer userID;
}
