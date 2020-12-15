package broker;

import broker.domain.Institution;

import java.util.Optional;

public interface ServiceRegistry {

    Optional<Institution> findInstitutionBySchacHome(String schacHome);

}
