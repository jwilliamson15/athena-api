package com.athena.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athena.dal.ConsultantDAL;
import com.athena.model.Consultant;

@RestController
@RequestMapping("/consultants")
public class ConsultantsController {

    private static Logger LOGGER = LoggerFactory.getLogger(ConsultantsController.class);

    @Autowired
    private ConsultantDAL consultantDAL;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Consultant> getAllConsultants() {
        return consultantDAL.getAllConsultant();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Consultant getConsultantById(@PathVariable("id") ObjectId id) {
        return consultantDAL.findById(id);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<Consultant> getConsultantsBySkill(@RequestParam("skills") List<String> skillNames,
                                                  @RequestParam(name = "experience", required = false) List<String> experienceTimes) {
        List<Consultant> searchResult;

        LOGGER.info("SKILLS SEARCHED: " + skillNames.toString());
        LOGGER.info("XP SEARCHED: " + experienceTimes.toString());

        if (skillNames.size() == 1) {
            searchResult = consultantDAL.findBySkill(skillNames.get(0));
            LOGGER.info("RESULTS COUNT: "+searchResult.size());
            return searchResult;
        }

        searchResult = consultantDAL.findMultipleSkills(skillNames);
        LOGGER.info("RESULTS COUNT: "+searchResult.size());
        return searchResult;
    }

    //TODO - advanced searches
//    @RequestMapping(value = "/search", method = RequestMethod.GET)
//    public List<Consultant> getConsultantsBySkillParam(@RequestParam("skills") List<String> skillNames,
//                                                       @RequestParam(name = "experience", required = false) List<String> experienceTimes) {
//        LOGGER.warn("Skill names: {}", skillNames.toString());
//
//        if (isNotNullOrEmpty(experienceTimes)) {
//            return advancedQueries.searchWithExperienceQuery(skillNames, experienceTimes);
//        }
//
//        //if skillsNames > 1
//        if (skillNames.size() > 1) {
//            return advancedQueries.searchForMultiSkillsOr(skillNames);
//        }
//
//        return repository.findBySingleSkill(skillNames.get(0));
//    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Consultant createConsultant(@Valid @RequestBody Consultant consultant) {
        return consultantDAL.saveConsultant(consultant);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void modifyConsultantsById(@PathVariable("id") ObjectId id, @Valid @RequestBody Consultant consultant) {
        consultant.set_id(id);
        consultantDAL.updateConsultant(consultant);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteConsultant(@PathVariable ObjectId id) {
        consultantDAL.deleteConsultant(consultantDAL.findById(id));
    }

    boolean isNotNullOrEmpty(List<String> array) {
        if (array == null) {
            return false;
        }
        return (!array.isEmpty());
    }

}
