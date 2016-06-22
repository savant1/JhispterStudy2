package com.thedevbridge.jhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.thedevbridge.jhipster.domain.enumeration.Typeouvrage;

/**
 * A Ouvrage.
 */
@Entity
@Table(name = "ouvrage")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "ouvrage")
public class Ouvrage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "filiere")
    private String filiere;

    @Column(name = "categorie")
    private String categorie;

    @Column(name = "discipline")
    private String discipline;

    @Column(name = "titre")
    private String titre;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Typeouvrage genre;

    @OneToMany(mappedBy = "ouvrage")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Realiser> realises = new HashSet<>();

    @OneToMany(mappedBy = "ouvrage")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Emprunter> empruntes = new HashSet<>();

    @OneToMany(mappedBy = "ouvrage")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Commenter> commentes = new HashSet<>();

    @OneToMany(mappedBy = "ouvrage")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Noter> notes = new HashSet<>();

    @OneToMany(mappedBy = "ouvrage")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Recommander> recommandes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Typeouvrage getGenre() {
        return genre;
    }

    public void setGenre(Typeouvrage genre) {
        this.genre = genre;
    }

    public Set<Realiser> getRealises() {
        return realises;
    }

    public void setRealises(Set<Realiser> realisers) {
        this.realises = realisers;
    }

    public Set<Emprunter> getEmpruntes() {
        return empruntes;
    }

    public void setEmpruntes(Set<Emprunter> emprunters) {
        this.empruntes = emprunters;
    }

    public Set<Commenter> getCommentes() {
        return commentes;
    }

    public void setCommentes(Set<Commenter> commenters) {
        this.commentes = commenters;
    }

    public Set<Noter> getNotes() {
        return notes;
    }

    public void setNotes(Set<Noter> noters) {
        this.notes = noters;
    }

    public Set<Recommander> getRecommandes() {
        return recommandes;
    }

    public void setRecommandes(Set<Recommander> recommanders) {
        this.recommandes = recommanders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ouvrage ouvrage = (Ouvrage) o;
        if(ouvrage.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, ouvrage.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Ouvrage{" +
            "id=" + id +
            ", filiere='" + filiere + "'" +
            ", categorie='" + categorie + "'" +
            ", discipline='" + discipline + "'" +
            ", titre='" + titre + "'" +
            ", genre='" + genre + "'" +
            '}';
    }
}
