package basicClasses;


import javafx.scene.control.CheckBox;

public class Provider {
    String FirstName;
    String LastName;
    String PhoneNumber;
    String Email;
    String Address;

    Integer id;
CheckBox selected;


    float TotalFigure;

    public CheckBox getSelected() {
        return selected;
    }

    public void setSelected(CheckBox selected) {
        this.selected = selected;
    }

    public void  initCheckBox(){
    selected = new CheckBox();
}
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String adress) {
        Address = adress;
    }


    public float getTotalFigure() {
        return TotalFigure;
    }

    public void setTotalFigure(float totalFigure) {
        TotalFigure = totalFigure;
    }


        public Provider(String firstName, String lastName, String phoneNumber, String email, String address, Integer id, float totalFigure) {
            FirstName = firstName;
            LastName = lastName;
            PhoneNumber = phoneNumber;
            Email = email;
            Address = address;
            this.id = id;
            TotalFigure = totalFigure; }

    public Provider (String firstName, String lastName, String phoneNumber, String email) {
        FirstName = firstName;
        LastName = lastName;
        PhoneNumber = phoneNumber;
        Email = email;

    }

    public Provider () {

    }

    public Provider ( Integer id,String firstName, String lastName , float totalFigure, String email) {
        FirstName = firstName;
        LastName = lastName;
        this.id = id;
        Email=email;
        TotalFigure = totalFigure;
    }

    @Override
    public String toString(){
        return this.FirstName+"  "+this.LastName;

    }
}

