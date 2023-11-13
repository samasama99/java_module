package fr.leet.chat.repositories;

public class NotSavedSubEntityException extends RuntimeException {
    public NotSavedSubEntityException(String message) {
        super(message);
    }
}
