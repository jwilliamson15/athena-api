package com.athena.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.models.ConsultantSkill;
import com.athena.models.Consultant;
import com.athena.repository.ConsultantRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdvancedQueriesTest {
    private ConsultantRepository mockRepository;
    private AdvancedQueries advancedQueries;

    private static Logger LOGGER = LoggerFactory.getLogger(AdvancedQueriesTest.class);

    @BeforeEach
    void setup() {
        mockRepository = mock(ConsultantRepository.class);
        advancedQueries = new AdvancedQueries(mockRepository);
    }

    @Test
    void searchForMultiSkillsDoesNotReturnDuplicates() {
        ConsultantSkill skillJava5 = new ConsultantSkill("Java", 5);
        ConsultantSkill skillJava10 = new ConsultantSkill("Java", 10);
        ConsultantSkill skillAws10 = new ConsultantSkill("aws", 10);

        List<ConsultantSkill> consultant1Skills = new ArrayList<>();
        consultant1Skills.add(skillJava5);
        List<ConsultantSkill> consultant2Skills = new ArrayList<>();
        consultant1Skills.add(skillJava10);
        consultant1Skills.add(skillAws10);

        ObjectId matchingResultId = new ObjectId();

        Consultant consultant1 = new Consultant(new ObjectId(), "Josh",
            "Software Engineer", "Just Josh", consultant1Skills,
            new ArrayList<>());
        Consultant consultant2 = new Consultant(matchingResultId, "David",
            "Software Engineer", "Just David", consultant2Skills,
            new ArrayList<>());
        Consultant consultant3 = new Consultant(matchingResultId, "David",
            "Software Engineer", "Just David", consultant2Skills,
            new ArrayList<>());
        List<Consultant> javaSearchResults = new ArrayList<>();
        javaSearchResults.add(consultant1);
        javaSearchResults.add(consultant2);
        List<Consultant> awsSearchResults = new ArrayList<>();
        awsSearchResults.add(consultant3);

        List<String> searchSkillNames = new ArrayList<>();
        searchSkillNames.add("java");
        searchSkillNames.add("aws");

        //given
        when(mockRepository.findBySingleSkill("java")).thenReturn(javaSearchResults);
        when(mockRepository.findBySingleSkill("aws")).thenReturn(awsSearchResults);

        //when
        List<Consultant> searchResults = advancedQueries.searchForMultiSkillsOr(searchSkillNames);

        //then
        LOGGER.info(searchResults.toString());
        assertEquals(2, searchResults.size());
    }
}