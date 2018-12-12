package com.anna.stock;

import android.content.Context;
import java.util.ArrayList;

//Для работы со списком
public class DataLab {

    private ArrayList <Data> dataList;
    private static DataLab sDataLab;
    private Context mAppContext;

    private DataLab (Context appContext){
        mAppContext = appContext;
    }

    // Получить объект DataLab
    public static DataLab get (Context c){
        if (sDataLab == null)
            sDataLab = new DataLab(c.getApplicationContext());
        return sDataLab;
    }

    //Найти объект Data в списке
    public Data getData (String code){
        for (Data d : dataList){
            if (d.getCode().equals(code))
                return d;
        }
        return null;
    }

    //Заполнить список dataList объектами из Database (из MainActivity)
    public void setDataList (ArrayList<Data> data){
        dataList = data;
    }

}
