package com.coolgirl.poctok.APIclasses;

import java.util.List;

public class UserLoginData {
    public String userlogin;
    public String userpassword;
    public int userid;
    public String userdescription;
    public String username;
    public String image;

    public List<Notes> getNotes() {
        return notes;
    }

    public List<Notes> notes;

    public List<Plant> getPlants() {
        return plants;
    }

    public List<Plant> plants;

    public UserLoginData(String login, String password) {
       this.userlogin = login;
       this.userpassword = password;
    }
}


