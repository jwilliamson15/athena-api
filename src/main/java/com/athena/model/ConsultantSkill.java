package com.athena.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ConsultantSkill {

    private String name;
    private int experienceTime;
    private SkillLevel skillLevel;
}
