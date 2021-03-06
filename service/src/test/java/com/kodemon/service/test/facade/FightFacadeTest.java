package com.kodemon.service.test.facade;

import com.kodemon.api.dto.FightDTO;
import com.kodemon.api.dto.GymDTO;
import com.kodemon.api.dto.PokemonDTO;
import com.kodemon.api.dto.UserDTO;
import com.kodemon.api.enums.WildPokemonFightMode;
import com.kodemon.api.facade.FightFacade;
import com.kodemon.persistence.entity.*;
import com.kodemon.persistence.enums.PokemonName;
import com.kodemon.persistence.enums.PokemonType;
import com.kodemon.service.config.ServiceConfig;
import com.kodemon.service.facade.FightFacadeImpl;
import com.kodemon.service.interfaces.*;
import com.kodemon.service.util.Pair;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static com.kodemon.service.util.TimeUtils.asDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Oliver Roch
 */

@ContextConfiguration(classes = ServiceConfig.class)
public class FightFacadeTest extends AbstractTestNGSpringContextTests {

    private BeanMappingService beanMappingService;
    private TrainerFightService trainerFightService;
    private PokemonFightService pokemonFightService;
    private PokemonService pokemonService;
    private TrainerService trainerService;
    private GymService gymService;
    private BadgeService badgeService;
    private TimeService timeService;
    private FightFacade fightFacade;

    private Trainer challenger;
    private Trainer defender;
    private UserDTO challengerDTO;
    private UserDTO defenderDTO;
    private Gym targetGym;
    private GymDTO targetGymDTO;
    private Pokemon pikachu;
    private Pokemon onix;
    private PokemonDTO pikachuDTO;
    private PokemonDTO onixDTO;
    private FightDTO fight1;
    private FightDTO fight2;
    private TrainerFight fight1e;
    private TrainerFight fight2e;
    private List<TrainerFight> trainerFights;
    private List<FightDTO> fights;

    @BeforeClass
    public void prepare() {
        beanMappingService = mock(BeanMappingService.class);
        trainerService = mock(TrainerService.class);
        gymService = mock(GymService.class);
        trainerFightService = mock(TrainerFightService.class);
        pokemonFightService = mock(PokemonFightService.class);
        pokemonService = mock(PokemonService.class);
        badgeService = mock(BadgeService.class);
        timeService = mock(TimeService.class);
        fightFacade = new FightFacadeImpl(beanMappingService, trainerFightService, pokemonFightService, pokemonService,
                trainerService, timeService);

        prepareTrainers();
        prepareGym();
        prepareFights();
    }

    @Test
    public void fightForBadgeTest() {
        Date today = new Calendar.Builder().setDate(2015, 4, 1).build().getTime();

        when(trainerService.findByUserName(challenger.getUserName())).thenReturn(Collections.singletonList(challenger));
        when(gymService.findByBadgeName(targetGym.getBadgeName())).thenReturn(Collections.singletonList(targetGym));

        when(beanMappingService.mapTo(pikachuDTO, Pokemon.class)).thenReturn(pikachu);
        when(beanMappingService.mapTo(pikachu, PokemonDTO.class)).thenReturn(pikachuDTO);
        when(beanMappingService.mapTo(challengerDTO, Trainer.class)).thenReturn(challenger);
        when(beanMappingService.mapTo(challenger, UserDTO.class)).thenReturn(challengerDTO);

        when(beanMappingService.mapTo(onixDTO, Pokemon.class)).thenReturn(onix);
        when(beanMappingService.mapTo(onix, PokemonDTO.class)).thenReturn(onixDTO);
        when(beanMappingService.mapTo(defenderDTO, Trainer.class)).thenReturn(defender);
        when(beanMappingService.mapTo(defender, UserDTO.class)).thenReturn(defenderDTO);

        when(beanMappingService.mapTo(targetGymDTO, Gym.class)).thenReturn(targetGym);
        when(beanMappingService.mapTo(targetGym, GymDTO.class)).thenReturn(targetGymDTO);
        when(trainerFightService.wasFightForBadgeSuccessful(challenger, defender)).thenReturn(true);
        Badge badge = new Badge();
        badge.setName("Fake Badge");
        badge.setGym(targetGym);
        when(badgeService.createBadgeOfGym(targetGym)).thenReturn(badge);
        when(timeService.currentDate()).thenReturn(today);
        fightFacade.fightForBadge(challengerDTO, targetGymDTO);

        FightDTO newFightDTO = new FightDTO();
        newFightDTO.setTargetGym(targetGymDTO);
        newFightDTO.setChallenger(challengerDTO);
        newFightDTO.setFightTime(today);
        newFightDTO.setWasChallengerSuccessful(true);

        TrainerFight newFight = new TrainerFight();
        newFight.setTargetGym(targetGym);
        newFight.setChallenger(challenger);
        newFight.setFightTime(today);
        newFight.setWasChallengerSuccessful(true);

        when(beanMappingService.mapTo(newFightDTO, TrainerFight.class)).thenReturn(newFight);

        Mockito.verify(trainerFightService).fightForBadge(challenger, targetGym);
    }

