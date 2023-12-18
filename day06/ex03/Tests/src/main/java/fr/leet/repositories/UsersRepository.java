package fr.leet.repositories;

import fr.leet.models.User;

public interface UsersRepository {
    User findByLogin(String login) throws EntityNotFoundException;

    void update(User user);

    class EntityNotFoundException extends Exception {
        EntityNotFoundException() {
            super();
        }
    }
}
