package com.athena.repository.queries;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DynamicQuery {
    private String skillsNameLike;
    private Integer skillsExperienceTimeGreaterThan;
}
