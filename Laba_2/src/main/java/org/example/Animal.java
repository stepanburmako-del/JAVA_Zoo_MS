package org.example;

import java.io.Serializable;

abstract class Animal implements Serializable {
    private String name;
    private int age;
    private double weight;

    public Animal(String name, int age, double weight) {
        this.name = name;
        this.age = age;
        this.weight = weight;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public abstract String getType();

    @Override
    public String toString() {
        return String.format("[%s] Имя: %s, Возраст: %d, Вес: %.2f", getType(), name, age, weight);
    }
}

class Mammal extends Animal {
    public Mammal(String name, int age, double weight) { super(name, age, weight); }
    @Override public String getType() { return "Млекопитающее"; }
}

class Bird extends Animal {
    public Bird(String name, int age, double weight) { super(name, age, weight); }
    @Override public String getType() { return "Птица"; }
}

class Fish extends Animal {
    public Fish(String name, int age, double weight) { super(name, age, weight); }
    @Override public String getType() { return "Рыба"; }
}
