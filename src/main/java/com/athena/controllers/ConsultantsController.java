package com.athena.controllers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.eclipse.collections.impl.block.factory.HashingStrategies;
import org.eclipse.collections.impl.utility.ListIterate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athena.models.Consultants;
import com.athena.repositories.ConsultantsRepository;

@RestController
@RequestMapping("/consultants")
public class ConsultantsController {
    @Autowired
    private ConsultantsRepository repository;

    private static Logger LOGGER = LoggerFactory.getLogger(ConsultantsController.class);

    public ConsultantsController(ConsultantsRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Consultants> getAllConsultants() {
        return repository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Consultants getConsultantById(@PathVariable("id") ObjectId id) {
        return repository.findBy_id(id);
    }

    @Deprecated
    @RequestMapping(value = "/skills/{skillName}", method = RequestMethod.GET)
    public List<Consultants> getConsultantsBySkill(@PathVariable("skillName") String skillName) {
        return repository.findBySingleSkill(skillName);
    }

    @RequestMapping(value = "/skills", method = RequestMethod.GET)
    public List<Consultants> getConsultantsBySkillParam(@RequestParam("skills") List<String> skillNames) {
        LOGGER.warn("Skill names: {}", skillNames.toString());

        //if skillsExperienceTime != null then advanced dynamic query for skills and experience

        //if skillsNames > 1
        if (skillNames.size() > 1) {
            return searchForMultiSkills(skillNames);
        }

        return repository.findBySingleSkill(skillNames.get(0));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Consultants createConsultant(@Valid @RequestBody Consultants consultants) {
        consultants.set_id(ObjectId.get());
        repository.save(consultants);
        return consultants;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void modifyConsultantsById(@PathVariable("id") ObjectId id, @Valid @RequestBody Consultants consultants) {
        consultants.set_id(id);
        repository.save(consultants);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteConsultant(@PathVariable ObjectId id) {
        repository.delete(repository.findBy_id(id));
    }

    List<Consultants> searchForMultiSkills(List<String> skillNames) {
        List<Consultants> searchResults = new ArrayList<>();

        for (String skillName: skillNames) {
            searchResults.addAll(repository.findBySingleSkill(skillName));
        }

        return ListIterate.distinct(
            searchResults, HashingStrategies.fromFunction(Consultants::get_id));
    }

}
