package pl.programowaniezespolowe.planner.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "groupid")
    private Integer groupid;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "permission")
    private String permission;

    @Column(name = "logged")
    private boolean logged;



    public User(String name, String surname, int groupid, String email, String password, String permission, boolean logged) {
        this.name = name;
        this.surname = surname;
        this.groupid = groupid;
        this.email = email;
        this.password = password;
        this.permission = permission;
        this.logged = logged;
    }


}