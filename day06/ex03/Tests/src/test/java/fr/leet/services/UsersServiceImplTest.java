package fr.leet.services;

import fr.leet.execptions.AlreadyAuthenticatedException;
import fr.leet.models.User;
import fr.leet.repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UsersServiceImplTest {
    private UsersRepository usersRepositoryMock;
    private UsersServiceImpl usersService;

    @BeforeEach
    public void init() {
        this.usersRepositoryMock = mock(UsersRepository.class);
        this.usersService = new UsersServiceImpl(usersRepositoryMock);
    }

    @Test
    void testCorrectLoginPassword() throws UsersRepository.EntityNotFoundException, AlreadyAuthenticatedException {
        when(usersRepositoryMock.findByLogin("ValidLogin"))
                .thenReturn(new User(1L, "ValidLogin", "password", false));
        assertTrue(usersService.authenticate("ValidLogin", "password"));
        verify(usersRepositoryMock, times(1)).update(any());
    }

    @Test
    void testIncorrectLogin() throws UsersRepository.EntityNotFoundException {
        when(usersRepositoryMock.findByLogin("InvalidLogin"))
                .thenThrow(UsersRepository.EntityNotFoundException.class);
        assertDoesNotThrow(() -> assertFalse(usersService.authenticate("InvalidLogin", "password")));
    }

    @Test
    void testCorrectLoginCorrectPasswordAlreadyAuthenticated() throws UsersRepository.EntityNotFoundException {
        when(usersRepositoryMock.findByLogin("ValidLoginAlreadyAuthenticated"))
                .thenReturn(
                        new User(
                                1L,
                                "ValidLoginAlreadyAuthenticated",
                                "password",
                                true
                        )
                );
        assertThrows(
                AlreadyAuthenticatedException.class,
                () -> usersService.authenticate("ValidLoginAlreadyAuthenticated", "password"));
        verify(usersRepositoryMock, never()).update(any());
    }

    @Test
    void testCorrectLoginIncorrectPassword() throws UsersRepository.EntityNotFoundException {
        when(usersRepositoryMock.findByLogin("ValidLogin"))
                .thenReturn(new User(1L, "ValidLogin", "password", false));
        assertDoesNotThrow(
                () -> assertFalse(
                        usersService.authenticate("ValidLogin", "IncorrectPassword")
                )
        );
        verify(usersRepositoryMock, never()).update(any());
    }
}
