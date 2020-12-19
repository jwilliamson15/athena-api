package com.athena.controllers;

import com.athena.models.Consultants;
import com.athena.repositories.ConsultantsRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/consultants")
public class ConsultantsController {
    @Autowired
    private ConsultantsRepository repository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Consultants> getAllConsultants() {
        return repository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Consultants getConsultantById(@PathVariable("id") ObjectId id) {
        return repository.findBy_id(id);
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

}
