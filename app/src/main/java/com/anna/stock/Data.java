package com.anna.stock;

//Объекты, хранимые в базе данных (товар)
public class Data {

    private String uid; //Идентификатор для Database
    private String mName; //Наименование
    private String mCode; //Артикул
    private String mPlace; //Место на складе
    private String mNumber; //Количество

    public Data (){ }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getCode() {
        return mCode;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String place) {
        mPlace = place;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    @Override
    public String toString() {
        return mName;
    }

}



