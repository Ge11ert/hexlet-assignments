package exercise.model;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.IDENTITY;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

// BEGIN
@Entity
@Table(name = "products")
@Getter
@Setter
@EqualsAndHashCode(of = {"title", "price"})
public class Product {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column
  private String title;

  @Column
  private Integer price;
}
// END
