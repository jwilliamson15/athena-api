package com.athena.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConsultantSkills {

    //enum ExperienceLevel {BASELINE,PROGRESSING,EXPERT,GENIUS}

    private String name;
    public int experienceTime;
    //public ExperienceLevel skillExperienceLevel;

    //TODO - enum for experience level
}
