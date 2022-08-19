package com.example.andmoduleads.admob;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.control.ads.nativeAds.TLAdPlacerSettings;
import com.ads.control.ads.nativeAds.AdmobRecyclerAdapter;
import com.example.andmoduleads.R;
import com.example.andmoduleads.applovin.ListSimpleAdapter;

public class AdmobSimpleListActivity extends AppCompatActivity {
    private static final String TAG = "SimpleListActivity";
    AdmobRecyclerAdapter aperoRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);

        // init adapter origin
        ListSimpleAdapter originalAdapter = new ListSimpleAdapter();
        RecyclerView recyclerView = findViewById(R.id.rvListSimple);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        TLAdPlacerSettings settings = new TLAdPlacerSettings(com.ads.control.R.layout.custom_native_admod_medium,com.ads.control.R.layout.layout_native_medium);
        settings.setAdUnitId(getString(R.string.admod_native_id));
        settings.setRepeatingInterval(3);
        aperoRecyclerAdapter = new AdmobRecyclerAdapter(settings,originalAdapter,this);
        recyclerView.setAdapter(aperoRecyclerAdapter);
//        aperoRecyclerAdapter.loadAds();
    }



    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}