package org.quizbe.model;


import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;


@Entity
@DynamicInsert
   // see https://thorben-janssen.com/dynamic-inserts-and-updates-with-spring-data-jpa/
   // https://stackoverflow.com/questions/21721818/why-does-not-hibernate-set-dynamicinsert-by-default
   // Question may be updated frequently (Response also, to test before :)
@Table(name = "QUESTION")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private long id;

    @Basic
    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Lob
    @Column(name = "SENTENCE", nullable = false)
    private String sentence;

    @Basic
    @Column(name = "DATECREA", nullable = false, length = 50)
    private Date datecrea;

    /// trace de la personne conceptrice et co-conceptrice
    @Basic
    @Column(name = "DESIGNER", nullable = false, length = 50)
    private String designer;

    @Basic
    @Column(name = "CODESIGNER", nullable = false, length = 50)
    private String codesigner;
    ///

    @ManyToOne
    private Scope scope;

    @ManyToOne
    private Topic topic;

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

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public Date getDatecrea() {
        return datecrea;
    }

    public void setDatecrea(Date datecrea) {
        this.datecrea = datecrea;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getCodesigner() {
        return codesigner;
    }

    public void setCodesigner(String codesigner) {
        this.codesigner = codesigner;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question scope = (Question) o;
        return getId() == scope.getId() && getName().equals(scope.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sentence='" + sentence + '\'' +
                ", datecrea=" + datecrea +
                ", designer='" + designer + '\'' +
                ", codesigner='" + codesigner + '\'' +
                '}';
    }
}
