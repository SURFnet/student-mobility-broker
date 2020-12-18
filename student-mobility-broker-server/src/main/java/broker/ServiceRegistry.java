package broker;

import broker.domain.Institution;

import java.util.List;
import java.util.Optional;

public interface ServiceRegistry {

    Optional<Institution> findInstitutionBySchacHome(String schacHome);

    List<Institution> allInstitutions();
}
