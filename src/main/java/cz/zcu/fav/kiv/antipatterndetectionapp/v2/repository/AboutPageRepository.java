package cz.zcu.fav.kiv.antipatterndetectionapp.v2.repository;

import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AboutPageRepository extends JpaRepository<Metadata, Long> {

}
