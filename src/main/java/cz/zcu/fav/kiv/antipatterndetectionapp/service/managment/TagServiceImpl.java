package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Tag;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;


    @Override
    public List<Tag> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        Collections.sort(tags, Comparator.comparing(Tag::getId));
        return tags;
    }

    @Override
    public Tag getTagById(Long id) {
        Optional<Tag> result = tagRepository.findById(id);
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get();
        }
    }
}
