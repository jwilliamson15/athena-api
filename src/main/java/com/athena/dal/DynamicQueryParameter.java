package com.athena.dal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DynamicQueryParameter {
    private String skillName;
    private Integer experienceTime;
}
