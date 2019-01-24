module com.acme.printer.logger {
  requires java.logging;
  requires transitive com.acme.printer.api;
  
  exports com.acme.printer.logger;
  
  provides com.acme.printer.api.Printer
    with com.acme.printer.logger.PrinterFactory;
}