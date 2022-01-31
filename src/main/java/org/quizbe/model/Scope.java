package org.quizbe.model;


import org.quizbe.controller.ClassroomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "SCOPE")
public class Scope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Basic
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Classroom classroom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scope scope = (Scope) o;

        return getId() == scope.getId() && getName().equals(scope.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "Scope{"
                + "id=" + id
                + ", name='" + name + '\''
              //  + ", classroom=" + classroom.getName() // Lazy loading
                + '}';
    }
}
