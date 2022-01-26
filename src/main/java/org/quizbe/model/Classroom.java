package org.quizbe.model;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "CLASSROOM")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private long id;

    @Basic
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    private Collection<Scope> scopes;

    @ManyToOne
    private User teacher;

    public Classroom() {
        this.scopes = new ArrayList<Scope>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Scope> getScopes() {
        return scopes;
    }

    public void setScopes(Collection<Scope> scopes) {
        this.scopes = scopes;
    }

    public void addScope(Scope scope) {
        if (!this.scopes.contains(scope)) {
            this.scopes.add(scope);
            scope.setClassroom(this);
        }
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

}
