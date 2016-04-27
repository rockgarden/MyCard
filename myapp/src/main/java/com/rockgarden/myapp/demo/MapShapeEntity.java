package com.rockgarden.myapp.demo;

import com.baidu.mapapi.map.Circle;
import com.baidu.mapapi.map.Dot;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.model.LatLng;

public class MapShapeEntity {

    private LatLng latlng;
    private Circle circle;
    private Polygon polygon;
    private Marker mark;
    private Dot dot;

    public MapShapeEntity() {
    }

    public MapShapeEntity(LatLng point, Circle circle, Marker mark) {
        this.latlng = point;
        this.circle = circle;
        this.mark = mark;
    }

    public MapShapeEntity(LatLng point, Polygon polygon, Marker mark) {
        this.latlng = point;
        this.polygon = polygon;
        this.mark = mark;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public Marker getMark() {
        return mark;
    }

    public void setMark(Marker mark) {
        this.mark = mark;
    }

    public Dot getDot() {
        return dot;
    }

    public void setDot(Dot dot) {
        this.dot = dot;
    }

}
