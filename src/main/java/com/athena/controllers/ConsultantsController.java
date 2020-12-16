package com.athena.controllers;

import com.athena.models.Consultants;
import com.athena.repositories.ConsultantsRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}
