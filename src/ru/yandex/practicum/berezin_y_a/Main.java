package ru.yandex.practicum.berezin_y_a;

import ru.yandex.practicum.berezin_y_a.httpServer.HttpTaskServer;
import ru.yandex.practicum.berezin_y_a.httpServer.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        new KVServer().start();
        new HttpTaskServer().start();
    }
}
