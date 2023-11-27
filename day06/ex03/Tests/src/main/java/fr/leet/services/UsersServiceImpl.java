package fr.leet.services;

import fr.leet.execptions.AlreadyAuthenticatedException;
import fr.leet.models.User;
import fr.leet.repositories.UsersRepository;

import java.util.Objects;

public record UsersServiceImpl(UsersRepository usersRepository) {
    boolean authenticate(String login, String password) throws AlreadyAuthenticatedException {
        try {
            User user = usersRepository.findByLogin(login);

            if (user.isAuthenticated()) throw new AlreadyAuthenticatedException();

            if (Objects.equals(user.getPassword(), password)) {
                user.setAuthenticated(true);
                usersRepository.update(user);
                return true;
            }

        } catch (UsersRepository.EntityNotFoundException e) {
            return false;
        }
        return false;
    }
}
