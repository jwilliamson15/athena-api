package com.athena.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.athena.dal.ConsultantDALImpl;
import com.athena.model.Consultant;
import com.athena.model.ConsultantSkill;
import com.athena.model.EngagementHistory;
import com.athena.model.SkillLevel;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConsultantControllerTest {

    private ConsultantDALImpl mockDAL;
    private ConsultantsController controller;

    private static Logger LOGGER = LoggerFactory.getLogger(ConsultantControllerTest.class);

    @BeforeEach
    void setup() {
        mockDAL = mock(ConsultantDALImpl.class);
        controller = new ConsultantsController(mockDAL);
    }

    @Test
    void isNotNullOrEmptyHelperMethodTest () {
        //given
        List<String> emptyList = new ArrayList<>();
        List<String> populatedList = new ArrayList<>();
        populatedList.add("test");

        //when-then
        assertFalse(controller.isNotNullOrEmpty(null));
        assertFalse(controller.isNotNullOrEmpty(emptyList));
        assertTrue(controller.isNotNullOrEmpty(populatedList));
    }

    @Test
    void getAllConsultantsTest() {
        //given
        ObjectId id1 = new ObjectId();
        ObjectId id2 = new ObjectId();
        List<Consultant> mockResponse = Arrays.asList(createConsultant(id1), createConsultant(id2));
        when(mockDAL.getAllConsultant()).thenReturn(mockResponse);

        //when
        List<Consultant> result = controller.getAllConsultants();

        //then
        assertEquals(2, result.size());
        assertEquals(id1, result.get(0).get_id());
        assertEquals(id2, result.get(1).get_id());
    }

    @Test
    void getConsultantByIdTest() {
        //given
        ObjectId id = new ObjectId();
        Consultant expectedConsultant = createConsultant(id);
        when(mockDAL.findById(id)).thenReturn(expectedConsultant);

        //when
        Consultant result = controller.getConsultantById(id);

        //then
        assertEquals(expectedConsultant.get_id(), result.get_id());
    }

    @Test
    void getConsultantByEmployeeNumberTest() {
        //given
        Consultant expectedConsultant = createConsultant(new ObjectId());
        String empNumToSearch = expectedConsultant.getEmployeeNumber();
        when(mockDAL.findByEmployeeNumber(empNumToSearch)).thenReturn(expectedConsultant);

        //when
        Consultant result = controller.getConsultantByEmployeeNumber(empNumToSearch);

        //then
        assertEquals(expectedConsultant.get_id(), result.get_id());
        assertEquals(empNumToSearch, result.getEmployeeNumber());
    }

    @Test
    void createConsultantTest() {
        //given
        Consultant consultantToSave = createConsultantWithoutId();
        when(mockDAL.saveConsultant(consultantToSave)).thenReturn(consultantToSave);

        //when
        Consultant result = controller.createConsultant(consultantToSave);

        //then
        assertEquals(consultantToSave.getEmployeeNumber(), result.getEmployeeNumber());
    }

    @Test
    void createConsultantConflictTest() {
        //given
        Consultant consultantToSave = createConsultantWithoutId();
        when(mockDAL.saveConsultant(consultantToSave)).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Object already exists."));

        //when
        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            controller.createConsultant(consultantToSave);
        });

        //then
        assertEquals(ResponseStatusException.class, exception.getClass());
        assertEquals(HttpStatus.CONFLICT, ((ResponseStatusException) exception).getStatus());
    }

    @Test
    void modifyConsultantByEmployeeNumberTest() {
        //given
        ObjectId id = new ObjectId();
        Consultant consultantToSave = createConsultant(id);
        String empNum = consultantToSave.getEmployeeNumber();
        when(mockDAL.findByEmployeeNumber(empNum)).thenReturn(consultantToSave);
        when(mockDAL.updateConsultant(consultantToSave)).thenReturn(consultantToSave);

        //when
        controller.modifyConsultantsByEmployeeNumber(empNum, consultantToSave);

        //then
        verify(mockDAL, times(1)).findByEmployeeNumber(empNum);
        verify(mockDAL, times(1)).updateConsultant(consultantToSave);
    }

    @Test
    void deleteConsultantTest() {
        //given
        Consultant consultantToDelete = createConsultant(new ObjectId());
        String empNum = consultantToDelete.getEmployeeNumber();
        when(mockDAL.findByEmployeeNumber(empNum)).thenReturn(consultantToDelete);

        //when
        controller.deleteConsultant(empNum);

        //then
        verify(mockDAL, times(1)).findByEmployeeNumber(empNum);
        verify(mockDAL, times(1)).deleteConsultant(consultantToDelete);
    }

    @Test
    void getConsultantBySkillTest() {
        //given
        List<String> skillNames = Arrays.asList("skill1", "skill2");
        List<String> experienceTimes = Arrays.asList("1","1");
        List<String> skillLevels = Arrays.asList(SkillLevel.BASELINE.toString(), SkillLevel.BASELINE.toString());
        ObjectId id1 = new ObjectId();
        ObjectId id2 = new ObjectId();
        List<Consultant> expectedConsultants = Arrays.asList(createConsultant(id1), createConsultant(id2));

        when(mockDAL.findByDynamicQuery(any())).thenReturn(expectedConsultants);

        //when
        List<Consultant> result = controller.getConsultantsBySkill(skillNames, experienceTimes, skillLevels);

        //then
        assertEquals(id1, result.get(0).get_id());
        assertEquals(id2, result.get(1).get_id());
    }

    private Consultant createConsultant(ObjectId id) {
        Consultant consultantToReturn = new Consultant(
            id,
            "Test Consultant",
            "123",
            "Test job role",
            "This person is a test",
            Arrays.asList(new ConsultantSkill("Test Skill", 1, SkillLevel.PROGRESSING)),
            Arrays.asList(new EngagementHistory("Test Engagement", "Test engagament description", 1))
        );

        return consultantToReturn;
    }

    private Consultant createConsultantWithoutId() {
        Consultant consultantToReturn = new Consultant();
        consultantToReturn.setName("Test Consultant");
        consultantToReturn.setEmployeeNumber("123");
        consultantToReturn.setJobRole("Test job role");
        consultantToReturn.setPersonDescription("This person is a test");
        consultantToReturn.setSkills(Arrays.asList(new ConsultantSkill("Test Skill", 1, SkillLevel.PROGRESSING)));
        consultantToReturn.setEngagementHistory(Arrays.asList(new EngagementHistory("Test Engagement", "Test engagament description", 1)));

        return consultantToReturn;
    }
}