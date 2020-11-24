package pl.programowaniezespolowe.planner.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


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
    private Integer noteID;


    public Event( String title, LocalDateTime start, LocalDateTime end, String color, String actions, Integer draggable, Integer beforeStart, Integer afterEnd, Integer userID, Integer noteID) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.color = color;
        this.actions = actions;
        this.draggable = draggable;
        this.beforeStart = beforeStart;
        this.afterEnd = afterEnd;
        this.userID = userID;
        this.noteID = noteID;
    }

}