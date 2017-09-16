package eminayar.com.cardhelper.models;

public class CardItem {

  public static final int NO_IMAGE = -1;

  public int id;
  public String title;
  public String description;
  public int image;

  public CardItem(String title, String description, int image) {
    this.title = title;
    this.description = description;
    this.image = image;
  }

  public CardItem(String title, String description) {
    this.title = title;
    this.description = description;
    this.image = NO_IMAGE;
  }
}
