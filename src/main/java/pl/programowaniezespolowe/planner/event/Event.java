package pl.programowaniezespolowe.planner.event;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "event", schema = "public")
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

    public Event() {
    }


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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public Integer getDraggable() {
        return draggable;
    }

    public void setDraggable(Integer draggable) {
        this.draggable = draggable;
    }

    public Integer getBeforeStart() {
        return beforeStart;
    }

    public void setBeforeStart(Integer beforeStart) {
        this.beforeStart = beforeStart;
    }

    public Integer getAfterEnd() {
        return afterEnd;
    }

    public void setAfterEnd(Integer afterEnd) {
        this.afterEnd = afterEnd;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getNoteID() {
        return noteID;
    }

    public void setNoteID(Integer noteID) {
        this.noteID = noteID;
    }
}