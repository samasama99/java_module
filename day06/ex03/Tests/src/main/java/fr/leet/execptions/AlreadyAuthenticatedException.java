package fr.leet.execptions;

public class AlreadyAuthenticatedException extends Exception {
    public AlreadyAuthenticatedException(String user) {
        super(user);
    }
}
