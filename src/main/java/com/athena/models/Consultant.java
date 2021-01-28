package com.athena.models;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "consultants")
public class Consultant {

    @Id
    public ObjectId _id;

    private String name;
    private String jobRole;
    private String personDescription;

    private List<ConsultantSkill> skills;
    private List<EngagementHistory> engagementHistory;

    @Override
    public String toString() {
        return "(ID: " + _id +" NAME: " + name+")";
    }
}
