package com.kodemon.service.interfaces;

import com.kodemon.persistence.entity.Gym;
import com.kodemon.persistence.entity.Trainer;
import com.kodemon.persistence.enums.PokemonType;
import com.kodemon.service.util.PasswordStorage;

import java.util.List;

/**
 * Service for doing stuff with {@link Gym}s
 *
 * @author Miso Romanek
 */
public interface GymService {

    /**
     * Initializes all gyms
     * <p/>
     * This method takes care of the creation and proper initialization of all gyms
     */
    void initializeGyms() throws PasswordStorage.CannotPerformOperationException;

    /**
     * Saves the given gym to the database
     *
     * @param gym gym to be saved to database
     */
    void save(Gym gym);

    /**
     * Deletes the given gym from the database
     *
     * @param gym gym to be deleted from database
     */
    void delete(Gym gym);

    /**
     * Find gym with the specified id
     *
     * @param id id of specific gym
     * @return gym with the specified id
     */
    Gym findById(Long id);

    /**
     * Returns a {@link List} of {@link Gym}s with the given username.
     *
     * @param city City to search for.
     * @return {@link List} of {@link Gym}s in the given city.
     */
    List<Gym> findByCity(String city);

    /**
     * Returns a {@link List} of {@link Gym}s with similar cities to the given one.
     * <p/>
     * Note: this method generates the following query: {@code … where x.city like ?1}
     *
     * @param city City to search for.
     * @return {@link List} of {@link Gym}s with similar cities to the given one.
     */
    List<Gym> findByCityLike(String city);


    /**
     * Returns a {@link List} of {@link Gym}s where the city contains the given string.
     * <p/>
     * Note: a parameter <b>must</b> be wrapped in {@code %} as the method generates the following query:
     * {@code … where x.city like ?1}
     *
     * @param string String contained in {@link Gym}'s city.
     * @return {@link List} of {@link Gym}s with cities containing the given string.
     */
    List<Gym> findByCityContaining(String string);

    /**
     * Returns a {@link List} of {@link Gym}s with the given badge name.
     *
     * @param badgeName Badge name to search for.
     * @return {@link List} of {@link Gym}s with the given badge name.
     */
    List<Gym> findByBadgeName(String badgeName);

    /**
     * Returns a {@link List} of {@link Gym}s with similar badge names to the given one.
     * <p/>
     * Note: this method generates the following query: {@code … where x.badgeName like ?1}
     *
     * @param badgeName Badge name to search for.
     * @return {@link List} of {@link Gym}s with similar cities to the given one.
     */
    List<Gym> findByBadgeNameLike(String badgeName);


    /**
     * Returns a {@link List} of {@link Gym}s where the badge name contains the given string.
     * <p/>
     * Note: a parameter <b>must</b> be wrapped in {@code %} as the method generates the following query:
     * {@code … where x.badgeName like ?1}
     *
     * @param string String contained in {@link Gym}'s badge name.
     * @return {@link List} of {@link Gym}s with badge names containing the given string.
     */
    List<Gym> findByBadgeNameContaining(String string);

    /**
     * Returns a {@link List} of {@link Gym}s with given PokemonType.
     *
     * @param type {@link PokemonType} to search for.
     * @return {@link List} of {@link Gym}s of the given type.
     */
    List<Gym> findByType(PokemonType type);

    /**
     * Returns a {@link List} of {@link Gym}s of the given trainer.
     * <p/>
     * Note: The list always contains only one gym.
     *
     * @param trainer {@link Trainer} to search for.
     * @return {@link List} of {@link Gym}s of the given trainer.
     */
    List<Gym> findByTrainer(Trainer trainer);

    /**
     * Returns a {@link List} of all {@link Gym}s in database.
     *
     * @return {@link List} of {@link Gym}s in the database.
     */
    List<Gym> findAll();
}
