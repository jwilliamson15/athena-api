package com.athena.TODELETE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.collections.impl.block.factory.HashingStrategies;
import org.eclipse.collections.impl.utility.ListIterate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.model.Consultant;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AdvancedQueries {
    private ConsultantRepository repository;

    private static Logger LOGGER = LoggerFactory.getLogger(AdvancedQueries.class);

    public List<Consultant> searchForMultiSkillsOr(List<String> skillNames) {
        List<Consultant> searchResults = new ArrayList<>();

        for (String skillName: skillNames) {
            searchResults.addAll(repository.findBySingleSkill(skillName));
        }

        return ListIterate.distinct(
            searchResults, HashingStrategies.fromFunction(Consultant::get_id));
    }

    public List<Consultant> searchWithExperienceQuery(List<String> skillNames, List<String> experienceTimes) {
        LOGGER.warn("experience nums: {}", experienceTimes.toString());

        HashMap<String, Integer> skillsAndExperienceMap = new HashMap<String, Integer>();

        for (int i = 0; i < skillNames.size(); i++) {
            skillsAndExperienceMap.put(skillNames.get(i), Integer.parseInt(experienceTimes.get(i)));
        }

        LOGGER.warn("SKILLS MAP: {}", skillsAndExperienceMap.toString());

        //TODO - advanced query shit
        return null;
    }
}
