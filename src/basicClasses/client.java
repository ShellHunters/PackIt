package basicClasses;

public class client {

  private int id;
  private String firstName, familyName;
  private int numberOfSells;

  public client(int id, String firstName, String familyName, int numberOfSells) {
    this.id = id;
    this.firstName = firstName;
    this.familyName = familyName;
    this.numberOfSells = numberOfSells;
  }
  public client(){
    this.id = 0;
    this.firstName = "";
    this.familyName = "";
    this.numberOfSells = 0;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getFamilyName() {
    return familyName;
  }

  public void setFamilyName(String familyName) {
    this.familyName = familyName;
  }

  public int getNumberOfSells() {
    return numberOfSells;
  }

  public void setNumberOfSells(int numberOfSells) {
    this.numberOfSells = numberOfSells;
  }
}
