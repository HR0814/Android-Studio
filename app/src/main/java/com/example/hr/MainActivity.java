package com.example.hr;

import android.graphics.Color;
import android.graphics.PointF;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.model.MarkerOptions;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.overlay.PolygonOverlay;
import com.naver.maps.map.overlay.PolylineOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    NaverMap mNaverMap;
    CheckBox checkBox;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private ArrayList<Marker> listMarker;
    private ArrayList<LatLng> listLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        checkBox = findViewById(R.id.checkBox);
        listMarker = new ArrayList<>();
        listLatLng = new ArrayList<>();

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                mNaverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);

    }

    @Override
    public void onMapReady(@NonNull @NotNull NaverMap naverMap) {
        mNaverMap = naverMap;
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.map_option, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        naverMap.setMapType(NaverMap.MapType.Basic);
                        break;
                    case 1:
                        naverMap.setMapType(NaverMap.MapType.Navi);
                        break;
                    case 2:
                        naverMap.setMapType(NaverMap.MapType.Satellite);
                        break;
                    case 3:
                        naverMap.setMapType(NaverMap.MapType.Hybrid);
                        break;
                    case 4:
                        naverMap.setMapType(NaverMap.MapType.Terrain);
                        break;
                    case 5:
                        naverMap.setMapType(NaverMap.MapType.None);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                naverMap.setMapType(NaverMap.MapType.Basic);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mNaverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, isChecked);
            }
        });

        naverMap.setOnMapClickListener((point, coord) ->
                Toast.makeText(this, coord.latitude + ", " + coord.longitude,
                        Toast.LENGTH_SHORT).show());



        Marker marker0 = new Marker();
        Marker marker1 = new Marker();
        Marker marker2 = new Marker();
        marker0.setPosition(new LatLng(35.9471019, 126.6814201));
        marker1.setPosition(new LatLng(35.9676514, 126.7368582));
        marker2.setPosition(new LatLng(35.9763355, 126.6247085));
        marker0.setMap(naverMap);
        marker1.setMap(naverMap);
        marker2.setMap(naverMap);

        PolylineOverlay polyline = new PolylineOverlay();
        polyline.setCoords(Arrays.asList(
                new LatLng(35.9471019, 126.6814201),
                new LatLng(35.9676514, 126.7368582),
                new LatLng(35.9763355, 126.6247085),
                new LatLng(35.9471019, 126.6814201)
        ));
        polyline.setMap(naverMap);

        PolygonOverlay polygon = new PolygonOverlay();
        polygon.setCoords(Arrays.asList(
                new LatLng(35.9471019, 126.6814201),
                new LatLng(35.9676514, 126.7368582),
                new LatLng(35.9763355, 126.6247085),
                new LatLng(35.9471019, 126.6814201)
        ));
        polygon.setMap(naverMap);
        polygon.setColor(Color.argb(125,255,0,0));

        Marker[] marker = {new Marker()};
        mNaverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull @NotNull PointF pointF, @NonNull @NotNull LatLng latLng) {
                //TODO 인자로 넘어온 latlng를 사용하여 marker를 찍는다.
                Marker marker = new Marker();
                marker.setPosition(latLng);
                marker.setMap(mNaverMap);
                listMarker.add(marker);
                listLatLng.add(latLng);
                if (listLatLng.size()>2){
                polygon.setCoords(listLatLng);
                }
            }
        });


        Button button0 = (Button) findViewById(R.id.button0);
        button0.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                marker0.setMap(null);
                marker1.setMap(null);
                marker2.setMap(null);
                polyline.setMap(null);
                polygon.setMap(null);
                for(int i=0;i<listMarker.size();i++)
                {
                    listMarker.get(i).setMap(null);
                }
            }
        });



        this.mNaverMap = naverMap;
        naverMap.setLocationSource(locationSource);

        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);



        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CameraUpdate cameraUpdate = CameraUpdate.fitBounds(polyline.getBounds());
                naverMap.moveCamera(cameraUpdate);
            }
        });


//        InfoWindow infoWindow = new InfoWindow();
//        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(context) {
//            @NonNull
//            @Override
//            public CharSequence getText(@NonNull InfoWindow infoWindow) {
//                return "지번주소";
//            }
//        });
//
//
//        Marker[] marker3 = {new Marker()};
//        mNaverMap.setOnMapLongClickListener(new NaverMap.OnMapLongClickListener() {
//            @Override
//            public void onMapLongClick(@NonNull @NotNull PointF pointF, @NonNull @NotNull LatLng latLng) {
//                Marker marker3 = new Marker();
//                marker3.setPosition(latLng);
//                marker3.setMap(mNaverMap);
//                listMarker.add(marker3);
//                listLatLng.add(latLng);
//
//                infoWindow.open(marker3);
//            }
//        });
    }















//    public void onCheckboxClicked(View view){
//        boolean checked = ((CheckBox) view).isChecked();
//
//        switch (view.getId()){
//            case R.id.checkBox:
//                if (checked)
//                    mNaverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, true);
//                else
//                    mNaverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_CADASTRAL, false);
//                break;
//        }
//    }

}

//        final Marker[] marker = {new Marker()};
//        mNaverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(@NonNull @NotNull PointF pointF, @NonNull @NotNull LatLng latLng) {
//                marker[0] = mNaverMap.addMarker(new MarkerOptions())
//                        .position(latLng);
//                System.out.println(latLng);
//            }
//        });
