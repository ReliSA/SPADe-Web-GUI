package cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces;

public interface Classification {

    /**
     * Method getting ID of class
     * @return  Classification ID
     */
    Long getId();

    /**
     * Method getting name of class
     * @return  Classification name
     */
    String getName();

    /**
     * Method getting super class name
     * @return  Superclass name
     */
    String getSuperClass();
}
