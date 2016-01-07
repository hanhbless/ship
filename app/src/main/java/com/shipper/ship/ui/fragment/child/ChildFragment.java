package com.shipper.ship.ui.fragment.child;

import android.view.View;
import android.widget.Button;

import com.shipper.ship.R;
import com.shipper.ship.ui.eventbus.DialogAdsEvent;
import com.shipper.ship.ui.fragment.BaseFragment;

import de.greenrobot.event.EventBus;

public class ChildFragment extends BaseFragment {
    @Override
    protected int getContentView() {
        return R.layout.child_fragment_layout;
    }

    @Override
    protected void initView() {
        Button btnClose = (Button) findViewById(R.id.btn_close_child);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openHome();
                EventBus.getDefault().post(new DialogAdsEvent());
            }
        });
    }
}
