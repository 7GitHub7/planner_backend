package pl.programowaniezespolowe.planner.note;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.programowaniezespolowe.planner.event.Event;

import javax.persistence.*;
import java.util.Date;


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

    @Column(name = "eventid")
    private int eventid;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventid",insertable=false, updatable=false)
    private Event mevent;

    public Note(String title, String description, Date date, int userid, int eventid) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.userid = userid;
        this.eventid = eventid;
    }

}
