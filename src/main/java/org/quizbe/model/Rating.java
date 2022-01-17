package org.quizbe.model;


import javax.persistence.*;


/** from symfony project
 * TODO convert to java/spring after install spring security
 *
 * Rating : when a user valuate a question
 *
 * @ORM\Table(name="rating",
uniqueConstraints={@UniqueConstraint(name="only_one_rating", columns={"id_user", "id_question"})}),
indexes={@Index(name="question_idx", columns={"id_question"})})
 * @ORM\Entity(repositoryClass="AppBundle\Entity\RatingRepository")
 * @UniqueEntity(fields={"user", "question"}, message="rating.userquestion")
 */


@Entity
@Table(name = "RATING")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private long id;

}
