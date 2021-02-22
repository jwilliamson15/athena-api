package com.athena.dal;

import com.athena.model.SkillLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DynamicQueryParameter {
    private String skillName;
    private Integer experienceTime;
    private SkillLevel skillLevel;
}
