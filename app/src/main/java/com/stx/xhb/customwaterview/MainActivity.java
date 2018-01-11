package com.stx.xhb.customwaterview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.stx.xhb.customwaterview.model.WaterModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<WaterModel> modelList=new ArrayList<>();
        for (int i=0;i<6;i++) {
            modelList.add(new WaterModel("sds"));
        }
        WaterFlake waterFlake=findViewById(R.id.custom_view);
        waterFlake.setModelList(modelList,300f,300f);
        waterFlake.setOnWaterItemListener(new WaterFlake.OnWaterItemListener() {
            @Override
            public void onItemClick(int pos) {
                Toast.makeText(MainActivity.this, "点击了第"+(pos+1)+"个小球", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
