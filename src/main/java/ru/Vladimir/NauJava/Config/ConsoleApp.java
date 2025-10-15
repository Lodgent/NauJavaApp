package ru.Vladimir.NauJava.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@Configuration
public class ConsoleApp {
    @Autowired
    private FileCommandProcessor commandProcessor;

    @Bean
    public CommandLineRunner commandScanner() {
        return args -> {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Введите 'help' для списка команд, 'exit' для выхода.");
                while (true) {
                    System.out.print(">>> ");
                    String input = scanner.nextLine();

                    if ("exit".equalsIgnoreCase(input.trim())) {
                        System.out.println("Выход из программы...");
                        break;
                    }

                    commandProcessor.processCommand(input);
                }
            }
        };
    }
}
