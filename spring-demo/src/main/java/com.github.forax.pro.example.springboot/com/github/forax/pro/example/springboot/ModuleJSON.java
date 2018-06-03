package com.github.forax.pro.example.springboot;

import java.util.List;
import java.util.Objects;

public class ModuleJSON {
  private final String name;
  private final List<String> requires;
  
  public ModuleJSON(String name, List<String> requires) {
    this.name = Objects.requireNonNull(name);
    this.requires = Objects.requireNonNull(requires);
  }
  
  public String getName() {
    return name;
  }
  public List<String> getRequires() {
    return requires;
  }
}
