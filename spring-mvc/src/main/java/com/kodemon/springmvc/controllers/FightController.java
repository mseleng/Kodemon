package com.kodemon.springmvc.controllers;

import com.kodemon.api.dto.FightDTO;
import com.kodemon.api.dto.GymDTO;
import com.kodemon.api.dto.UserAuthDTO;
import com.kodemon.api.dto.UserDTO;
import com.kodemon.api.facade.FightFacade;
import com.kodemon.api.facade.UserFacade;
import com.kodemon.persistence.entity.Gym;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;

/**
 *  @author Matej Poklemba
 */

@Controller
@RequestMapping("/fight")
public class FightController {

    final static Logger LOG = LoggerFactory.getLogger(FightController.class);

    @Inject
    private FightFacade fightFacade;

    /**
     * Show list of all fights.
     *
     * @param model data to display
     * @return JSP page name
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam String period, Model model) {
        Collection<FightDTO> fights;
        switch (period)
        {
            case "year":
                fights = fightFacade.listThisYearsFights();
                break;
            case "month":
                fights = fightFacade.listThisMonthsFights();
                break;
            case "today":
                fights = fightFacade.listTodaysFights();
                break;
            default:
                fights = fightFacade.listAllFights();
                break;
        }
        model.addAttribute("fights", fights);
        LOG.debug("list" + period + "()");
        return "fight/list";
    }

    /**
     * Show list of given gym's fights.
     *
     * @param gym which gym's fights do display
     * @param model data to display
     * @return JSP page name
     */
    @RequestMapping(value = "/listFightsOfGym", method = RequestMethod.GET)
    public String listFightsOfGym(@PathVariable GymDTO gym, Model model) {
        model.addAttribute("fights", fightFacade.listFightsOfGym(gym));
        LOG.debug("listFightsOfGym()");
        return "fight/list";
    }

    /**
     * Show list of given user's fights.
     *
     * @param user which user's fights do display
     * @param model data to display
     * @return JSP page name
     */
    @RequestMapping(value = "/listFightsOfUser", method = RequestMethod.GET)
    public String listFightsOfUser(@PathVariable UserDTO user, Model model) {
        model.addAttribute("fights", fightFacade.listFightsOfTrainer(user));
        LOG.debug("listFightsOfUser()");
        return "fight/list";
    }

    /**
     * Show detail of fight specified by its id
     *
     * @param id which id is to be shown
     * @param model data to display
     * @return JSP page name
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("fight", fightFacade.findFightById(id));
        return "fight/detail";
    }
}
