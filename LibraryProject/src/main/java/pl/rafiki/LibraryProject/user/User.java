package pl.rafiki.LibraryProject.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "LibraryUser")
@Table(
        name = "libraryUser",
        uniqueConstraints = {
            @UniqueConstraint(name = "user_email_unique", columnNames = "email")
        }
)
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "first_name",
            nullable = false
    )
    private String firstname;

    @Column(
            name = "last_name",
            nullable = false
    )
    private String lastname;

    @Column(
            name = "email",
            nullable = false
    )
    private String email;

    @Column(
            name = "date_of_birthday",
            nullable = false
    )
    private LocalDate dateOfBirth;

    @Transient
    private int age;

    public User(String firstname, String lastname, String email, LocalDate dateOfBirth) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return Period.between(this.dateOfBirth, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", age=" + age +
                '}';
    }
}
