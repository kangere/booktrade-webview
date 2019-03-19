package com.booktrade.kangere.entities;

public class Author {

    private String Fname;

    private String Lname;

    private String Mname;

    public Author(){}

    public String getFname() {
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        Lname = lname;
    }

    public String getMname() {
        return Mname;
    }

    public void setMname(String mname) {
        Mname = mname;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(Fname).append("\t");
        if(Mname != null)
            builder.append(Mname).append("\t");
        builder.append(Lname);

        return builder.toString();
    }
}
