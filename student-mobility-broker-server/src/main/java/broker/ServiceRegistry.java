package broker;

import broker.domain.Institution;

public interface ServiceRegistry {
    Institution findInstitutionBySchacHome(String schacHome);
}
