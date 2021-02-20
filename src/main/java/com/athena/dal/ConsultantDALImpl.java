package com.athena.dal;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static com.athena.Constants.REGEX_CASE_INSENSITIVE_OPTION;

import com.athena.model.Consultant;

@Repository
public class ConsultantDALImpl implements ConsultantDAL {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ConsultantDALImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Consultant saveConsultant(Consultant consultant) {
        mongoTemplate.save(consultant);
        return consultant;
    }

    @Override
    public List<Consultant> getAllConsultant() {
        return mongoTemplate.findAll(Consultant.class);
    }

    @Override
    public Consultant findById(ObjectId id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        return mongoTemplate.findOne(query, Consultant.class);
    }

    @Override
    public List<Consultant> findBySkill(String skillName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("skills.name").regex(skillName, REGEX_CASE_INSENSITIVE_OPTION));

        return mongoTemplate.find(query, Consultant.class);
    }

    @Override
    public List<Consultant> findMultipleSkills(List<String> skillNames) {
        Query query = new Query();

        List<Criteria> criteria = new ArrayList<>();
        for(String skillName: skillNames) {
            criteria.add(Criteria.where("skills.name").regex(skillName, REGEX_CASE_INSENSITIVE_OPTION));
        }
        query.addCriteria(new Criteria().orOperator(criteria.toArray(new Criteria[criteria.size()])));

        return mongoTemplate.find(query, Consultant.class);
    }

    @Override
    public Consultant updateConsultant(Consultant consultant) {
        mongoTemplate.save(consultant);
        return consultant;
    }

    @Override
    public void deleteConsultant(Consultant consultant) {
        mongoTemplate.remove(consultant);
    }
}
