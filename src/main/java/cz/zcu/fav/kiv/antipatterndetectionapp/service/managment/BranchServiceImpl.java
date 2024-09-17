package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Branch;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepository branchRepository;

    @Override
    public List<Branch> getAllBranches() {
        List<Branch> branches = branchRepository.findAll();
        Collections.sort(branches, Comparator.comparing(Branch::getName, String.CASE_INSENSITIVE_ORDER));
        return branches;
    }

    @Override
    public Branch getBranchById(Long id) {
        Optional<Branch> result = branchRepository.findById(id);
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get();
        }
    }
}
