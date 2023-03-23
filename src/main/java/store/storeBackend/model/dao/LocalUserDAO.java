package store.storeBackend.model.dao;

import org.springframework.data.repository.CrudRepository;
import store.storeBackend.model.LocalUser;

import java.util.Optional;

public interface LocalUserDAO extends CrudRepository<LocalUser, Long> {

    Optional<LocalUser> findByUsernameIgnoreCase(String username);
    Optional<LocalUser> findByEmailIgnoreCase(String email);
}
