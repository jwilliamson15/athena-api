package com.athena.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.athena.models.Consultant;

public interface ConsultantRepository extends MongoRepository<Consultant, String>, ConsultantsRepositoryCustom {
    Consultant findBy_id(ObjectId _id);

    @Query("{'skills.name': { $regex: ?0, $options: 'i'}}")
    List<Consultant> findBySingleSkill(String skillName);
}
