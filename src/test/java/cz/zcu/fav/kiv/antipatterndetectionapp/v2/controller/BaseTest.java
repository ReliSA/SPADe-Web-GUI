package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller;

import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.AntiPatternManager;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.AntiPatternService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectServiceImpl;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Configuration;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Metadata;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.User;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.UserConfigurationJoin;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.AboutPageRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.ConfigRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository.UserRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.JSONBuilder;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.RequestBuilder;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.ProjectToDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest()
public abstract class BaseTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RequestBuilder requestBuilder;

    @MockBean
    private AntiPatternService antiPatternService;

    @MockBean
    private ProjectServiceImpl projectService;

    @MockBean
    private ConfigRepository configurationRepository;

    @MockBean
    private AboutPageRepository aboutPageRepository;

    @MockBean
    private AntiPatternManager antiPatternManager;

    @BeforeEach
    public void init() {

        setupUserControllerMocks();
        setupFilterMocks();
        setupConfigurationControllerMocks();
        setupAboutControllerMocks();
        setupDetectionControllerMocks();

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
    }

    private void setupUserControllerMocks() {
        User user = new User("foo", "foo@foo.foo", "76D3BC41C9F588F7FCD0D5BF4718F8F84B1C41B20882703100B9EB9413807C01");
        User userWrongPassword = new User("foo", "foo@foo.foo", "foo");
        ResponseEntity<String> loginResponse = ResponseEntity.ok("token");

        /* Registration of user */
        when(userRepository.findByName("new_user")).thenReturn(null);
        when(userRepository.findByName("existing")).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        /* Login of user */
        when(userRepository.findByName("existing")).thenReturn(user);
        when(requestBuilder.sendRequestResponse(eq("http://localhost:8081/login"), anyMap())).thenReturn(loginResponse);
        when(userRepository.findByName("non_existing")).thenReturn(null);
        when(userRepository.findByName("foo")).thenReturn(userWrongPassword);

        /* Logout of user */
        Map<String,Object> logoutJson = new HashMap<>();
        logoutJson.put("message", "User logged out");
        String logoutBody = JSONBuilder.buildJSON(logoutJson);
        ResponseEntity<String> logoutResponse = ResponseEntity.ok(logoutBody);
        when(requestBuilder.sendRequestResponse(eq("http://localhost:8081/logout"), anyMap())).thenReturn(logoutResponse);
    }

    private void setupFilterMocks() {
        /* Filter mock */
        Map<String,Object> filterJson = new HashMap<>();
        filterJson.put("message", "User authorized");
        String filterBody = JSONBuilder.buildJSON(filterJson);
        ResponseEntity<String> oauth = ResponseEntity.ok(filterBody);
        when(requestBuilder.sendRequestResponse(anyString(), eq("token"))).thenReturn(oauth);

        Map<String,Object> filterJson2 = new HashMap<>();
        filterJson2.put("message", "User unauthorized");
        String filterBody2 = JSONBuilder.buildJSON(filterJson2);
        ResponseEntity<String> oauth2 = ResponseEntity.status(500).body(filterBody2);
        when(requestBuilder.sendRequestResponse(anyString(), eq("wrong"))).thenReturn(oauth2);
    }

    private void setupConfigurationControllerMocks() {
        /* /configuration_name */
        User config_user = new User("config_user");
        config_user.setId(1);
        when(userRepository.findByName("config_user")).thenReturn(config_user);
        List<Object[]> configs = new ArrayList<>();
        Object[] pom_pole = new Object[2];
        pom_pole[0] = "default config";
        pom_pole[1] = 1;
        configs.add(pom_pole);
        when(configurationRepository.getAllUserConfigurationNames(config_user.getId())).thenReturn(configs);

        /* /configuration */
        List<Object[]> configuration = new ArrayList<>();
        Configuration con = new Configuration("{\r\n    \"configuration\": [\r\n        {\r\n            \"antiPattern\": \"TooLongSprint\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"maxIterationLength\",\r\n                    \"value\": \"21\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maxNumberOfTooLongIterations\",\r\n                    \"value\": \"0\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"VaryingSprintLength\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"maxDaysDifference\",\r\n                    \"value\": \"7\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maxIterationChanged\",\r\n                    \"value\": \"1\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"BusinessAsUsual\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"divisionOfIterationsWithRetrospective\",\r\n                    \"value\": \"66.66f\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsWithRetrospective\",\r\n                    \"value\": \"%retr%||%revi%||%week%scrum%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"SpecifyNothing\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"minNumberOfWikiPagesWithSpecification\",\r\n                    \"value\": \"1\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"minNumberOfActivitiesWithSpecification\",\r\n                    \"value\": \"1\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"minAvgLengthOfActivityDescription\",\r\n                    \"value\": \"150\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsWithProjectSpecification\",\r\n                    \"value\": \"%dsp%||%specifikace%||%specification%||%vize%proj%||%vize%produ%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"RoadToNowhere\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"minNumberOfWikiPagesWithProjectPlan\",\r\n                    \"value\": \"1\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"minNumberOfActivitiesWithProjectPlan\",\r\n                    \"value\": \"1\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsWithProjectPlan\",\r\n                    \"value\": \"%pl?n projektu%||%project plan%||%plan project%||%projektov? pl?n%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"LongOrNonExistentFeedbackLoops\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"divisionOfIterationsWithFeedbackLoop\",\r\n                    \"value\": \"50.00f\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maxGapBetweenFeedbackLoopRate\",\r\n                    \"value\": \"2f\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsWithFeedbackLoop\",\r\n                    \"value\": \"%sch?z%z?kazn?k%||%p?edveden?%z?kazn?k%||%z?kazn%demo%||%sch?z%zadavat%||%inform%sch?z%||%z?kazn%||%zadavatel%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"NinetyNinetyRule\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"maxDivisionRange\",\r\n                    \"value\": \"1.25f\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maxBadDivisionLimit\",\r\n                    \"value\": \"2\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"UnknownPoster\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsInvalidNames\",\r\n                    \"value\": \"%unknown%||%anonym%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"BystanderApathy\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsInvalidContributors\",\r\n                    \"value\": \"%dependabot%\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maximumPercentageOfTasksWithoutTeamwork\",\r\n                    \"value\": \"30f\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"YetAnotherProgrammer\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"maxNumberOfNewContributors\",\r\n                    \"value\": \"5\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"numberOfFirstMonthsWithoutDetection\",\r\n                    \"value\": \"2\"\r\n                }\r\n            ]\r\n        }\r\n    ]\r\n}"
                ,null,  "Y", "default config", "default config");
        Object[] pom = new Object[2];
        pom[0] = con;
        pom[1] = new UserConfigurationJoin(null, "default config");
        configuration.add(pom);
        when(configurationRepository.getAllUserConfigurations(config_user.getId())).thenReturn(configuration);
    }

    private void setupAboutControllerMocks() {
        List<Metadata> metadata = new ArrayList<>();
        Metadata met = new Metadata();
        met.setAppDataValue("[ { \"version\": \"1.0.0\", \"authors\": [ { \"name\": \"Ondrej Vane\", \"email\": \"vaneo@students.zcu.cz\"}], \"description\": \"This application is used to detect presence of anti-patterns in project management tools data. Seven selected anti-patterns are implemented in this application.\"}, { \"version\": \"1.1.0\", \"authors\": [ { \"name\": \"Ondrej Vane\", \"email\": \"vaneo@students.zcu.cz\"}], \"description\": \"This application is used to detect presence of anti-patterns in project management tools data. Seven selected anti-patterns are implemented in this application.\"}, { \"version\": \"1.2.0\", \"authors\": [ { \"name\": \"Petr Stepanek\", \"email\": \"petrs1@students.zcu.cz\"}], \"description\": \"This application is used to detect presence of anti-patterns in project management tools data. Ten selected anti-patterns are implemented in this application.\"}, { \"version\": \"2.0.0\", \"authors\": [ { \"name\": \"Petr Stepanek\", \"email\": \"petrs1@students.zcu.cz\"}, { \"name\": \"Petr Urban\", \"email\": \"urbanp@students.zcu.cz\"}, { \"name\": \"Jiri Trefil\", \"email\": \"trefil@students.zcu.cz\"}, { \"name\": \"Vaclav Hrabik\", \"email\": \"hrabikv@students.zcu.cz\"}], \"description\": \"TODO\"}]");
        met.setId(1L);
        met.setAppDataKey("basics");
        metadata.add(met);
        when(aboutPageRepository.findAll()).thenReturn(metadata);
    }

    private void setupDetectionControllerMocks() {
        /* Detection */
        /* /list */
        Map<String, Threshold> thresholdMap1 = new HashMap<>();
        thresholdMap1.put("divisionOfIterationsWithRetrospective", new Threshold(
                "divisionOfIterationsWithRetrospective",
                "Division of iterations with retrospective",
                "Minimum percentage of the total number of iterations with a retrospective (0,100)",
                "Percentage must be float number between 0 and 100",
                "Percentage"
        ));
        thresholdMap1.put("searchSubstringsWithRetrospective", new Threshold(
                "searchSubstringsWithRetrospective",
                "Search substrings with retrospective",
                "Substring that will be search in wikipages and activities",
                "Maximum number of substrings is ten and must not starts and ends with characters || ",
                "String"
        ));
        List<AntiPattern> antiPatterns = new ArrayList<>();
        antiPatterns.add(new AntiPattern(3L,
                "Business As Usual",
                "BusinessAsUsual",
                "Absence of a retrospective after individual iterations or after the completion project.",
                thresholdMap1,
                "Business_As_Usual.md"
        ));
        when(antiPatternService.getAllAntiPatterns()).thenReturn(null);
        when(antiPatternService.antiPatternsToModel(any())).thenReturn(antiPatterns);

        List<Project> projectList = new ArrayList<>();
        Project project1 = new Project("Aplikace nad otevrenymi daty (KIV) BHVS", "generic description in czech");
        project1.setId(6L);
        projectList.add(project1);
        when(projectService.getAllProjects()).thenReturn(projectList);

        /* /analyze */
        Configuration con = new Configuration("{\r\n    \"configuration\": [\r\n        {\r\n            \"antiPattern\": \"TooLongSprint\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"maxIterationLength\",\r\n                    \"value\": \"21\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maxNumberOfTooLongIterations\",\r\n                    \"value\": \"0\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"VaryingSprintLength\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"maxDaysDifference\",\r\n                    \"value\": \"7\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maxIterationChanged\",\r\n                    \"value\": \"1\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"BusinessAsUsual\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"divisionOfIterationsWithRetrospective\",\r\n                    \"value\": \"66.66f\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsWithRetrospective\",\r\n                    \"value\": \"%retr%||%revi%||%week%scrum%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"SpecifyNothing\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"minNumberOfWikiPagesWithSpecification\",\r\n                    \"value\": \"1\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"minNumberOfActivitiesWithSpecification\",\r\n                    \"value\": \"1\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"minAvgLengthOfActivityDescription\",\r\n                    \"value\": \"150\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsWithProjectSpecification\",\r\n                    \"value\": \"%dsp%||%specifikace%||%specification%||%vize%proj%||%vize%produ%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"RoadToNowhere\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"minNumberOfWikiPagesWithProjectPlan\",\r\n                    \"value\": \"1\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"minNumberOfActivitiesWithProjectPlan\",\r\n                    \"value\": \"1\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsWithProjectPlan\",\r\n                    \"value\": \"%pl?n projektu%||%project plan%||%plan project%||%projektov? pl?n%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"LongOrNonExistentFeedbackLoops\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"divisionOfIterationsWithFeedbackLoop\",\r\n                    \"value\": \"50.00f\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maxGapBetweenFeedbackLoopRate\",\r\n                    \"value\": \"2f\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsWithFeedbackLoop\",\r\n                    \"value\": \"%sch?z%z?kazn?k%||%p?edveden?%z?kazn?k%||%z?kazn%demo%||%sch?z%zadavat%||%inform%sch?z%||%z?kazn%||%zadavatel%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"NinetyNinetyRule\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"maxDivisionRange\",\r\n                    \"value\": \"1.25f\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maxBadDivisionLimit\",\r\n                    \"value\": \"2\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"UnknownPoster\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsInvalidNames\",\r\n                    \"value\": \"%unknown%||%anonym%\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"BystanderApathy\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"searchSubstringsInvalidContributors\",\r\n                    \"value\": \"%dependabot%\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"maximumPercentageOfTasksWithoutTeamwork\",\r\n                    \"value\": \"30f\"\r\n                }\r\n            ]\r\n        },\r\n        {\r\n            \"antiPattern\": \"YetAnotherProgrammer\",\r\n            \"thresholds\": [\r\n                {\r\n                    \"thresholdName\": \"maxNumberOfNewContributors\",\r\n                    \"value\": \"5\"\r\n                },\r\n                {\r\n                    \"thresholdName\": \"numberOfFirstMonthsWithoutDetection\",\r\n                    \"value\": \"2\"\r\n                }\r\n            ]\r\n        }\r\n    ]\r\n}"
                , null, "Y", "default config", "default config");
        String[] selectedProjects = new String[1];
        selectedProjects[0] = "1";
        String[] selectedAntiPatterns = new String[1];
        selectedAntiPatterns[0] = "3";
        Map<String, Map<String,String>> currentConfiguration = JSONBuilder.createMapFromString(con.getConfig());
        when(configurationRepository.findConfigurationById(1)).thenReturn(con);
        Project project = new Project("Aplikace pro Centrum blizkovychodnich studii (FF) Medici", "");
        project.setId(1L);
        List<QueryResult> results = new ArrayList<>();
            List<QueryResultItem> queryResultItemList = new ArrayList<>();
                List<ResultDetail> resultDetailsList = new ArrayList<>();
                    resultDetailsList.add(new ResultDetail("Min retrospective limit", "3"));
                    resultDetailsList.add(new ResultDetail("Found retrospectives", "4"));
                    resultDetailsList.add(new ResultDetail("Total number of iterations", "5"));
                    resultDetailsList.add(new ResultDetail("Conclusion", "There is right number of retrospectives"));
            queryResultItemList.add(new QueryResultItem(antiPatterns.get(0), false, resultDetailsList));
        ProjectToDto projectToDto = new ProjectToDto();
        results.add(new QueryResult(projectToDto.convert(project), queryResultItemList));
        when(antiPatternManager.analyze(selectedProjects, selectedAntiPatterns, currentConfiguration)).thenReturn(results);
    }
}
