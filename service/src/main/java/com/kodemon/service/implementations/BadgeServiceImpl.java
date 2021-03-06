package com.kodemon.service.implementations;

import com.kodemon.persistence.dao.BadgeDao;
import com.kodemon.persistence.entity.Badge;
import com.kodemon.persistence.entity.Gym;
import com.kodemon.service.interfaces.BadgeService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Badge service implementation
 * <p>
 * Can create new badge of a gym, assign a trainer, save and find a badge.
 *
 * @author Miso Romanek
 */
@Service
public class BadgeServiceImpl implements BadgeService {

    private BadgeDao badgeDao;

    @Inject
    public BadgeServiceImpl(BadgeDao badgeDao) {
        this.badgeDao = badgeDao;
    }

    @Override
    public Badge createBadgeOfGym(Gym gym) {
        return new Badge(gym);
    }

    @Override
    public void save(Badge badge) {
        badgeDao.save(badge);
    }

    @Override
    public void delete(Badge badge) {
        badgeDao.delete(badge);
    }

    @Override
    public List<Badge> findByName(String name) {
        return badgeDao.findByName(name);
    }

    @Override
    public List<Badge> findByNameStartingWith(String prefix) {
        return badgeDao.findByNameStartingWith(prefix);
    }

    @Override
    public List<Badge> findByGym(Gym gym) {
        return badgeDao.findByGym(gym);
    }
}
