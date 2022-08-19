package com.example.andmoduleads.applovin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.ads.control.ads.TLAppAd;
import com.ads.control.ads.TLAdConfig;
import com.ads.control.ads.nativeAds.TLAdPlacer;
import com.ads.control.ads.nativeAds.TLAdAdapter;
import com.ads.control.ads.wrapper.ApAdValue;
import com.example.andmoduleads.R;

import java.util.ArrayList;
import java.util.List;

public class MaxSimpleListActivity extends AppCompatActivity {
    private static final String TAG = "SimpleListActivity";
    TLAdAdapter adAdapter;
    int layoutCustomNative = com.ads.control.R.layout.custom_native_admod_medium;
    String idNative = "";
    SwipeRefreshLayout swRefresh;
    ListSimpleAdapter originalAdapter;
    RecyclerView recyclerView;
    TLAdPlacer.Listener listener = new TLAdPlacer.Listener() {
        @Override
        public void onAdLoaded(int i) {
            Log.i(TAG, "onAdLoaded native list: " + i);
        }

        @Override
        public void onAdRemoved(int i) {
            Log.i(TAG, "onAdRemoved: " + i);
        }

        @Override
        public void onAdClicked() {

        }

        @Override
        public void onAdRevenuePaid(ApAdValue adValue) {

        }

    };

    private List<String> sampleData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list);
        swRefresh = findViewById(R.id.swRefresh);
        addSampleData();
        // init adapter origin
        originalAdapter = new ListSimpleAdapter(new ListSimpleAdapter.Listener() {
            @Override
            public void onRemoveItem(int pos) {
//                adAdapter.notifyItemRemoved(adAdapter.getOriginalPosition(pos));
                adAdapter.getAdapter().notifyDataSetChanged();
//                setupAdAdapter();
            }
        });
        originalAdapter.setSampleData(sampleData);
        recyclerView = findViewById(R.id.rvListSimple);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        if (TLAppAd.getInstance().getMediationProvider() == TLAdConfig.PROVIDER_ADMOB) {
            layoutCustomNative = com.ads.control.R.layout.custom_native_admod_medium;
            idNative = getString(R.string.admod_native_id);
//        } else {
//            layoutCustomNative = com.ads.control.R.layout.custom_native_max_small;
//            idNative = getString(R.string.applovin_test_native);
//        }

        setupAdAdapter();
        swRefresh.setOnRefreshListener(() -> {
            sampleData.add("Item add new");
            adAdapter.getAdapter().notifyDataSetChanged();
            swRefresh.setRefreshing(false);
        });
    }

    private void setupAdAdapter() {
        adAdapter = TLAppAd.getInstance().getNativeRepeatAdapter(this, idNative, layoutCustomNative, com.ads.control.R.layout.layout_native_medium,
                originalAdapter, listener, 4);

        recyclerView.setAdapter(adAdapter.getAdapter());
        adAdapter.loadAds();
    }

    private void addSampleData() {
        for (int i = 0; i < 3; i++) {
            sampleData.add("Item " + i);
        }
    }

    @Override
    public void onDestroy() {
        adAdapter.destroy();
        super.onDestroy();
    }
}