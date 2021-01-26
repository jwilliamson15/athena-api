package com.athena.repositories;

import java.util.List;

import com.athena.models.ConsultantSkills;
import com.athena.models.Consultants;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ConsultantsRepository extends MongoRepository<Consultants, String> {
    Consultants findBy_id(ObjectId _id);

    @Query("{'skills.name': { $regex: ?0, $options: 'i'}}")
    List<Consultants> findBySkill(String skillName);
}
