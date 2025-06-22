package com.david.calvados.friendlychat;



public class Contacts
{

    public String name;


    public Contacts()
    {

    }

    public Contacts(String name, String status, String image) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
