package org.hibernate.issue;

public abstract class Pet {
  
  protected int age;
  private int id;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return String.format("Pet[id = %s; age = %s]", id, age);
  }
}
