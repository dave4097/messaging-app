package org.david.messaging.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ViewableMessageRepository extends CrudRepository<ViewableMessage, String> {
}
