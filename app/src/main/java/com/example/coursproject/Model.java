package com.example.coursproject;

import java.util.List;

public class Model {

    private String tData;
    private String nData;
    private String dData;
    private Integer iData;
    public static boolean isSelected = false;

    public Model(String tData, String nData, String dData, Integer iData) {
        this.tData = tData;
        this.nData = nData;
        this.dData = dData;
        this.iData = iData;

    }

    public String getTitle() {
        return tData;
    }
    public String getNote() {
        return nData;
    }
    public String getDate() {
        return dData;
    }
    public Integer getid() {
        return iData;
    }

    public static void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static boolean isSelected() {
        return isSelected;
    }
}
