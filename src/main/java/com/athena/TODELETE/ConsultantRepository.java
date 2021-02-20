package com.athena.TODELETE;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.athena.model.Consultant;

public interface ConsultantRepository extends MongoRepository<Consultant, String> {
    Consultant findBy_id(ObjectId _id);

    @Query("{'skills.name': { $regex: ?0, $options: 'i'}}")
    List<Consultant> findBySingleSkill(String skillName);
}
