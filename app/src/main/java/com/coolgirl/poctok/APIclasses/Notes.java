package com.coolgirl.poctok.APIclasses;

public class Notes {
    public int userid;
    public int plantid;
    public String image;
    public String notetext;
    public int noteid;
    public String notedata;

    public void setNotetext(String notetext) {
        this.notetext = notetext;
    }

    public void setNoteid(int noteid) {
        this.noteid = noteid;
    }

    public void setNotedata(String notedata) {
        this.notedata = notedata;
    }

    public UserLoginData user;
    public String getNotetext() {
        return notetext;
    }

    public int getNoteid() {
        return noteid;
    }

    public String getNotedata() {
        return notedata;
    }


}
