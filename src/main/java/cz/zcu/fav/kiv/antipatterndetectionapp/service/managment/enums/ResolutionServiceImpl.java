package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Resolution;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.EnumType;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.ResolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class ResolutionServiceImpl implements ResolutionService {

    @Autowired
    private ResolutionRepository resolutionRepository;

    @Override
    public List<Resolution> getAllResolutions() {
        List<Resolution> resolutions = resolutionRepository.findAll();
        Collections.sort(resolutions, Comparator.comparing(Resolution::getName, String.CASE_INSENSITIVE_ORDER));
        return resolutions;
    }

    @Override
    public EnumType getEnumById(Long id) {
        return resolutionRepository.getOne(id);
    }

    @Override
    public EnumType saveEnum(EnumType enumType) {
        Resolution newEnum = resolutionRepository.save((Resolution) enumType);
        resolutionRepository.flush();
        return newEnum;
    }
}
