package com.athena.dal;

import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.athena.exception.ConflictException;
import com.athena.model.Consultant;
import com.athena.model.SkillLevel;

public interface ConsultantDAL {

    //create
    Consultant saveConsultant(Consultant consultant) throws ConflictException;

    //read
    List<Consultant> getAllConsultant();
    Consultant findById(ObjectId id);
    Consultant findByEmployeeNumber(String employeeNumber);
    List<Consultant> findByDynamicQuery(List<DynamicQueryParameter> dynamicQuery);

    //update
    Consultant updateConsultant(Consultant consultant);

    //delete
    void deleteConsultant(Consultant consultant);
}
