package com.shipper.ship.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.shipper.ship.R;
import com.shipper.ship.ui.fragment.child.MainChildFragment;
import com.shipper.ship.ui.fragment.home.MainHomeFragment;

import shipper.com.ads.api.response.NormalAdsResponse;
import shipper.com.ads.types.GridViewAds;

public class MainActivity extends BaseActivity {

    /* ^.^ ^~^ ^.^ ^~^ ^.^ ^~^ ^.^ ^~^ ^.^
    *  Where do show Ads?
    *  GridView Ads into this class
    *  Dialog Ads into HomeFragment class
    * */

    private boolean isFirst = true;


    @Override
    protected int getContentFrame() {
        return R.id.content_frame;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        openHome();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            //-- Show GridView ads in here
            isFirst = false;
            final View containerAds = findViewById(R.id.containerAds);
            containerAds.setVisibility(View.VISIBLE);
            GridViewAds gridViewAds = (GridViewAds) findViewById(R.id.gridViewAds);
            gridViewAds.getAds(this, new GridViewAds.IOnClick() {
                @Override
                public void onClick(NormalAdsResponse item) {
                    containerAds.setVisibility(View.GONE);
                }
            });
        }
    }

    public void openHome() {
        openFragment(MainHomeFragment.class, null, false);
    }

    public void openChild() {
        openFragment(MainChildFragment.class, null, false);
    }
}
