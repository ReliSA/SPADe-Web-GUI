package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Commit;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.CommitRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.CommittedConfigurationRepository;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.EntityConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommitServiceImpl implements CommitService {

    @Autowired
    private CommitRepository commitRepository;

    @Autowired
    private EntityConfigurationRepository configurationRepository;

    @Autowired
    private CommittedConfigurationRepository committedConfigurationRepository;

    @Override
    public List<Commit> getAllCommits() {
        List<Commit> commits = commitRepository.findAll();
        Collections.sort(commits, Comparator.comparing(Commit::getId));
        return commits;
    }

    @Override
    public Commit getCommitById(Long id) {
        Optional<Commit> result = commitRepository.findById(id);
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get();
        }
    }

    @Override
    public Commit saveCommit(Commit commit) {
        Commit newCommit = commitRepository.save(commit);
        commitRepository.flush();
        return newCommit;
    }

    @Override
    public List<Commit> getReleasedCommitsByProject(Long id) {
        return commitRepository.getReleasedCommitsByProject(id);
    }

    @Override
    public Commit getCommitByIdentifier(Long projectId, String identifier) {
        return commitRepository.getCommitByIdentifier(projectId, identifier);
    }

}
