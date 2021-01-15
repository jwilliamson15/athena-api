package com.athena.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

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

    //TODO - expand attributes... see ConsultantSkills
}
