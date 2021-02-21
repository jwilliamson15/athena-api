package com.athena.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.athena.dal.DynamicQueryParameter;
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
        List<Consultant> searchResult = new ArrayList<>();

        LOGGER.info("SKILLS SEARCHED: " + skillNames.toString());
        LOGGER.info("XP SEARCHED: " + experienceTimes.toString());

        if (skillNames.size() == 1) {
            searchResult = singleSkillSearch(skillNames.get(0));
        }

        if (isNullOrEmpty(experienceTimes)) {
            searchResult = multiSkillSearch(skillNames);
        }

        searchResult = experienceSearch(createDynamicQuery(skillNames, experienceTimes));

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

    private List<Consultant> singleSkillSearch(String skillName) {
        List<Consultant> searchResult = consultantDAL.findBySkill(skillName);
        LOGGER.info("RESULTS COUNT: "+searchResult.size());
        return searchResult;
    }

    private List<Consultant> multiSkillSearch(List<String> skillNames) {
        List<Consultant> searchResult = consultantDAL.findMultipleSkills(skillNames);
        LOGGER.info("RESULTS COUNT: "+searchResult.size());
        return searchResult;
    }

    private List<Consultant> experienceSearch(List<DynamicQueryParameter> dynamicQuery) {
        LOGGER.info("DYNAMIC QUERY: "+dynamicQuery.toString());
        List<Consultant> searchResult = consultantDAL.findBySkillAndExperienceTime(dynamicQuery);
        LOGGER.info("RESULTS COUNT: "+searchResult.size());
        return searchResult;
    }

    private List<DynamicQueryParameter> createDynamicQuery(List<String> skillNames, List<String> experienceTimes) {
        List<DynamicQueryParameter> dynamicQuery = new ArrayList<>();

        for (int i = 0; i < skillNames.size(); i++) {
            DynamicQueryParameter queryParameter = DynamicQueryParameter.builder()
                .skillName(skillNames.get(i))
                .experienceTime(Integer.parseInt(experienceTimes.get(i)))
                .build();

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

    boolean isNullOrEmpty(List<String> array) {
        if (array == null) {
            return false;
        }
        return (array.isEmpty());
    }

}
