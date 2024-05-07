package org.example.annotation.processor;

public class UnsupportedType extends RuntimeException {
    public UnsupportedType(String message) {
        super(message);
    }
}
