package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Tag;

import java.util.List;

public interface TagService {

    /**
     * Method getting all tags from database
     * @return  List of all tags in database
     */
    List<Tag> getAllTags();

    /**
     * Method getting tag by ID
     * @param id    ID of tag
     * @return      Tag with that ID
     */
    Tag getTagById(Long id);
}
