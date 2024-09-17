package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.QueryResult;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserDetectionDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.ConfigRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.detection.DetectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DetectionServiceImplementationTest {
    @Autowired
    DetectionService detectionService;
    @MockBean
    private ConfigRepository configRepository;
    private final Configuration mockConfiguration = new Configuration
            ("{      \"configuration\": [          {              \"antiPattern\": \"TooLongSprint\",              \"thresholds\": [                  {                      \"thresholdName\": \"maxIterationLength\",                      \"value\": \"21\"                  },                  {                      \"thresholdName\": \"maxNumberOfTooLongIterations\",                      \"value\": \"0\"                  }              ]          },          {              \"antiPattern\": \"VaryingSprintLength\",              \"thresholds\": [                  {                      \"thresholdName\": \"maxDaysDifference\",                      \"value\": \"7\"                  },                  {                      \"thresholdName\": \"maxIterationChanged\",                      \"value\": \"1\"                  }              ]          },          {              \"antiPattern\": \"BusinessAsUsual\",              \"thresholds\": [                  {                      \"thresholdName\": \"divisionOfIterationsWithRetrospective\",                      \"value\": \"66.66f\"                  },                  {                      \"thresholdName\": \"searchSubstringsWithRetrospective\",                      \"value\": \"%retr%||%revi%||%week%scrum%\"                  }              ]          },          {              \"antiPattern\": \"SpecifyNothing\",              \"thresholds\": [                  {                      \"thresholdName\": \"minNumberOfWikiPagesWithSpecification\",                      \"value\": \"1\"                  },                  {                      \"thresholdName\": \"minNumberOfActivitiesWithSpecification\",                      \"value\": \"1\"                  },                  {                      \"thresholdName\": \"minAvgLengthOfActivityDescription\",                      \"value\": \"150\"                  },                  {                      \"thresholdName\": \"searchSubstringsWithProjectSpecification\",                      \"value\": \"%dsp%||%specifikace%||%specification%||%vize%proj%||%vize%produ%\"                  }              ]          },          {              \"antiPattern\": \"RoadToNowhere\",              \"thresholds\": [                  {                      \"thresholdName\": \"minNumberOfWikiPagesWithProjectPlan\",                      \"value\": \"1\"                  },                  {                      \"thresholdName\": \"minNumberOfActivitiesWithProjectPlan\",                      \"value\": \"1\"                  },                  {                      \"thresholdName\": \"searchSubstringsWithProjectPlan\",                      \"value\": \"%plán projektu%||%project plan%||%plan project%||%projektový plán%\"                  }              ]          },          {              \"antiPattern\": \"LongOrNonExistentFeedbackLoops\",              \"thresholds\": [                  {                      \"thresholdName\": \"divisionOfIterationsWithFeedbackLoop\",                      \"value\": \"50.00f\"                  },                  {                      \"thresholdName\": \"maxGapBetweenFeedbackLoopRate\",                      \"value\": \"2f\"                  },                  {                      \"thresholdName\": \"searchSubstringsWithFeedbackLoop\",                      \"value\": \"%schůz%zákazník%||%předvedení%zákazník%||%zákazn%demo%||%schůz%zadavat%||%inform%schůz%||%zákazn%||%zadavatel%\"                  }              ]          },          {              \"antiPattern\": \"NinetyNinetyRule\",              \"thresholds\": [                  {                      \"thresholdName\": \"maxDivisionRange\",                      \"value\": \"1.25f\"                  },                  {                      \"thresholdName\": \"maxBadDivisionLimit\",                      \"value\": \"2\"                  }              ]          },          {              \"antiPattern\": \"UnknownPoster\",              \"thresholds\": [                  {                      \"thresholdName\": \"searchSubstringsInvalidNames\",                      \"value\": \"%unknown%||%anonym%\"                  }              ]          },          {              \"antiPattern\": \"BystanderApathy\",              \"thresholds\": [                  {                      \"thresholdName\": \"searchSubstringsInvalidContributors\",                      \"value\": \"%dependabot%\"                  },                  {                      \"thresholdName\": \"maximumPercentageOfTasksWithoutTeamwork\",                      \"value\": \"30f\"                  }              ]          },          {              \"antiPattern\": \"YetAnotherProgrammer\",              \"thresholds\": [                  {                      \"thresholdName\": \"maxNumberOfNewContributors\",                      \"value\": \"5\"                  },                  {                      \"thresholdName\": \"numberOfFirstMonthsWithoutDetection\",                      \"value\": \"2\"                  }              ]          }      ]  }",
                    "N","mockName",null);

    //no projects provided in request - incorrect request
    @Test
    public void invalidProjectParameterDetection() {
        final UserDetectionDto userDetectionDtoMockInvalid = new UserDetectionDto("foo",1,new String[]{},new String[]{"1"});
        //request is invalid

        when(configRepository.findConfigurationById(1)).thenReturn(mockConfiguration);
        List<QueryResult> results = detectionService.analyze(userDetectionDtoMockInvalid);
        assertEquals(0,results.size());
    }
    //no antipatterns provided in request - also incorrect
    @Test
    public void invalidAntipatternParameterDetection() {
        final UserDetectionDto userDetectionDtoMockInvalid = new UserDetectionDto("foo",1,new String[]{"1"},new String[]{});
        //request is invalid
        when(configRepository.findConfigurationById(1)).thenReturn(mockConfiguration);
        List<QueryResult> results = detectionService.analyze(userDetectionDtoMockInvalid);
        assertEquals(0,results.size());
    }

}
