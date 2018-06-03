module printer.logger {
  requires java.logging;
  requires transitive printer.api;
  
  exports com.acme.printer.logger;
  
  provides com.acme.printer.api.Printer
    with com.acme.printer.logger.PrinterFactory;
}