package com.example.hr;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull @org.jetbrains.annotations.NotNull NaverMap naverMap) {
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


    }

}
