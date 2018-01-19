package com.stx.xhb.customwaterview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.stx.xhb.customwaterview.model.WaterModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WaterFlake mWaterFlake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWaterFlake = findViewById(R.id.custom_view);
        mWaterFlake.setOnWaterItemListener(new WaterFlake.OnWaterItemListener() {
            @Override
            public void onItemClick(int pos) {
                Toast.makeText(MainActivity.this, "点击了第"+(pos+1)+"个小球", Toast.LENGTH_SHORT).show();
            }
        });
        initData();
    }

    private void initData() {
        List<WaterModel> modelList=new ArrayList<>();
        for (int i=0;i<6;i++) {
            modelList.add(new WaterModel("sds"));
        }
        //此处目前写死坐标，后期可以获取小树的坐标添加进去
        mWaterFlake.setModelList(modelList,300,400);
    }

    public void onClick(View view) {
         initData();
    }
}
