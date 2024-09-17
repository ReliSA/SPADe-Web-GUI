package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Identity;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class IdentityServiceImpl implements IdentityService {

    @Autowired
    private IdentityRepository identityRepository;


    @Override
    public List<Identity> getAllIdentities() {
        List<Identity> identities = identityRepository.findAll();
        Collections.sort(identities, Comparator.comparing(Identity::getName, String.CASE_INSENSITIVE_ORDER));
        return identities;
    }

    @Override
    public Identity getIdentityById(Long id) {
        Optional<Identity> result = identityRepository.findById(id);
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get();
        }
    }

    @Override
    public Identity saveIdentity(Identity identity) {
        Identity newIdentity = identityRepository.save(identity);
        identityRepository.flush();
        return newIdentity;
    }
}
