package com.athena.repository;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;

import com.athena.models.Consultant;
import com.athena.repository.queries.DynamicQuery;
import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {ConsultantRepository.class})
@EnableMongoRepositories
@Import(EmbeddedMongoAutoConfiguration.class)
class ConsultantRepositoryTest {
    private ConsultantRepository consultantRepository;

    @Test
    public void combinedQuery_shouldReturnList() {
        //given
        final String skillNameToSearch = "java";
        final int experienceTimeToSearch = 7;
        final DynamicQuery dynamicQuery = new DynamicQuery();
        dynamicQuery.setSkillsNameLike(skillNameToSearch);
        dynamicQuery.setSkillsExperienceTimeGreaterThan(experienceTimeToSearch);

        //when
        final List<Consultant> consultants = consultantRepository.query(dynamicQuery);

        //then
        assertEquals(1, consultants.size());
    }

}