package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.activity;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.controller.management.SegmentActivityController;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Activity;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Category;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.ProjectInstance;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.WuType;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.DatabaseObject;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.ActivityRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.WorkUnitRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.CategoryService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.WorkUnitService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums.WuTypeService;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

import org.springframework.stereotype.Service;

@Service
public class ActivityServiceImpl implements ActivityService {
    private final Logger LOGGER = LoggerFactory.getLogger(SegmentActivityController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private WorkUnitService workUnitService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WuTypeService wuTypeService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private WorkUnitRepository workUnitRepository;

    @Override
    public List<Activity> fetchAllProjectActivities(long projectId) {
        Project p = this.projectService.getProjectById(projectId);
        if(p == null){
            return null;
        }
        List<Activity> activities = p.getActivities();
        return activities;
    }

    @Override
    public DatabaseObject saveActivity(Activity activity) {
        return activityRepository.save(activity);
    }
}
