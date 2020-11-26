package pl.programowaniezespolowe.planner.note;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    public Note(String title, String description, Date date, int userid, int eventid) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.userid = userid;
        this.eventid = eventid;
    }

}
