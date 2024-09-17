package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.WorkUnit;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.WorkUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class WorkUnitServiceImpl implements WorkUnitService {

    @Autowired
    private WorkUnitRepository workUnitRepository;


    @Override
    public List<WorkUnit> getAllProjects() {
        List<WorkUnit> workUnits = workUnitRepository.findAll();
        Collections.sort(workUnits, Comparator.comparing(WorkUnit::getId));
        return workUnits;
    }

    @Override
    public WorkUnit saveWorkUnit(WorkUnit workUnit) {
        WorkUnit newWorkUnit = workUnitRepository.save(workUnit);
        workUnitRepository.flush();
        return newWorkUnit;
    }
}
