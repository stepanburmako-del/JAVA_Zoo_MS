package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class ZooManager {
    private static final Logger logger = LogManager.getLogger(ZooManager.class);
    private List<Animal> animals = new ArrayList<>();
    private static final String FILE_NAME = "zoo_data.dat";

    public void addAnimal(Animal animal) {
        animals.add(animal);
        logger.info("Добавлено животное: {} ({})", animal.getName(), animal.getType());
    }

    public List<Animal> getAll() { return animals; }

    public boolean removeAnimal(int index) {
        if (index >= 0 && index < animals.size()) {
            Animal removed = animals.remove(index);
            logger.warn("Администратор удалил запись: {}", removed);
            return true;
        }
        logger.error("Ошибка удаления: неверный индекс {}", index);
        return false;
    }

    public List<Animal> searchByName(String name) {
        logger.debug("Поиск по имени: {}", name);
        return animals.stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public void sortByName() {
        animals.sort(Comparator.comparing(Animal::getName));
        logger.info("Выполнена сортировка по имени");
    }

    public void sortByAge() {
        animals.sort(Comparator.comparingInt(Animal::getAge));
        logger.info("Выполнена сортировка по возрасту");
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(animals);
            logger.info("Данные успешно сохранены в файл {}", FILE_NAME);
        } catch (IOException e) {
            logger.error("Ошибка при сохранении данных: ", e);
        }
    }

    @SuppressWarnings("unchecked")
    public void loadData() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            logger.info("Файл данных не найден, создана новая база.");
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            animals = (List<Animal>) ois.readObject();
            logger.info("Данные успешно загружены. Записей: {}", animals.size());
        } catch (Exception e) {
            logger.error("Критическая ошибка загрузки: ", e);
        }
    }
}
