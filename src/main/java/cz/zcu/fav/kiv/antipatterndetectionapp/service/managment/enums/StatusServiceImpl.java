package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Status;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.EnumType;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class StatusServiceImpl implements StatusService {

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public List<Status> getAllStatuses() {
        List<Status> statuses = statusRepository.findAll();
        Collections.sort(statuses, Comparator.comparing(Status::getName, String.CASE_INSENSITIVE_ORDER));
        return statuses;
    }

    @Override
    public EnumType getEnumById(Long id) {
        return statusRepository.getOne(id);
    }

    @Override
    public EnumType saveEnum(EnumType enumType) {
        Status newEnum = statusRepository.save((Status) enumType);
        statusRepository.flush();
        return newEnum;
    }
}
