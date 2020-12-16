package com.athena.repositories;

import com.athena.models.Consultants;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsultantsRepository extends MongoRepository<Consultants, String> {
    Consultants findBy_id(ObjectId _id);
}
