package pl.programowaniezespolowe.planner.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.programowaniezespolowe.planner.entities.CalendarEvent;
import pl.programowaniezespolowe.planner.note.Note;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "event", schema = "public")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

//    @Column(name = ??)
    private CalendarEvent calendarEvent;

    @Column(name = "userid")
    private Integer userID;

    @Column(name = "noteid")
    private Integer noteid;

    @OneToMany
    //@JsonIgnore
    @JoinTable(
            name="note", schema = "public",
            joinColumns = {@JoinColumn(name = "id")},
            inverseJoinColumns = {@JoinColumn(name = "eventid")}
    )
    private List<Note> note = new ArrayList<>();

}
