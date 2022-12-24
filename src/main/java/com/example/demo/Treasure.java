package com.example.demo;

public class Treasure {
    private String name;

    public Treasure() {
        String[] options = {"Sand", "Bronze", "Silver", "Folwellium"};
        int idx = (int) (Math.random() * 4);
        this.name = options[idx];
    }

    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
}
