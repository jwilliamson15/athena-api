package com.athena.dal;

import java.util.HashMap;
import java.util.List;

import org.bson.types.ObjectId;

import com.athena.model.Consultant;

public interface ConsultantDAL {

    //create
    Consultant saveConsultant(Consultant consultant);

    //read
    List<Consultant> getAllConsultant();
    Consultant findById(ObjectId id);
    List<Consultant> findBySkill(String skillName);
    List<Consultant> findMultipleSkills(List<String> skillNames);
    List<Consultant> findBySkillAndExperienceTime(List<DynamicQueryParameter> dynamicQuery);

    //update
    Consultant updateConsultant(Consultant consultant);

    //delete
    void deleteConsultant(Consultant consultant);
}
