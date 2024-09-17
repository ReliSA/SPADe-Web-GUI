package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Severity;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.EnumType;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.SeverityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class SeverityServiceImpl implements SeverityService {

    @Autowired
    private SeverityRepository severityRepository;

    @Override
    public List<Severity> getAllSeverities() {
        List<Severity> severities = severityRepository.findAll();
        Collections.sort(severities, Comparator.comparing(Severity::getName, String.CASE_INSENSITIVE_ORDER));
        return severities;
    }

    @Override
    public EnumType getEnumById(Long id) {
        return severityRepository.getOne(id);
    }

    @Override
    public EnumType saveEnum(EnumType enumType) {
        Severity newEnum = severityRepository.save((Severity) enumType);
        severityRepository.flush();
        return newEnum;
    }
}