    @Test
    public void fightWildPokemonTest() {
        when(beanMappingService.mapTo(pikachuDTO, Pokemon.class)).thenReturn(pikachu);
        when(beanMappingService.mapTo(pikachu, PokemonDTO.class)).thenReturn(pikachuDTO);
        when(beanMappingService.mapTo(challengerDTO, Trainer.class)).thenReturn(challenger);
        when(beanMappingService.mapTo(challenger, UserDTO.class)).thenReturn(challengerDTO);

        Pokemon randomWildPokemon = new Pokemon();
        randomWildPokemon.setName(PokemonName.ABRA);
        randomWildPokemon.setLevel(1);
        randomWildPokemon.setNickname("Teleportabra");

        when(pokemonService.findById(null)).thenReturn(pikachu);
        when(pokemonFightService.getScorePair(pikachu, randomWildPokemon)).thenReturn(new Pair<Double, Double>(20.0, 1.0));

        PokemonDTO wildPokemonDto = new PokemonDTO();
        wildPokemonDto.setLevel(randomWildPokemon.getLevel());
        wildPokemonDto.setName(randomWildPokemon.getName());
        when(beanMappingService.mapTo(wildPokemonDto, Pokemon.class)).thenReturn(randomWildPokemon);
        when(beanMappingService.mapTo(randomWildPokemon, PokemonDTO.class)).thenReturn(wildPokemonDto);
        List<Trainer> trainers = new ArrayList<>();
        trainers.add(challenger);
        when(trainerService.findByUserName(challengerDTO.getUserName())).thenReturn(trainers);
        fightFacade.fightWildPokemon(challengerDTO, wildPokemonDto, WildPokemonFightMode.TRAIN);
        Mockito.verify(pokemonService).levelPokemonUp(pikachu);
    }

    @Test
    public void listFightsBetweenTest() {
        when(beanMappingService.mapListTo(trainerFights, FightDTO.class)).thenReturn(fights);
        Date from = new Calendar.Builder().setDate(2015, 4, 1).build().getTime();
        Date to = new Calendar.Builder().setDate(2015, 4, 3).build().getTime();
        when(trainerFightService.findByFightTimeBetween(from, to)).thenReturn(trainerFights);
        Collection<FightDTO> fights = fightFacade.listFightsBetween(from, to);
        assertThat(fights.size(), is(2));
    }

    @Test
    public void listTodaysFightTest() {
        LocalDate today = LocalDate.of(2015, Month.APRIL, 1);
        Date todayDate = asDate(today.atStartOfDay());
        Date dayStart = todayDate;
        Date dayEnd = asDate(today.atStartOfDay().plusDays(1));
        when(timeService.currentDate()).thenReturn(todayDate);
        when(timeService.startOfTheDay(todayDate)).thenReturn(dayStart);
        when(timeService.endOfTheDay(todayDate)).thenReturn(dayEnd);
        when(trainerFightService.findByFightTimeBetween(dayStart, dayEnd)).thenReturn(Collections.singletonList(fight1e));
        when(beanMappingService.mapListTo(Collections.singletonList(fight1e), FightDTO.class)).thenReturn(Collections.singletonList(fight1));

        Collection<FightDTO> todaysFights = fightFacade.listTodaysFights();
        assertThat(todaysFights.size(), is(1));
    }

    @Test
    public void listAllFightsTest() {
        when(beanMappingService.mapListTo(trainerFights, FightDTO.class)).thenReturn(fights);
        when(trainerFightService.findAll()).thenReturn(trainerFights);
        Collection<FightDTO> allFights = fightFacade.listAllFights();
        assertThat(allFights.size(), is(2));
    }

    @Test
    public void listFightsOfTrainerTest() {
        when(beanMappingService.mapTo(challengerDTO, Trainer.class)).thenReturn(challenger);
        when(beanMappingService.mapListTo(trainerFights, FightDTO.class)).thenReturn(fights);
        when(trainerFightService.findByChallenger(challenger.getUserName())).thenReturn(trainerFights);
        Collection<FightDTO> fights = fightFacade.listFightsOfTrainer(challengerDTO);
        assertThat(fights.size(), is(2));
    }

    @Test
    public void listFightsOfGymTest() {
        when(beanMappingService.mapTo(targetGymDTO, Gym.class)).thenReturn(targetGym);
        when(beanMappingService.mapListTo(trainerFights, FightDTO.class)).thenReturn(fights);
        when(trainerFightService.findByTargetGymsBadgeName(targetGym.getBadgeName())).thenReturn(trainerFights);
        Collection<FightDTO> fights = fightFacade.listFightsOfGym(targetGymDTO);
        assertThat(fights.size(), is(2));
    }

