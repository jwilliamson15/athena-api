package com.athena.models;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Consultants {

    @Id
    public ObjectId _id;

    private String name;
    private String jobRole;
    private String personDescription;

    private List<ConsultantSkills> skills;
    private List<EngagementHistory> engagementHistory;

    @Override
    public String toString() {
        return "(ID: " + _id +" NAME: " + name+")";
    }
}
