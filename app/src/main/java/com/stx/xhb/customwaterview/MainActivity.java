package com.stx.xhb.customwaterview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.stx.xhb.customwaterview.model.WaterModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr.xiao
 */
public class MainActivity extends AppCompatActivity {

    private WaterFlake mWaterFlake;
    private Button mBtn;
    private List<WaterModel> mModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWaterFlake = findViewById(R.id.custom_view);
        mBtn = findViewById(R.id.btn);
        mWaterFlake.setOnWaterItemListener(new WaterFlake.OnWaterItemListener() {
            @Override
            public void onItemClick(WaterModel pos) {
                Toast.makeText(MainActivity.this, "点击了", Toast.LENGTH_SHORT).show();
            }
        });
        initData();
    }

    private void initData() {
        mModelList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mModelList.add(new WaterModel("sds"));
        }
        mBtn.post(new Runnable() {
            @Override
            public void run() {
                //此处目前写死坐标，后期可以获取小树的坐标添加进去
                mWaterFlake.setModelList(mModelList, mBtn);
            }
        });

    }

    public void onClick(View view) {
        initData();
    }
}
