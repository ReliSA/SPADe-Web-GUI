package cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces;

public interface EnumType {

    /**
     * Method getting ID of enum
     * @return  ID of EnumType instance
     */
    Long getId();

    /**
     * Method getting name of enum
     * @return  Name of EnumType instance
     */
    String getName();

    /**
     * Method getting class of enum
     * @return  Classification of EnumType instance
     */
    Classification getClassId();

    /**
     * Method setting class of enum
     * @param classId   New classification of EnumType instance
     */
    void setClassId(Classification classId);
}
