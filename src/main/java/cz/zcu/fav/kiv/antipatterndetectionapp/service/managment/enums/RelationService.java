package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.enums;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.enums.Relation;

import java.util.List;

public interface RelationService extends EnumService {
    List<Relation> getAllRelations();
}
