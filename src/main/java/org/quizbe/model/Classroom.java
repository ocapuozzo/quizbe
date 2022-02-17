package org.quizbe.model;


import javax.persistence.*;
import java.util.*;

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

    @Basic
    @Column(nullable = false)
    private boolean visible = true;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Scope> scopes;

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

    public void setScopes(List<Scope> scopes) {
        this.scopes = scopes;
    }

    public void addScope(Scope scope) {
        if (!this.scopes.contains(scope)) {
            this.scopes.add(scope);
            scope.setClassroom(this);
        }
    }
    public void removeScope(Scope scope) {
        scope.setClassroom(null);
        this.scopes.remove(scope);
    }

    public void removeScopes() {
        Iterator<Scope> it = this.scopes.iterator();
        while (it.hasNext()) {
            Scope scope = it.next();
            scope.setClassroom(null);
            it.remove();
        }
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
