package com.kodemon.service.interfaces;

import com.kodemon.persistence.entity.Badge;
import com.kodemon.persistence.entity.Gym;
import com.kodemon.persistence.entity.Trainer;

import java.util.List;

/**
 * Service for doing stuff with {@link Badge}s
 *
 * @author <a href="mailto:xseleng@fi.muni.cz">Maros Seleng, 422624</a>
 */
public interface BadgeService {

    /**
     * Generates new badge of the given gym.
     * <p/>
     * This method only creates a new instance of {@link Badge} and sets its gym.
     * This method does <b>not</b> set a {@link Trainer} to the badge.
     * This method does <b>not</b> take care of saving the {@link Badge} entity to the database.
     *
     * @param gym gym where the badge is awarded from
     * @return returns the created badge
     */
    Badge createBadgeOfGym(Gym gym);

    /**
     * Saves the given badge to the database.
     *
     * @param badge badge to be saved
     */
    void save(Badge badge);

    /**
     * Deletes the given badge from the database.
     *
     * @param badge badge to be deleted
     */
    void delete(Badge badge);

    /**
     * Returns a {@link List} of {@link Badge}s with given name
     *
     * @param name Name to search for
     * @return {@link List} of {@link Badge}s with given name
     */
    List<Badge> findByName(String name);

    /**
     * Returns a {@link List} of {@link Badge}s with name starting with the given prefix.
     * <p/>
     * Note: the prefix <b>must</b> be appended by {@code %} as the method generates the following query:
     * {@code … where x.name like ?1}
     *
     * @param prefix Name prefix to search by
     * @return {@link List} of {@link Badge}s with names prefixed with the given prefix
     */
    List<Badge> findByNameStartingWith(String prefix);

    /**
     * Returns a {@link List} of {@link Badge}s belonging to given {@link Gym}
     *
     * @param gym Gym whose badges to search for
     * @return {@link List} of {@link Badge}s belonging to given {@link Gym}
     */
    List<Badge> findByGym(Gym gym);
}
