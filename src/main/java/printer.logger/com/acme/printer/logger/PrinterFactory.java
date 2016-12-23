package com.acme.printer.logger;

import java.util.logging.Logger;

import com.acme.printer.api.Printer;

public class PrinterFactory {
  private PrinterFactory() {
    throw new AssertionError();
  }
  
  public static Printer provider() {
    Logger logger = Logger.getLogger("PrinterFactory");
    return logger::info;
  }
}
