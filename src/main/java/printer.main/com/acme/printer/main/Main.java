package com.acme.printer.main;

import java.util.ServiceLoader;

import com.acme.printer.api.Printer;

public class Main {
  public static void main(String[] args) {
    Printer printer = ServiceLoader.load(Printer.class).findFirst().orElse(System.out::println);
    printer.print("hello world !");
  }
}
