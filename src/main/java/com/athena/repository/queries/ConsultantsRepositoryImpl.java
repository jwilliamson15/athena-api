package com.athena.repository.queries;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.MongoRegexCreator;
import org.springframework.data.mongodb.core.query.Query;

import com.athena.models.Consultant;
import com.athena.repository.ConsultantsRepositoryCustom;

public class ConsultantsRepositoryImpl implements ConsultantsRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ConsultantsRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Consultant> query(DynamicQuery dynamicQuery) {
        final Query query = new Query();
        final List<Criteria> criteria = new ArrayList<>();

        if (dynamicQuery.getSkillsNameLike() != null) {
            criteria.add(Criteria.where("skills.name").regex(MongoRegexCreator.INSTANCE.toRegularExpression(
                dynamicQuery.getSkillsNameLike(), MongoRegexCreator.MatchMode.CONTAINING
            ), "i"));
        }

        if (dynamicQuery.getSkillsExperienceTimeGreaterThan() != null) {
            criteria.add(Criteria.where("skills.experienceTime").gte(dynamicQuery.getSkillsExperienceTimeGreaterThan()));
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        }

        return mongoTemplate.find(query, Consultant.class);
    }
}
