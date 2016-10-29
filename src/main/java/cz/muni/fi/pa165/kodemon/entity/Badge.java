package cz.muni.fi.pa165.kodemon.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Entity that represents a badge a trainer can earn from a gym.
 *
 * A trainer earns a badge, by defeating gym leader.
 *
 * @author Oliver Roch
 */

@Entity
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

    @ManyToOne // this is the BIDIRECTIONAL OWNING side
    @NotNull
    private Trainer trainer;

    /**
     * Parameterless constructor for (not only) persistence purposes.
     */
    public Badge() {}

    /**
     * Constructor which initialize a gym of the badge and trainer to own it
     *
     * @param gym where can this badge be earned
     */
    public Badge(@NotNull Gym gym, @NotNull Trainer trainer) {
        this.gym = gym;
        this.trainer = trainer;
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

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Badge)) return false;

        Badge badge = (Badge) o;

        if (getId() != null && !getId().equals(badge.getId())) return false;
        if (!getName().equals(badge.getName())) return false;
        if (!getGym().equals(badge.getGym())) return false;
        return getTrainer().equals(badge.getTrainer());

    }

    @Override
    public int hashCode() {
        int result = getId() == null ? 0 : getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getGym().hashCode();
        result = 31 * result + getTrainer().hashCode();
        return result;
    }
}
