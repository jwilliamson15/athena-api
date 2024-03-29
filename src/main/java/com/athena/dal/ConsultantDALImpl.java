package com.athena.dal;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import static com.athena.Constants.REGEX_CASE_INSENSITIVE_OPTION;
import com.athena.exception.ConflictException;
import com.athena.model.Consultant;
import com.athena.model.SkillLevel;

@Repository
public class ConsultantDALImpl implements ConsultantDAL {

    private final MongoTemplate mongoTemplate;

    private static Logger LOGGER = LoggerFactory.getLogger(ConsultantDALImpl.class);

    @Autowired
    public ConsultantDALImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Consultant saveConsultant(Consultant consultant) throws ConflictException {
        Consultant existingConsultant = findByEmployeeNumber(consultant.getEmployeeNumber());

        if (existingConsultant != null) {
            throw new ConflictException();
        }

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
    public Consultant findByEmployeeNumber(String employeeNumber) {
        Query query = new Query();
        query.addCriteria(Criteria.where("employeeNumber").is(employeeNumber));

        return mongoTemplate.findOne(query, Consultant.class);
    }

    @Override
    public List<Consultant> findByDynamicQuery(List<DynamicQueryParameter> dynamicQuery) {
        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();

        for(DynamicQueryParameter queryParameter: dynamicQuery) {
            List<Criteria> skillCriteria = new ArrayList<>();
            if (queryParameter.getSkillName() != null) {
                final String skillName = queryParameter.getSkillName();
                skillCriteria.add(Criteria.where("skills.name").regex(skillName, REGEX_CASE_INSENSITIVE_OPTION));
            }

            if (queryParameter.getExperienceTime() != null) {
                final Integer experienceTime = queryParameter.getExperienceTime();
                skillCriteria.add(Criteria.where("skills.experienceTime").gte(experienceTime));
            }

            if (queryParameter.getSkillLevel() != null) {
                final SkillLevel skillLevel = queryParameter.getSkillLevel();
                skillCriteria.add(Criteria.where("skills.skillLevel").is(skillLevel));
            }

            criteria.add(new Criteria().andOperator(skillCriteria.toArray(new Criteria[skillCriteria.size()])));
        }

        query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
        LOGGER.info("DYNAMIC QUERY: " + query.toString());

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
