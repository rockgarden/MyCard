package com.rockgarden.myapp.uitl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.text.TextPaint;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Baidu Map 通用绘图方法
 * Created by wangkan on 16/4/28.
 */
public class BaiduMapUtil {

    /**
     * 计算伪扇形的多个顶点
     *
     * @param point
     * @param radius
     * @param dirctionAngle
     * @return
     */
    public static List<LatLng> getSectorPoints(LatLng point, double radius, double dirctionAngle, double halfAngle) {
        List<LatLng> points = new ArrayList<LatLng>();
        if (halfAngle < 5) halfAngle = 5;
        double step = 5;
        if (halfAngle * 2 / 5 > 10) step = halfAngle * 2 / 10;
        points.add(point);
        for (double i = dirctionAngle - halfAngle; i < dirctionAngle + halfAngle + 0.001; i += step) {
            points.add(EOffsetBearing(point, radius, i));
        }
        return points;
    }

    /**
     * 计算顶点坐标
     *
     * @param point
     * @param dist
     * @param bearing
     * @return
     */
    public static LatLng EOffsetBearing(LatLng point, double dist,
                                        double bearing) {
        float[] distance = new float[1];
        LatLng latPoint = new LatLng(point.latitude + 0.1, point.longitude);
        Location.distanceBetween(point.latitude, point.longitude,
                latPoint.latitude, latPoint.longitude, distance);
        double latConv = distance[0] * 10;
        LatLng lonPoint = new LatLng(point.latitude, point.longitude + 0.1);
        Location.distanceBetween(point.latitude, point.longitude,
                lonPoint.latitude, lonPoint.longitude, distance);
        double lngConv = distance[0] * 10;
        double lat = dist * Math.cos(bearing * Math.PI / 180) / latConv;
        double lng = dist * Math.sin(bearing * Math.PI / 180) / lngConv;
        return new LatLng(point.latitude + lat, point.longitude + lng);
    }

    /**
     * 将文字转换为图片bitmap
     *
     * @param str
     * @param color
     * @return
     */
    public static Bitmap StringToBitMap(String str, int color) {
        int width = 0;
        for (int i = 0; i <= str.length(); i++) {
            width += 22;
        }
        int height = 22;
        int textSize = 20;
        Bitmap codeBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(codeBitmap);

        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);
        textPaint.setStrokeWidth(1);

        int x = 22;
        int y = (height + textSize) / 2;
        for (int index = 0; index < str.length(); index++) {
            textPaint.setColor(color);
            canvas.drawText(str.charAt(index) + "", (x + textSize * index), y,
                    textPaint);
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return codeBitmap;
    }

    /**
     * 获取三角形三个点
     *
     * @param lat
     * @param lon
     * @return
     */
    public static List<GeoPoint> GetTrianglePoint(double lat, double lon, double radius, double DirctionAngle) {
        List<GeoPoint> list = new ArrayList<GeoPoint>();
        double TriangleAngle = 5.0;
        double latEx1 = lat + radius * Math.cos(DirctionAngle + TriangleAngle);
        double lonEx1 = lon + radius * Math.sin(DirctionAngle + TriangleAngle);
        GeoPoint gp1 = new GeoPoint((int) (latEx1 * 1E6), (int) (lonEx1 * 1E6));
        double latEx2 = lat + radius * Math.cos(DirctionAngle - TriangleAngle);
        double lonEx2 = lon + radius * Math.sin(DirctionAngle - TriangleAngle);
        GeoPoint gp2 = new GeoPoint((int) (latEx2 * 1E6), (int) (lonEx2 * 1E6));
        list.add(gp1);
        list.add(gp2);
        return list;
    }

    /**
     * 获取基站外围点的坐标
     *
     * @param lat
     * @param lon
     * @return
     */
    public static GeoPoint GetOnePoint(double lat, double lon, double radius, double DirctionAngle) {
        double latEx = lat + radius * Math.cos(DirctionAngle * Math.PI / 180);
        double lonEx = lon + radius * Math.sin(DirctionAngle * Math.PI / 180);
        //double lonEx = lon+radius*Math.sin(DirctionAngle*Math.PI/180)*143239.448782706*Math.asin(6.9813170079206E-6/Math.cos(lat*Math.PI/180));
        GeoPoint gp = new GeoPoint((int) (latEx * 1E6), (int) (lonEx * 1E6));
        return gp;
    }

    public static LatLng getOnePoint(double lat, double lon, double radius, double DirctionAngle) {
        double latEx = lat + radius * Math.cos(DirctionAngle * Math.PI / 180);
        double lonEx = lon + radius * Math.sin(DirctionAngle * Math.PI / 180);
        //double lonEx = lon+radius*Math.sin(DirctionAngle*Math.PI/180)*143239.448782706*Math.asin(6.9813170079206E-6/Math.cos(lat*Math.PI/180));
        return new LatLng((int) (latEx * 1E6), (int) (lonEx * 1E6));
    }
}
