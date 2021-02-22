package com.athena.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athena.dal.ConsultantDAL;
import com.athena.dal.DynamicQueryParameter;
import com.athena.model.Consultant;
import com.athena.model.SkillLevel;

@CrossOrigin
@RestController
@RequestMapping("/consultants")
public class ConsultantsController {

    private static Logger LOGGER = LoggerFactory.getLogger(ConsultantsController.class);

    @Autowired
    private ConsultantDAL consultantDAL;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Consultant> getAllConsultants() {
        LOGGER.info("GET REQUEST");
        return consultantDAL.getAllConsultant();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Consultant getConsultantById(@PathVariable("id") ObjectId id) {
        return consultantDAL.findById(id);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<Consultant> getConsultantsBySkill(@RequestParam("skills") List<String> skillNames,
                                                  @RequestParam(name = "experience", required = false) List<String> experienceTimes,
                                                  @RequestParam(name = "skillLevel", required = false) List<String> skillLevels) {
        List<Consultant> searchResult;

        try {
            LOGGER.info("SKILLS SEARCHED: " + skillNames.toString());
            LOGGER.info("XP SEARCHED: " + experienceTimes.toString());
            LOGGER.info("SKILL LEVELS: " + skillLevels.toString());
        } catch (NullPointerException npe) {
            LOGGER.info("npe thrown due to optional params not provided. Only affects logging line");
        }

        searchResult = dynamicSearch(createDynamicQuery(skillNames, experienceTimes, skillLevels));

        return searchResult;
    }

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

    private List<Consultant> dynamicSearch(List<DynamicQueryParameter> dynamicQuery) {
        LOGGER.info("DYNAMIC QUERY: "+dynamicQuery.toString());
        List<Consultant> searchResult = consultantDAL.findByDynamicQuery(dynamicQuery);
        LOGGER.info("RESULTS COUNT: "+searchResult.size());
        return searchResult;
    }

    private List<DynamicQueryParameter> createDynamicQuery(List<String> skillNames, List<String> experienceTimes, List<String> skillLevels) {
        List<DynamicQueryParameter> dynamicQuery = new ArrayList<>();

        for (int i = 0; i < skillNames.size(); i++) {
            DynamicQueryParameter queryParameter = new DynamicQueryParameter();
            queryParameter.setSkillName(skillNames.get(i));

            if (isNotNullOrEmpty(experienceTimes)) {
                queryParameter.setExperienceTime(Integer.parseInt(experienceTimes.get(i)));
            }

            if (isNotNullOrEmpty(skillLevels)) {
                queryParameter.setSkillLevel(SkillLevel.valueOf(skillLevels.get(i).toUpperCase()));
            }

            dynamicQuery.add(queryParameter);
        }

        return dynamicQuery;
    }

    boolean isNotNullOrEmpty(List<String> array) {
        if (array == null) {
            return false;
        }
        return (!array.isEmpty());
    }

}
