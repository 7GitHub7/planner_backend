package pl.programowaniezespolowe.planner.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.programowaniezespolowe.planner.dtos.CalendarEventDto;
import pl.programowaniezespolowe.planner.note.Note;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    @Column(name = "title")
    private String title;

    @Column(name = "startdate")
    private Date start;

    @Column(name = "enddate")
    private Date end;
//
//    @Column(name = "color")
//    private String color;
//
//    @Column(name = "actions")
//    private String actions;
//
//    @Column(name = "draggable")
//    private Integer draggable;
//
//    @Column(name = "beforestart")
//    private Integer beforeStart;
//
//    @Column(name = "after")
//    private Integer afterEnd;

    @Column(name = "userid")
    private Integer userID;

//    @OneToMany
//    //@JsonIgnore
//    @JoinTable(
//            name="note", schema = "public",
//            joinColumns = {@JoinColumn(name = "id")},
//            inverseJoinColumns = {@JoinColumn(name = "eventid")}
//    )
//    private List<Note> note = new ArrayList<>();

}
