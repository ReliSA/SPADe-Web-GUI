package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.ProjectInstance;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.ProjectInstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ProjectInstanceServiceImpl implements ProjectInstanceService {

    @Autowired
    private ProjectInstanceRepository projectInstanceRepository;

    @Override
    public List<ProjectInstance> getAllProjectInstances() {
        List<ProjectInstance> instances = projectInstanceRepository.findAll();
        Collections.sort(instances, Comparator.comparing(ProjectInstance::getName, String.CASE_INSENSITIVE_ORDER));
        return instances;
    }
}
