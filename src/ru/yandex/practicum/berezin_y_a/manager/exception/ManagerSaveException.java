package ru.yandex.practicum.berezin_y_a.manager.exception;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException() {
    }

    public ManagerSaveException(String message) {
        super(message);
    }
}
