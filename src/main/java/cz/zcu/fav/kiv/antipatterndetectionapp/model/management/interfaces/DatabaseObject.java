package cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces;

public interface DatabaseObject {

    String getObjectName();

    String getAttributeValue(String attr);
}
