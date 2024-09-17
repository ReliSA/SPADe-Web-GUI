package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.about;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Metadata;

import java.util.List;

/**
 * Interface used in the AboutPageServiceImpl
 *
 * @author Petr Urban
 * @since 2023-04-26
 * @version 1.0.0
 */
public interface AboutPageService {

    /**
     * method in which it's a great time to call findAll to jdbc repository
     * @return list of obtained rows from the database
     */
    List<Metadata> findAll();

}
