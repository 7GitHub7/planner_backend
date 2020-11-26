package pl.programowaniezespolowe.planner.note;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.programowaniezespolowe.planner.event.Event;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "note", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private Date date;

    @Column(name = "userid")
    private int userid;

    @ManyToOne
    //@JsonIgnore
    @JoinTable(
            name="event", schema = "public",
            joinColumns = {@JoinColumn(name = "id")},
            inverseJoinColumns = {@JoinColumn(name = "noteid")}
    )
    private Event event;

    public Note(String title, String description, Date date, int userid) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.userid = userid;
    }

    public Note(String title, String description, Date date, int userid, Event event) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.userid = userid;
        this.event = event;
    }
}
