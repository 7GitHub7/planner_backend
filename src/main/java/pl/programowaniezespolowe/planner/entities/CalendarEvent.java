package pl.programowaniezespolowe.planner.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
//@Table(name = "external_transfers")
@NoArgsConstructor
public class CalendarEvent {
    @Column(name="id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "startdate")
    private LocalDateTime start;

    @Column(name = "enddate")
    private LocalDateTime end;

    // powinien zawierac primary i secondary color - na razie niech zostanie to ustawiane na froncie
    @Column(name = "color")
    private String[] color;

    //opcjonalne - nie obslugujemy tego na razie na backu - na razie na froncie bedzie dodawane recznie
    //okresla akcje dostepne dla eventu np edycje lub dodawanie notatki
    //w przyszlosci mozna zmienic na enum
    @Column(name = "actions")  //a11yLabel: 'Edit' lub a11yLabel: 'Note'
    private String[] actions;

    //opcjonalne - nie obslugujemy tego na razie
    @Column(name = "draggable")
    private Integer draggable;

    //opcjonalne - nie obslugujemy tego na razie
    @Column(name = "beforestart")
    private Integer beforeStart;

    //opcjonalne - nie obslugujemy tego na razie
    @Column(name = "after")
    private Integer afterEnd;
}

//back musi obsluzyc odebranie i wyslanie obiektu calendarevent w Event
//calendar event ma byc zwracany/wysylany z:
// id, title, start, end (end jest opcjonalne)