    private void prepareTrainers() {
        pikachu = new Pokemon();
        pikachu.setName(PokemonName.PIKACHU);
        pikachu.setLevel(20);
        pikachu.setNickname("Yellow mouse");

        pikachuDTO = new PokemonDTO();
        pikachuDTO.setName(PokemonName.PIKACHU);
        pikachuDTO.setLevel(20);
        pikachuDTO.setNickname("Yellow mouse");

        challenger = new Trainer();
        challenger.setFirstName("Ash");
        challenger.setLastName("Ketchum");
        Date dob = new Calendar.Builder().setDate(1987, 4, 1).build().getTime();
        challenger.setDateOfBirth(dob);
        challenger.setUserName("Ash123");
        challenger.addPokemon(pikachu);
        challenger.addActivePokemon(pikachu);

        challengerDTO = new UserDTO();
        challengerDTO.setFirstName("Ash");
        challengerDTO.setLastName("Ketchum");
        challengerDTO.setDateOfBirth(dob);
        challengerDTO.setUserName("Ash123");
        challengerDTO.addPokemon(pikachuDTO);
        challengerDTO.addActivePokemon(pikachuDTO);

        pikachu.setTrainer(challenger);
        pikachuDTO.setTrainer(challengerDTO);

        onix = new Pokemon();
        onix.setName(PokemonName.ONIX);
        onix.setLevel(5);
        onix.setNickname("The Rock");

        onixDTO = new PokemonDTO();
        onixDTO.setName(PokemonName.ONIX);
        onixDTO.setLevel(5);
        onixDTO.setNickname("The Rock");

        defender = new Trainer();
        defender.setFirstName("Brock");
        defender.setLastName("Takechi");
        Date dob2 = new Calendar.Builder().setDate(1984, 2, 3).build().getTime();
        defender.setDateOfBirth(dob2);
        defender.setUserName("Brocky123");
        defender.addPokemon(onix);
        defender.addActivePokemon(onix);

        defenderDTO = new UserDTO();
        defenderDTO.setFirstName("Brock");
        defenderDTO.setLastName("Takechi");
        defenderDTO.setDateOfBirth(dob2);
        defenderDTO.setUserName("Brocky123");
        defenderDTO.addPokemon(onixDTO);
        defenderDTO.addActivePokemon(onixDTO);

        onix.setTrainer(defender);
        onixDTO.setTrainer(defenderDTO);
    }

    private void prepareGym() {
        targetGym = new Gym();
        targetGym.setTrainer(defender);
        targetGym.setCity("Violet city");
        targetGym.setType(PokemonType.GROUND);
        targetGym.setBadgeName("Cool Badge");

        targetGymDTO = new GymDTO();
        targetGymDTO.setTrainer(defenderDTO);
        targetGymDTO.setCity("Violet city");
        targetGymDTO.setType(PokemonType.GROUND);
        targetGymDTO.setBadgeName("Cool Badge");
    }

    private void prepareFights() {
        fight1 = new FightDTO();
        fight1.setWasChallengerSuccessful(false);
        Date dof1 = new Calendar.Builder().setDate(2015, 4, 1).build().getTime();
        fight1.setFightTime(dof1);
        fight1.setChallenger(challengerDTO);
        fight1.setTargetGym(targetGymDTO);

        fight2 = new FightDTO();
        fight2.setWasChallengerSuccessful(false);
        Date dof2 = new Calendar.Builder().setDate(2015, 4, 3).build().getTime();
        fight2.setFightTime(dof2);
        fight2.setChallenger(challengerDTO);
        fight2.setTargetGym(targetGymDTO);

        fight1e = new TrainerFight();
        fight1e.setWasChallengerSuccessful(false);
        fight1e.setFightTime(dof1);
        fight1e.setChallenger(challenger);
        fight1e.setTargetGym(targetGym);

        fight2e = new TrainerFight();
        fight2e.setWasChallengerSuccessful(false);
        fight2e.setFightTime(dof2);
        fight2e.setChallenger(challenger);
        fight2e.setTargetGym(targetGym);

        trainerFights = new ArrayList<>();
        fights = new ArrayList<>();

        trainerFights.add(fight1e);
        trainerFights.add(fight2e);

        fights.add(fight1);
        fights.add(fight2);
    }

    @AfterMethod
    void resetMocks() {
        Mockito.reset(beanMappingService);
        Mockito.reset(trainerService);
        Mockito.reset(pokemonFightService);
        Mockito.reset(trainerFightService);
        Mockito.reset(timeService);
        Mockito.reset(pokemonService);
        Mockito.reset(badgeService);
    }
}
