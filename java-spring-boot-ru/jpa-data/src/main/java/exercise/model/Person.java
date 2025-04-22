package exercise.model;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;
import lombok.Getter;
import lombok.Setter;

// BEGIN
@Entity
@Table(name = "people")
@Getter
@Setter
public class Person {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column
  String firstName;

  @Column
  String lastName;
}
// END
