package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.WuType;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.EnumType;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.enums.WuTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WuTypeServiceImpl implements WuTypeService {

    @Autowired
    private WuTypeRepository wuTypeRepository;

    @Override
    public List<WuType> getAllWuTypes() {
        List<WuType> wuTypes = wuTypeRepository.findAll();
        Collections.sort(wuTypes, Comparator.comparing(WuType::getName, String.CASE_INSENSITIVE_ORDER));
        return wuTypes;
    }

    @Override
    public WuType getWuTypeById(Long id) {
        Optional<WuType> result = wuTypeRepository.findById(id);
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get();
        }
    }

    @Override
    public EnumType getEnumById(Long id) {
        return wuTypeRepository.getOne(id);
    }

    @Override
    public EnumType saveEnum(EnumType enumType) {
        WuType newEnum = wuTypeRepository.save((WuType) enumType);
        wuTypeRepository.flush();
        return newEnum;
    }
}
