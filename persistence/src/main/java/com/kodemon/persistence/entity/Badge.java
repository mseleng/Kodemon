package com.kodemon.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity that represents a badge a trainer can earn from a gym.
 * <p>
 * A trainer earns a badge, by defeating gym leader.
 *
 * @author Oliver Roch
 */

@Entity
@Table(name = "BADGE", schema = "APP")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String name;

    @OneToOne
    @NotNull
    private Gym gym;

    /**
     * Parameterless constructor for (not only) persistence purposes.
     */
    public Badge() {
    }

    /**
     * Constructor which initialize a gym of the badge and trainer to own it
     *
     * @param gym where can this badge be earned
     */
    public Badge(@NotNull Gym gym) {
        this.gym = gym;
        this.name = gym.getBadgeName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gym getGym() {
        return gym;
    }

    public void setGym(Gym gym) {
        this.gym = gym;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Badge badge = (Badge) o;

        if (getId() != null ? !getId().equals(badge.getId()) : badge.getId() != null) return false;
        return (getName() != null ? getName().equals(badge.getName()) : badge.getName() == null) && getGym().equals(badge.getGym());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + getGym().hashCode();
        return result;
    }
}
