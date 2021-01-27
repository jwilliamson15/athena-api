package com.athena.controllers;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.models.ConsultantSkills;
import com.athena.models.Consultants;
import com.athena.repositories.ConsultantsRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConsultantsControllerTest {

    private ConsultantsRepository mockRepository;
    private ConsultantsController controller;

    private static Logger LOGGER = LoggerFactory.getLogger(ConsultantsControllerTest.class);

    @BeforeEach
    void setup() {
        mockRepository = mock(ConsultantsRepository.class);
        controller = new ConsultantsController(mockRepository);
    }

    @Test
    void searchForMultiSkillsDoesNotReturnDuplicates() {
        ConsultantSkills skillJava5 = new ConsultantSkills("Java", 5);
        ConsultantSkills skillJava10 = new ConsultantSkills("Java", 10);
        ConsultantSkills skillAws10 = new ConsultantSkills("aws", 10);

        List<ConsultantSkills> consultant1Skills = new ArrayList<>();
        consultant1Skills.add(skillJava5);
        List<ConsultantSkills> consultant2Skills = new ArrayList<>();
        consultant1Skills.add(skillJava10);
        consultant1Skills.add(skillAws10);

        ObjectId matchingResultId = new ObjectId();

        Consultants consultant1 = new Consultants(new ObjectId(), "Josh",
            "Software Engineer", "Just Josh", consultant1Skills,
            new ArrayList<>());
        Consultants consultant2 = new Consultants(matchingResultId, "David",
            "Software Engineer", "Just David", consultant2Skills,
            new ArrayList<>());
        Consultants consultant3 = new Consultants(matchingResultId, "David",
            "Software Engineer", "Just David", consultant2Skills,
            new ArrayList<>());
        List<Consultants> javaSearchResults = new ArrayList<>();
        javaSearchResults.add(consultant1);
        javaSearchResults.add(consultant2);
        List<Consultants> awsSearchResults = new ArrayList<>();
        awsSearchResults.add(consultant3);

        List<String> searchSkillNames = new ArrayList<>();
        searchSkillNames.add("java");
        searchSkillNames.add("aws");

        //given
        when(mockRepository.findBySingleSkill("java")).thenReturn(javaSearchResults);
        when(mockRepository.findBySingleSkill("aws")).thenReturn(awsSearchResults);

        //when
        List<Consultants> searchResults = controller.searchForMultiSkills(searchSkillNames);

        //then
        LOGGER.info(searchResults.toString());
        assertEquals(2, searchResults.size());
    }
}