package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;


import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Priority;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.EnumType;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.PriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class PriorityServiceImpl implements PriorityService {

    @Autowired
    private PriorityRepository priorityRepository;

    @Override
    public List<Priority> getAllPriorities() {
        List<Priority> priorities = priorityRepository.findAll();
        Collections.sort(priorities, Comparator.comparing(Priority::getName, String.CASE_INSENSITIVE_ORDER));
        return priorities;
    }

    @Override
    public EnumType getEnumById(Long id) {
        return priorityRepository.getOne(id);
    }

    @Override
    public EnumType saveEnum(EnumType enumType) {
        Priority newEnum = priorityRepository.save((Priority) enumType);
        priorityRepository.flush();
        return newEnum;
    }
}
