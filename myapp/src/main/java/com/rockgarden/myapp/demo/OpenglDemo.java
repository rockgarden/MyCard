//package baidumapsdk.demo;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.PointF;
//import android.location.Location;
//import android.os.Bundle;
//import android.text.TextPaint;
//import android.util.Log;
//
//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BaiduMap.OnMapDrawFrameCallback;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.Circle;
//import com.baidu.mapapi.map.CircleOptions;
//import com.baidu.mapapi.map.MapStatus;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.Marker;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.map.Polygon;
//import com.baidu.mapapi.map.PolygonOptions;
//import com.baidu.mapapi.map.Stroke;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.model.inner.GeoPoint;
//import com.rockgarden.myapp.R;
//import com.rockgarden.myapp.demo.CellBean;
//import com.rockgarden.myapp.demo.MapShapeEntity;
//
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.nio.FloatBuffer;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.microedition.khronos.opengles.GL10;
//
///**
// * 此demo用来展示如何在地图绘制的每帧中再额外绘制一些用户自己的内容
// */
//public class OpenglDemo extends Activity implements OnMapDrawFrameCallback {
//
//    private static final String LTAG = OpenglDemo.class.getSimpleName();
//
//    MapView mMapView;
//    BaiduMap mBaiduMap;
////    Bitmap bitmap;
//
//    CellBean cellBean1 = new CellBean("test1", 39.97923, 116.357428, "2G", 0.0);
//    CellBean cellBean2 = new CellBean("test2", 39.97923, 116.357428, "4G", 120.0);
//    CellBean cellBean3 = new CellBean("test3", 39.97923, 116.357428, "2G", 240.0);
//    private List<CellBean> cellBeanList;
//
//    {
//        cellBeanList = new ArrayList<CellBean>();
//        cellBeanList.add(cellBean1);
//        cellBeanList.add(cellBean2);
//        cellBeanList.add(cellBean3);
//    }
//
//    LatLng latlng1 = new LatLng(39.97923, 116.357428);
//    LatLng latlng2 = new LatLng(39.94923, 116.397428);
//    LatLng latlng3 = new LatLng(39.96923, 116.437428);
//    private List<LatLng> latLngPolygon;
//
//    {
//        latLngPolygon = new ArrayList<LatLng>();
//        latLngPolygon.add(latlng1);
//        latLngPolygon.add(latlng2);
//        latLngPolygon.add(latlng3);
//    }
//
//    private float[] vertexs;
//    private FloatBuffer vertexBuffer;
//    private Map<String, MapShapeEntity> cellOnMap = new HashMap<String, MapShapeEntity>();
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_opengl);
//        mMapView = (MapView) findViewById(R.id.bmapView);
//        mBaiduMap = mMapView.getMap();
//        mBaiduMap.setOnMapDrawFrameCallback(this);
////        bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.ground_overlay);
//        onPostExecute(cellBeanList, 100);
//    }
//
//    @Override
//    protected void onPause() {
//        mMapView.onPause();
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        mMapView.onResume();
//        // onResume 纹理失效
//        textureId = -1;
//        super.onResume();
//    }
//
//    @Override
//    protected void onDestroy() {
//        mMapView.onDestroy();
//        super.onDestroy();
//    }
//
//    /**
//     * 获取基站外围点的坐标
//     *
//     * @param lat
//     * @param lon
//     * @return
//     */
//    public static GeoPoint GetOnePoint(double lat, double lon, double radius, double DirctionAngle) {
//        double latEx = lat + radius * Math.cos(DirctionAngle * Math.PI / 180);
//        double lonEx = lon + radius * Math.sin(DirctionAngle * Math.PI / 180);
//        //double lonEx = lon+radius*Math.sin(DirctionAngle*Math.PI/180)*143239.448782706*Math.asin(6.9813170079206E-6/Math.cos(lat*Math.PI/180));
//        GeoPoint gp = new GeoPoint((int) (latEx * 1E6), (int) (lonEx * 1E6));
//        return gp;
//    }
//
//    public static LatLng getOnePoint(double lat, double lon, double radius, double DirctionAngle) {
//        double latEx = lat + radius * Math.cos(DirctionAngle * Math.PI / 180);
//        double lonEx = lon + radius * Math.sin(DirctionAngle * Math.PI / 180);
//        //double lonEx = lon+radius*Math.sin(DirctionAngle*Math.PI/180)*143239.448782706*Math.asin(6.9813170079206E-6/Math.cos(lat*Math.PI/180));
//        return new LatLng((int) (latEx * 1E6), (int) (lonEx * 1E6));
//    }
//
//    /**
//     * 获取三角形三个点
//     *
//     * @param lat
//     * @param lon
//     * @return
//     */
//    public static List<GeoPoint> GetTrianglePoint(double lat, double lon, double radius, double DirctionAngle) {
//        List<GeoPoint> list = new ArrayList<GeoPoint>();
//        double TriangleAngle = 5.0;
//        double latEx1 = lat + radius * Math.cos(DirctionAngle + TriangleAngle);
//        double lonEx1 = lon + radius * Math.sin(DirctionAngle + TriangleAngle);
//        GeoPoint gp1 = new GeoPoint((int) (latEx1 * 1E6), (int) (lonEx1 * 1E6));
//        double latEx2 = lat + radius * Math.cos(DirctionAngle - TriangleAngle);
//        double lonEx2 = lon + radius * Math.sin(DirctionAngle - TriangleAngle);
//        GeoPoint gp2 = new GeoPoint((int) (latEx2 * 1E6), (int) (lonEx2 * 1E6));
//        list.add(gp1);
//        list.add(gp2);
//        return list;
//    }
//
//
//    protected void onPostExecute(List<CellBean> result, double radius) {
//
//        if (result != null && result.size() > 0) {
//            for (int i = 0; i < result.size(); i++) {
//                LatLng cellPoint = new LatLng(result.get(i).getCellLat(), result.get(i).getCellLon());
//                String cellName = result.get(i).CellName;
//                if (result.get(i).getNetType().contains("室内")) {
//                    DrawCircle(cellName, cellPoint);
//                } else {
//                    if (result.get(i).getNetType().contains("2G")) {
//                        DrawSector(cellName, cellPoint, radius,
//                                result.get(i).DirctionAngle, "2G");
//                    } else {
//                        DrawSector(cellName, cellPoint, radius,
//                                result.get(i).DirctionAngle, "4G");
//                    }
//                }
//            }
//        }
//    }
//
//
//    public void DrawCircle(String cellName, LatLng location) {
//        if (cellOnMap.get(cellName) != null) {
//            return;
//        }
//        int color = Color.argb(130, 121, 168, 178);
//        // 添加圆
//        LatLng llCircle = new LatLng(location.latitude, location.latitude);
//        OverlayOptions ooCircle = new CircleOptions().fillColor(color)
//                .center(llCircle).stroke(new Stroke(2, 0xAA000000))
//                .radius(20);
//        Circle circle = (Circle) mBaiduMap.addOverlay(ooCircle);
//        // 画文字
//        Bitmap bit = StringToBitMap(cellName, Color.argb(255, 121, 168, 178));
//        MarkerOptions markerOptions = new MarkerOptions().position(location).icon(
//                BitmapDescriptorFactory.fromBitmap(bit));
//        Marker marker = (Marker) (mBaiduMap.addOverlay(markerOptions));
//
//        cellOnMap.put(cellName, new MapShapeEntity(location, circle, marker));
//        // lineIDs.add(graphicsOverlay.setData(circleGraphic));
//    }
//
//    public Bitmap StringToBitMap(String str, int color) {
//        int width = 0;
//        for (int i = 0; i <= str.length(); i++) {
//            width += 22;
//        }
//        int height = 22;
//        int textSize = 20;
//        Bitmap codeBitmap = Bitmap.createBitmap(width, height,
//                Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(codeBitmap);
//
//        TextPaint textPaint = new TextPaint();
//        textPaint.setAntiAlias(true);
//        textPaint.setTextSize(textSize);
//        textPaint.setStrokeWidth(1);
//
//        int x = 22;
//        int y = (height + textSize) / 2;
//        for (int index = 0; index < str.length(); index++) {
//            textPaint.setColor(color);
//            canvas.drawText(str.charAt(index) + "", (x + textSize * index), y,
//                    textPaint);
//        }
//        canvas.save(Canvas.ALL_SAVE_FLAG);
//        canvas.restore();
//        return codeBitmap;
//    }
//
//    public void DrawSector(String cellName, LatLng point, double radius,
//                           double dirctionAngle, String NetType) {
//        if (cellBeanList.get(cellName) != null) {
//            return;
//        }
//        List<LatLng> points = getSectorPoints(point, radius,
//                dirctionAngle);
//        PolygonOptions options = new PolygonOptions();
//        LatLng textPoint = null;
//        int color = Color.argb(130, 33, 2, 211);
//        if (NetType.contains("GSM")) {
//            color = Color.argb(130, 33, 2, 211);
//            textPoint = points.get(2);
//        } else if (NetType.contains("TD")) {
//            color = Color.argb(130, 217, 0, 0);
//            textPoint = points.get(points.size() - 1);
//        }
//        options.fillColor(color);
//        options.strokeColor(color);
//        options.strokeWidth(1);
//        options.addAll(points);
//        Polygon polygon = mMapView.addPolygon(options);
//
//        // 画文字
//        Bitmap bit = StringToBitMap(cellName, color);
//        Marker mark = mMapView.addMarker(new MarkerOptions().position(textPoint).icon(
//                BitmapDescriptorFactory.fromBitmap(bit)));
//
//        cellOnMap.put(cellName, new MapShapeEntity(point, polygon, mark));
//
//    }
//
//    public static List<LatLng> getSectorPoints(LatLng point, double radius, double dirctionAngle) {
//        List<LatLng> points = new ArrayList<LatLng>();
//        double defaultAngle = 0;
//        double step = 5;
//        double sDegree = dirctionAngle - defaultAngle;
//        double eDegree = dirctionAngle + defaultAngle;
//        if ((eDegree - sDegree) / 5 > 0) {
//            step = ((eDegree - sDegree) / 10);
//        }
//        points.add(point);
//        for (double i = sDegree; i < eDegree + 0.001; i += step) {
//            points.add(EOffsetBearing(point, radius, i));
//        }
//        return points;
//    }
//
//    public static LatLng EOffsetBearing(LatLng point, double dist,
//                                        double bearing) {
//        float[] distance = new float[1];
//        LatLng latPoint = new LatLng(point.latitude + 0.1, point.longitude);
//        Location.distanceBetween(point.latitude, point.longitude,
//                latPoint.latitude, latPoint.longitude, distance);
//        double latConv = distance[0] * 10;
//        LatLng lonPoint = new LatLng(point.latitude, point.longitude + 0.1);
//        Location.distanceBetween(point.latitude, point.longitude,
//                lonPoint.latitude, lonPoint.longitude, distance);
//        double lngConv = distance[0] * 10;
//        // var latConv = point.distanceFrom(new
//        // GLatLng(point.lat()+0.1,point.lng()))*10;
//        // var lngConv = point.distanceFrom(new
//        // GLatLng(point.lat(),point.lng()+0.1))*10;
//        double lat = dist * Math.cos(bearing * Math.PI / 180) / latConv;
//        double lng = dist * Math.sin(bearing * Math.PI / 180) / lngConv;
//        return new LatLng(point.latitude + lat, point.longitude + lng);
//    }
//
//
//    public void onMapDrawFrame(GL10 gl, MapStatus drawingMapStatus) {
//        if (mBaiduMap.getProjection() != null) {
//            calPolylinePoint(drawingMapStatus);
//            drawPolyline(gl, Color.argb(255, 255, 0, 0), vertexBuffer, 10, 3,
//                    drawingMapStatus);
////            drawTexture(gl, bitmap, drawingMapStatus);
//        }
//    }
//
//    public void calPolylinePoint(MapStatus mspStatus) {
//        PointF[] polyPoints = new PointF[latLngPolygon.size()];
//        vertexs = new float[3 * latLngPolygon.size()];
//        int i = 0;
//        for (LatLng xy : latLngPolygon) {
//            polyPoints[i] = mBaiduMap.getProjection().toOpenGLLocation(xy,
//                    mspStatus);
//            vertexs[i * 3] = polyPoints[i].x;
//            vertexs[i * 3 + 1] = polyPoints[i].y;
//            vertexs[i * 3 + 2] = 0.0f;
//            i++;
//        }
//        for (int j = 0; j < vertexs.length; j++) {
//            Log.d(LTAG, "vertexs[" + j + "]: " + vertexs[j]);
//        }
//        vertexBuffer = makeFloatBuffer(vertexs);
//    }
//
//    private FloatBuffer makeFloatBuffer(float[] fs) {
//        ByteBuffer bb = ByteBuffer.allocateDirect(fs.length * 4);
//        bb.order(ByteOrder.nativeOrder());
//        FloatBuffer fb = bb.asFloatBuffer();
//        fb.put(fs);
//        fb.position(0);
//        return fb;
//    }
//
//    private void drawPolyline(GL10 gl, int color, FloatBuffer lineVertexBuffer,
//                              float lineWidth, int pointSize, MapStatus drawingMapStatus) {
//        gl.glEnable(GL10.GL_BLEND);
//        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
//
//        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
//
//        float colorA = Color.alpha(color) / 255f;
//        float colorR = Color.red(color) / 255f;
//        float colorG = Color.green(color) / 255f;
//        float colorB = Color.blue(color) / 255f;
//
//        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVertexBuffer);
//        gl.glColor4f(colorR, colorG, colorB, colorA);
//        gl.glLineWidth(lineWidth);
//        gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, pointSize);
//
//        gl.glDisable(GL10.GL_BLEND);
//        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
//    }
//
//    int textureId = -1;
//
//    /**
//     //     * 使用opengl坐标绘制
//     //     *
//     //     * @param gl
//     //     * @param bitmap
//     //     * @param drawingMapStatus
//     //     */
////    public void drawTexture(GL10 gl, Bitmap bitmap, MapStatus drawingMapStatus) {
////        PointF p1 = mBaiduMap.getProjection().toOpenGLLocation(latlng2,
////                drawingMapStatus);
////        PointF p2 = mBaiduMap.getProjection().toOpenGLLocation(latlng3,
////                drawingMapStatus);
////        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 3 * 4);
////        byteBuffer.order(ByteOrder.nativeOrder());
////        FloatBuffer vertices = byteBuffer.asFloatBuffer();
////        vertices.put(new float[]{p1.x, p1.y, 0.0f, p2.x, p1.y, 0.0f, p1.x,
////                p2.y, 0.0f, p2.x, p2.y, 0.0f});
////
////        ByteBuffer indicesBuffer = ByteBuffer.allocateDirect(6 * 2);
////        indicesBuffer.order(ByteOrder.nativeOrder());
////        ShortBuffer indices = indicesBuffer.asShortBuffer();
////        indices.put(new short[]{0, 1, 2, 1, 2, 3});
////
////        ByteBuffer textureBuffer = ByteBuffer.allocateDirect(4 * 2 * 4);
////        textureBuffer.order(ByteOrder.nativeOrder());
////        FloatBuffer texture = textureBuffer.asFloatBuffer();
////        texture.put(new float[]{0, 1f, 1f, 1f, 0f, 0f, 1f, 0f});
////
////        indices.position(0);
////        vertices.position(0);
////        texture.position(0);
////
////        // 生成纹理
////        if (textureId == -1) {
////            int[] textureIds = new int[1];
////            gl.glGenTextures(1, textureIds, 0);
////            textureId = textureIds[0];
////            Log.d(LTAG, "textureId: " + textureId);
////            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
////            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
////            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
////                    GL10.GL_NEAREST);
////            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
////                    GL10.GL_NEAREST);
////            gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
////        }
////
////        gl.glEnable(GL10.GL_TEXTURE_2D);
////        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
////        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
////        gl.glEnable(GL10.GL_BLEND);
////        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
////        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
////
////        // 绑定纹理ID
////        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
////        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertices);
////        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture);
////
////        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, 6, GL10.GL_UNSIGNED_SHORT,
////                indices);
////
////        gl.glDisable(GL10.GL_TEXTURE_2D);
////        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
////        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
////        gl.glDisable(GL10.GL_BLEND);
////    }
//}
