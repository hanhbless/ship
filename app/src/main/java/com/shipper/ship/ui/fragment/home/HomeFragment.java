package com.shipper.ship.ui.fragment.home;

import android.view.View;
import android.widget.Button;

import com.shipper.ship.R;
import com.shipper.ship.ui.eventbus.DialogAdsEvent;
import com.shipper.ship.ui.fragment.BaseFragment;

import de.greenrobot.event.EventBus;
import shipper.com.ads.types.DialogAds;

public class HomeFragment extends BaseFragment {
    private EventBus bus = EventBus.getDefault();
    private DialogAds dialogAds;

    @Override
    protected int getContentView() {
        return R.layout.home_fragment_layout;
    }

    @Override
    protected void initView() {
        Button btnOpenChildScreen = (Button) findViewById(R.id.btn_open_child);
        btnOpenChildScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMainActivity().openChild();
            }
        });

        dialogAds = new DialogAds(getActivity());
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }

    //-- On receiver when child fragment closed
    //-- Show dialog ads in here
    public void onEvent(DialogAdsEvent event) {
        dialogAds.checkShow();
    }


}
