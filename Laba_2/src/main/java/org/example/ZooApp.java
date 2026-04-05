package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Scanner;

public class ZooApp {
    private static final Logger logger = LogManager.getLogger(ZooApp.class);
    private ZooManager manager = new ZooManager();
    private Scanner scanner = new Scanner(System.in);
    private boolean isAdmin = false;
    private final String ADMIN_PASS = "admin123";

    public void start() {
        logger.info("Приложение Зоопарк запущено.");
        manager.loadData();
        runMenu();
    }

    private void runMenu() {
        while (true) {
            printMenuHeader();
            int choice = readInt("Выберите пункт: ");
            processChoice(choice);
        }
    }

    private void printMenuHeader() {
        System.out.println("\n=== ЗООПАРК (Режим: " + (isAdmin ? "АДМИНИСТРАТОР" : "ГОСТЬ") + ") ===");
        System.out.println("1-3. Добавить (Млекоп./Птицу/Рыбу) | 4. Список всех");
        System.out.println("5. Поиск по имени | 6. Фильтр по типу | 7. Фильтр по возрасту");
        System.out.println("8. Фильтр по весу | 9. Сорт. Имя | 10. Сорт. Возраст");
        System.out.println("11. Статистика | 12. Сохранить | 13. Войти (Админ)");
        System.out.println("14. [АДМИН] Удалить | 15. [АДМИН] Редактировать | 0. Выход");
    }

    private void processChoice(int choice) {
        switch (choice) {
            case 1 -> addAnimal("Mammal");
            case 2 -> addAnimal("Bird");
            case 3 -> addAnimal("Fish");
            case 4 -> manager.getAll().forEach(System.out::println);
            case 5 -> {
                System.out.print("Введите имя для поиска: ");
                manager.searchByName(scanner.next()).forEach(System.out::println);
            }
            case 6 -> {
                System.out.print("Тип (Млекопитающее/Птица/Рыба): ");
                String type = scanner.next();
                manager.getAll().stream()
                        .filter(a -> a.getType().equalsIgnoreCase(type))
                        .forEach(System.out::println);
            }
            case 7 -> {
                int age = readInt("Возраст: ");
                manager.getAll().stream().filter(a -> a.getAge() == age).forEach(System.out::println);
            }
            case 8 -> {
                double min = readDouble("Мин. вес: ");
                double max = readDouble("Макс. вес: ");
                manager.getAll().stream()
                        .filter(a -> a.getWeight() >= min && a.getWeight() <= max)
                        .forEach(System.out::println);
            }
            case 9 -> manager.sortByName();
            case 10 -> manager.sortByAge();
            case 11 -> printStats();
            case 12 -> manager.saveData();
            case 13 -> login();
            case 14 -> deleteAction();
            case 15 -> editAction();
            case 0 -> { logger.info("Выход из программы."); System.exit(0); }
            default -> System.out.println("Неверный пункт меню.");
        }
    }

    private void login() {
        System.out.print("Введите пароль администратора: ");
        if (scanner.next().equals(ADMIN_PASS)) {
            isAdmin = true;
            logger.info("Успешный вход в систему под админом.");
            System.out.println("Доступ к функциям удаления и редактирования открыт.");
        } else {
            logger.warn("Попытка взлома или неверный пароль.");
            System.out.println("Неверный пароль!");
        }
    }

    private void deleteAction() {
        if (!isAdmin) { System.out.println("Ошибка: Требуются права администратора!"); return; }
        int id = readInt("Введите индекс животного для удаления: ");
        manager.removeAnimal(id);
    }

    private void editAction() {
        if (!isAdmin) { System.out.println("Ошибка: Требуются права администратора!"); return; }
        int id = readInt("Введите индекс для редактирования: ");
        if (id >= 0 && id < manager.getAll().size()) {
            Animal a = manager.getAll().get(id);
            logger.info("Начато редактирование объекта: {}", a.getName());
            a.setAge(readInt("Новый возраст: "));
            a.setWeight(readDouble("Новый вес: "));
            System.out.println("Данные обновлены.");
        } else {
            System.out.println("Животное с таким индексом не найдено.");
        }
    }

    private void addAnimal(String kind) {
        System.out.print("Имя: "); String n = scanner.next();
        int a = readInt("Возраст: ");
        double w = readDouble("Вес: ");
        if (kind.equals("Mammal")) manager.addAnimal(new Mammal(n, a, w));
        else if (kind.equals("Bird")) manager.addAnimal(new Bird(n, a, w));
        else manager.addAnimal(new Fish(n, a, w));
    }

    private void printStats() {
        System.out.println("--- Статистика по видам ---");
        manager.getAll().stream()
                .collect(java.util.stream.Collectors.groupingBy(Animal::getType, java.util.stream.Collectors.counting()))
                .forEach((type, count) -> System.out.println(type + ": " + count));
    }

    private int readInt(String msg) {
        System.out.print(msg);
        while (!scanner.hasNextInt()) {
            System.out.println("Ошибка: введите целое число.");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private double readDouble(String msg) {
        System.out.print(msg);
        while (!scanner.hasNextDouble()) {
            System.out.println("Ошибка: введите число (например, 5,5).");
            scanner.next();
        }
        return scanner.nextDouble();
    }
}
