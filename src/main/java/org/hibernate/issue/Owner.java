package org.hibernate.issue;

import java.util.HashMap;
import java.util.Map;

public class Owner {

  private int id;
  private String name;
  private Map<String,Pet> pets = new HashMap<String, Pet>();

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, Pet> getPets() {
    return pets;
  }

  public void setPets(Map<String, Pet> pets) {
    this.pets = pets;
  }

  @Override
  public String toString() {
    return String.format("Owner[id = %s; name = %s; pets = %s]", id, name, pets);
  }
}
