package org.hibernate.issue;

public class Dog extends Pet {

  private int feet;

  public int getFeet() {
    return feet;
  }

  public void setFeet(int feet) {
    this.feet = feet;
  }
}
