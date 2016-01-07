package com.shipper.ship.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Toast;

import com.shipper.ship.R;
import com.shipper.ship.ui.fragment.child.MainChildFragment;
import com.shipper.ship.ui.fragment.home.MainHomeFragment;
import com.shipper.ship.utils.FragmentUtils;
import com.shipper.ship.utils.OSUtils;

import shipper.com.ads.api.response.NormalAdsResponse;
import shipper.com.ads.types.GridViewAds;

public class MainActivity extends BaseActivity {

    /* ^.^ ^~^ ^.^ ^~^ ^.^ ^~^ ^.^ ^~^ ^.^
    *  Where do show Ads?
    *  GridView Ads into this class
    *  Dialog Ads into HomeFragment class
    * */

    private boolean isFirst = true;
    private View containerAds;
    private long lastBackPressTime;


    @Override
    protected int getContentFrame() {
        return R.id.content_frame;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        containerAds = findViewById(R.id.containerAds);
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
        containerAds.setVisibility(View.GONE);
        openFragment(MainChildFragment.class, null, false);
    }

    @Override
    public void onBackPressed() {
        OSUtils.hideKeyBoard(this);
        Fragment currentFragment = FragmentUtils.getCurrentFragment(this);
        if (currentFragment == null) {
            return;
        }
        Fragment currentChild = FragmentUtils.getCurrentChildFragment(this);
        if (currentChild == null) {
            return;
        }
        FragmentManager manager = currentFragment.getChildFragmentManager();
        long currentTimeMillis = System.currentTimeMillis();
        try {
            if (manager.getBackStackEntryCount() == 0) {
                boolean shouldExit = ((currentTimeMillis - lastBackPressTime) / 1000) < 3;
                if (shouldExit) {
                    super.onBackPressed();
                    System.exit(0);
                } else {
                    Toast.makeText(this,
                            R.string.press_back_again_to_exit, Toast.LENGTH_LONG).show();
                    lastBackPressTime = currentTimeMillis;
                }
            } else {
                manager.popBackStack();
            }
        } catch (NullPointerException e) {
            manager.popBackStack();
            e.printStackTrace();
        }
    }
}
