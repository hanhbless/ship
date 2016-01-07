package com.shipper.ship.ui.fragment.home;

import com.shipper.ship.R;
import com.shipper.ship.ui.fragment.BaseFragment;

public class MainHomeFragment extends BaseFragment{
    @Override
    protected int getContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        openFragment(HomeFragment.class, false);
    }


}
