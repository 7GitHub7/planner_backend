package pl.programowaniezespolowe.planner.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @Column(name = "title")
    private String title;

    @Column(name = "startdate")
    private LocalDateTime start;

    @Column(name = "enddate")
    private LocalDateTime end;

    @Column(name = "color")
    private String color;

    @Column(name = "actions")
    private String actions;

    @Column(name = "draggable")
    private Integer draggable;

    @Column(name = "beforestart")
    private Integer beforeStart;

    @Column(name = "after")
    private Integer afterEnd;

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


    public Event( String title, LocalDateTime start, LocalDateTime end, String color, String actions, Integer draggable, Integer beforeStart, Integer afterEnd, Integer userID, Integer noteid) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.color = color;
        this.actions = actions;
        this.draggable = draggable;
        this.beforeStart = beforeStart;
        this.afterEnd = afterEnd;
        this.userID = userID;
        this.noteid = noteid;
    }

    public Event(String title, LocalDateTime start, LocalDateTime end, String color, String actions, Integer draggable, Integer beforeStart, Integer afterEnd, Integer userID, Integer noteid, List<Note> note) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.color = color;
        this.actions = actions;
        this.draggable = draggable;
        this.beforeStart = beforeStart;
        this.afterEnd = afterEnd;
        this.userID = userID;
        this.noteid = noteid;
        this.note = note;
    }
}