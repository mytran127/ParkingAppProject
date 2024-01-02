package com.example.parkfinder2;

public class ReadWriteUserDetails {
    public String name, email, gwid;

    public ReadWriteUserDetails(){};

    public ReadWriteUserDetails(String textName, String textEmail,String textGWID){
        this.name = textName;
        this.email = textEmail;
        this.gwid = textGWID;
    }
}
