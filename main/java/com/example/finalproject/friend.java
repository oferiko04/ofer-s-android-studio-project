package com.example.finalproject;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

import android.icu.util.Calendar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class friend {

    private String username ;
    private Date birthday ;
    private String password ;
    private int age ;
    private String  daysUntil;
    private String face ;
    private String fullName;
    private String phoneNumber ;
    private HashMap<String, String> addedComps ;

    public void setAddedComps(HashMap<String, String> addedComps) {
        this.addedComps = addedComps;
    }

    public ArrayList<Message> getMsgArr() {
        return msgArr;
    }

    public HashMap<String, String> getAddedComps() {
        return addedComps;
    }
    public void AddAddedComps(String comp, String type){
        this.addedComps.put(comp, type);
    }

    public void setMsgArr(ArrayList<Message> msgArr) {
        this.msgArr = msgArr;
    }

    private ArrayList<Message> msgArr ;

    public friend() {
    }

    public friend(String username, String password, Date birthday, int age, String daysUntil, String face){
        this.username = username ;
        this.password = password;
        this.birthday = birthday;
        this.age = age ;
        this.daysUntil = daysUntil ;
        this.face = face ;
        this.msgArr = new ArrayList<>();
        this.addedComps = new HashMap<String, String>() ;

    }
    public friend(String username, String password){
        this.username = username ;
        this.password = password;
        this.face = "pic" ;
        this.msgArr = new ArrayList<>();
        this.addedComps = new HashMap<String, String>() ;


    }
    // getter & setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDaysUntil() {
        return daysUntil;
    }

    public void setDaysUntil(String daysUntil) {
        this.daysUntil = daysUntil;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
        //Date curr = new Date();


    }

    public void addMsg(Message msg) {
        this.msgArr.add(msg);
    }

    public void removeMsg(int index) {
        msgArr.remove(index);
    }


}
