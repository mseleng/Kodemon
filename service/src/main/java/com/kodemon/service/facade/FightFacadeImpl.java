package com.kodemon.service.facade;

import com.kodemon.api.dto.FightDTO;
import com.kodemon.api.dto.GymDTO;
import com.kodemon.api.dto.UserDTO;
import com.kodemon.api.enums.WildPokemonFightMode;
import com.kodemon.api.facade.FightFacade;
import com.kodemon.persistence.entity.*;
import com.kodemon.service.interfaces.*;
import com.kodemon.service.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Oliver Roch
 */

@Service
@Transactional
public class FightFacadeImpl implements FightFacade {

    private BeanMappingService beanMappingService;
    private TrainerFightService trainerFightService;
    private PokemonFightService pokemonFightService;
    private PokemonService pokemonService;
    private TrainerService trainerService;
    private BadgeService badgeService;
    private TimeService timeService;

    @Inject
    public FightFacadeImpl(
            BeanMappingService beanMappingService,
            TrainerFightService trainerFightService,
            PokemonFightService pokemonFightService,
            PokemonService pokemonService,
            TrainerService trainerService,
            BadgeService badgeService,
            TimeService timeService) {
        this.beanMappingService = beanMappingService;
        this.trainerFightService = trainerFightService;
        this.pokemonFightService = pokemonFightService;
        this.pokemonService = pokemonService;
        this.trainerService = trainerService;
        this.badgeService = badgeService;
        this.timeService = timeService;
    }

    @Override
    public void fightForBadge(UserDTO user, GymDTO gym) {
        Trainer challengingTrainer = beanMappingService.mapTo(user, Trainer.class);
        Gym targetGym = beanMappingService.mapTo(gym, Gym.class);

        boolean wasChallengerSuccessful = false;
        if (trainerFightService.wasFightForBadgeSuccessful(challengingTrainer, targetGym.getTrainer())) {
            Badge badge = badgeService.createBadgeOfGym(targetGym);
            badgeService.assignTrainerToBadge(challengingTrainer, badge);
            trainerService.addBadge(badge, challengingTrainer);
            wasChallengerSuccessful = true;
        }

        FightDTO fight = new FightDTO();
        fight.setTargetGym(gym);
        fight.setChallenger(user);
        fight.setFightTime(timeService.currentDate());
        fight.setWasChallengerSuccessful(wasChallengerSuccessful);
        trainerFightService.save(beanMappingService.mapTo(fight, TrainerFight.class));
    }

    @Override
    public void fightWildPokemon(UserDTO user, WildPokemonFightMode mode) {
        Trainer trainer = beanMappingService.mapTo(user, Trainer.class);
        Pokemon wildPokemon = pokemonService.generateWildPokemon(null);
        Pokemon trainersPokemon = pokemonService.findByTrainer(trainer).get(0);

        Pair<Double, Double> fightScore = pokemonFightService.getScorePair(trainersPokemon, wildPokemon);

        if (fightScore.getX() > fightScore.getY()) {
            if (mode == WildPokemonFightMode.TRAIN) {
                pokemonService.levelPokemonUp(trainersPokemon);
            } else if (mode == WildPokemonFightMode.CATCH) {
                pokemonService.save(wildPokemon);
                trainerService.addPokemon(wildPokemon, trainer);
            }
        }
    }

    @Override
    public List<FightDTO> listTodaysFights() {
        return listFightsBetween(timeService.startOfTheDay(timeService.currentDate()), timeService.endOfTheDay(timeService.currentDate()));
    }

    @Override
    public List<FightDTO> listThisMonthsFights() {
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Date from = new Calendar.Builder().setDate(currentYear, currentMonth, 1).build().getTime();
        return listFightsBetween(from, timeService.currentDate());
    }

    @Override
    public List<FightDTO> listThisYearsFights() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Date from = new Calendar.Builder().setDate(currentYear, 1, 1).build().getTime();
        return listFightsBetween(from, timeService.currentDate());
    }

    @Override
    public List<FightDTO> listFightsBetween(Date from, Date to) {
        return beanMappingService.mapCollectionTo(trainerFightService.findByFightTimeBetween(from, to), FightDTO.class);
    }

    @Override
    public List<FightDTO> listAllFights() {
        return beanMappingService.mapCollectionTo(trainerFightService.findAll(), FightDTO.class);
    }

    @Override
    public List<FightDTO> listFightsOfTrainer(UserDTO user) {
        Trainer challenger = beanMappingService.mapTo(user, Trainer.class);
        return beanMappingService.mapCollectionTo(trainerFightService.findByChallenger(challenger), FightDTO.class);
    }

    @Override
    public List<FightDTO> listFightsOfGym(GymDTO gym) {
        Gym targetGym = beanMappingService.mapTo(gym, Gym.class);
        return beanMappingService.mapCollectionTo(trainerFightService.findByTargetGym(targetGym), FightDTO.class);
    }
}
