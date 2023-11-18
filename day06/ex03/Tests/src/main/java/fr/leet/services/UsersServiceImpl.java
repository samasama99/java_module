package fr.leet.services;

import fr.leet.models.User;
import fr.leet.repositories.UsersRepository;

public record UsersServiceImpl(UsersRepository usersRepository) {
    boolean authenticate(String login, String password) {
        try {
            User user = usersRepository.findByLogin(login);
            
        } catch (UsersRepository.EntityNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
