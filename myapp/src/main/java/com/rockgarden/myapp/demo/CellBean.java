package com.rockgarden.myapp.demo;

/**
 * Created by rockgarden on 16/4/27.
 */
public class CellBean {

    public String CellName;
    public double CellLat;
    public double CellLon;
    public String NetType;

    public CellBean(String cellName, double cellLat, double cellLon, String netType, double dirctionAngle) {
        CellName = cellName;
        CellLat = cellLat;
        CellLon = cellLon;
        NetType = netType;
        DirctionAngle = dirctionAngle;
    }

    public double getDirctionAngle() {
        return DirctionAngle;
    }

    public void setDirctionAngle(double dirctionAngle) {
        DirctionAngle = dirctionAngle;
    }

    public String getCellName() {
        return CellName;
    }

    public void setCellName(String cellName) {
        CellName = cellName;
    }

    public double getCellLat() {
        return CellLat;
    }

    public void setCellLat(double cellLat) {
        CellLat = cellLat;
    }

    public double getCellLon() {
        return CellLon;
    }

    public void setCellLon(double cellLon) {
        CellLon = cellLon;
    }

    public String getNetType() {
        return NetType;
    }

    public void setNetType(String netType) {
        NetType = netType;
    }

    public double DirctionAngle;

}
