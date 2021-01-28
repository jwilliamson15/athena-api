package com.athena.controllers;

import java.util.List;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athena.models.Consultant;
import com.athena.repository.ConsultantRepository;
import com.athena.service.AdvancedQueries;

@RestController
@RequestMapping("/consultants")
public class ConsultantsController {
    @Autowired
    private ConsultantRepository repository;

    private AdvancedQueries advancedQueries = new AdvancedQueries(repository);

    private static Logger LOGGER = LoggerFactory.getLogger(ConsultantsController.class);

    public ConsultantsController(ConsultantRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Consultant> getAllConsultants() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Consultant getConsultantById(@PathVariable("id") ObjectId id) {
        return repository.findBy_id(id);
    }

    @Deprecated
    @RequestMapping(value = "/skills/{skillName}", method = RequestMethod.GET)
    public List<Consultant> getConsultantsBySkill(@PathVariable("skillName") String skillName) {
        return repository.findBySingleSkill(skillName);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<Consultant> getConsultantsBySkillParam(@RequestParam("skills") List<String> skillNames,
                                                       @RequestParam(name = "experience", required = false) List<String> experienceTimes) {
        LOGGER.warn("Skill names: {}", skillNames.toString());

        if (isNotNullOrEmpty(experienceTimes)) {
            return advancedQueries.searchWithExperienceQuery(skillNames, experienceTimes);
        }

        //if skillsNames > 1
        if (skillNames.size() > 1) {
            return advancedQueries.searchForMultiSkillsOr(skillNames);
        }

        return repository.findBySingleSkill(skillNames.get(0));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Consultant createConsultant(@Valid @RequestBody Consultant consultant) {
        consultant.set_id(ObjectId.get());
        repository.save(consultant);
        return consultant;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void modifyConsultantsById(@PathVariable("id") ObjectId id, @Valid @RequestBody Consultant consultant) {
        consultant.set_id(id);
        repository.insert(consultant);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteConsultant(@PathVariable ObjectId id) {
        repository.delete(repository.findBy_id(id));
    }

    boolean isNotNullOrEmpty(List<String> array) {
        if (array == null) {
            return false;
        }
        return (!array.isEmpty());
    }

}
