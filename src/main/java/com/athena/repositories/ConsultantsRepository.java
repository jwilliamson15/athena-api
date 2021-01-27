package com.athena.repositories;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.athena.models.Consultants;

public interface ConsultantsRepository extends MongoRepository<Consultants, String> {
    Consultants findBy_id(ObjectId _id);

    @Query("{'skills.name': { $regex: ?0, $options: 'i'}}")
    List<Consultants> findBySingleSkill(String skillName);
}
