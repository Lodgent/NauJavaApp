package ru.Vladimir.NauJava;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Scanner;

public class ConsoleApp {
    @Autowired
    private FileEntity fileService;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("1. Загрузить файл");
            System.out.println("2. Сгенерировать ссылку");
            System.out.println("3. Скачать файл");
            System.out.println("4. Выйти");

            int choice = scanner.nextInt();

        }
    }
}
