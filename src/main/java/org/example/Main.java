package org.example;

import org.example.service.Transposer;

import java.util.List;

public class Main {

    static Transposer transposer = new Transposer();
    public static void main(String[] args) {
       transposer.execute(args);
    }
}