package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Identity;

import java.util.List;

public interface IdentityService {

    /**
     * Method getting all identities from database
     * @return  List of all identities in database
     */
    List<Identity> getAllIdentities();

    /**
     * Method getting identity by ID
     * @param id    ID of identity
     * @return      Identity with that ID
     */
    Identity getIdentityById(Long id);

    /**
     * Method saving identity into database
     * @param identity  Identity tha will be safe
     * @return          Identity saved in database
     */
    Identity saveIdentity(Identity identity);
}
