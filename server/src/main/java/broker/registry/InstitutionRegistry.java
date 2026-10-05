package broker.registry;

import broker.domain.Institution;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InstitutionRegistry {

    Optional<Institution> findInstitutionBySchacHome(String schacHome);

    Collection<Institution> allInstitutions();
}
