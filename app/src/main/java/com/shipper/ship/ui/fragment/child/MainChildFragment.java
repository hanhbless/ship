package com.shipper.ship.ui.fragment.child;

import com.shipper.ship.R;
import com.shipper.ship.ui.fragment.BaseFragment;

public class MainChildFragment extends BaseFragment {
    @Override
    protected int getContentView() {
        return R.layout.fragment_child;
    }

    @Override
    protected void initView() {
        openFragment(ChildFragment.class, false);
    }
}
