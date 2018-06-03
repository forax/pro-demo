package com.github.forax.pro.example.springboot;

import static java.util.stream.Collectors.toList;

import java.lang.module.ModuleDescriptor.Requires;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings("static-method")
public class ModuleController {
  @RequestMapping("/modules")
  public Stream<String> modules() {
    return ModuleController.class.getModule().getLayer().modules().stream().map(Module::getName);
  }

  @RequestMapping("/module")
  public Optional<ModuleJSON> module(@RequestParam(value="name") String name) {
    return ModuleController.class.getModule().getLayer().findModule(name)
        .map(module -> new ModuleJSON(module.getName(), module.getDescriptor().requires().stream().map(Requires::name).collect(toList())));
  }
}