package shipper.com.ads.types;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.security.SignatureException;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import shipper.com.ads.R;
import shipper.com.ads.api.request.RequestApi;
import shipper.com.ads.api.response.GetAdsResponse;
import shipper.com.ads.api.response.InstallAdsResponse;
import shipper.com.ads.api.response.NormalAdsResponse;
import shipper.com.ads.api.sender.GetAdsSender;
import shipper.com.ads.api.sender.InstallAdsSender;
import shipper.com.ads.utils.BitmapUtils;
import shipper.com.ads.utils.Utils;

public class GridViewAds extends FrameLayout {
    public static int NUMBER_COLUMNS = 3;

    private Activity mAct;
    private Context mContext;
    private GridView gridView;
    private GridAdapter adapter;
    private ArrayList<NormalAdsResponse> adsList = new ArrayList<>();
    private IOnClick iOnClick;

    public GridViewAds(Context context) {
        super(context);
        initView(context);
    }

    public GridViewAds(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public GridViewAds(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        isInEditMode();
        this.mContext = context;

        View v = LayoutInflater.from(mContext).inflate(R.layout.gridview_ads_layout, null, false);
        gridView = (GridView) v.findViewById(R.id.gridView);
        adapter = new GridAdapter(context, adsList);
        gridView.setNumColumns(NUMBER_COLUMNS);
        gridView.setAdapter(adapter);
        addView(v);
    }

    public void getAds(Activity activity, IOnClick onClick) {
        this.mAct = activity;
        this.iOnClick = onClick;
        this.adsList.clear();

        GetAdsSender sender = new GetAdsSender();
        sender.device_id = Utils.getDeviceId(mContext);
        sender.timestamp = Utils.getTimeStamp();
        try {
            sender.token = Utils.hashMac(sender.getParamsString());
        } catch (SignatureException e) {
            e.printStackTrace();
        }

        RequestApi requestApi = new RequestApi();
        requestApi.getAds(sender, new Callback<GetAdsResponse>() {
            @Override
            public void success(GetAdsResponse getAdsResponse, Response response) {
                if (getAdsResponse != null && 0 == getAdsResponse.code && getAdsResponse.normalAdsList != null) {
                    adsList.addAll(getAdsResponse.normalAdsList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public class GridAdapter extends ArrayAdapter<NormalAdsResponse> {
        private ArrayList<NormalAdsResponse> adsList;

        public GridAdapter(Context context, ArrayList<NormalAdsResponse> adsList) {
            super(context, R.layout.item_gridview_ads);
            this.adsList = adsList;
        }

        @Override
        public int getCount() {
            return adsList != null ? adsList.size() : 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview_ads, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.container = convertView.findViewById(R.id.container);
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
                viewHolder.tv = (TextView) convertView.findViewById(R.id.tvHeader);

                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();

            if (position >= 0 && position < getCount()) {
                final NormalAdsResponse item = adsList.get(position);
                NormalAdsResponse.LanguageAds languageAds = item.languageAdsList
                        .get(item.languageAdsList.indexOf(item.defaultLanguage));
                BitmapUtils.setImageWithUILoader(languageAds.icon, viewHolder.icon, android.R.color.transparent);
                viewHolder.tv.setText(languageAds.name);

                viewHolder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        installApp(item);
                        if (iOnClick != null)
                            iOnClick.onClick(item);
                    }
                });
            }

            return convertView;
        }

        private final class ViewHolder {
            View container;
            ImageView icon;
            TextView tv;
        }

    }

    private void installApp(NormalAdsResponse item) {
        if (mAct != null) {
            Utils.openAppFromPlayStore(mAct, item.packageApp);

            InstallAdsSender sender = new InstallAdsSender();
            sender.country = Utils.getLocaleCurrently(mAct.getApplicationContext());
            sender.device = Utils.getDeviceId(mAct.getApplicationContext());
            sender.packageApp = item.packageApp;
            sender.timestamp = Utils.getTimeStamp();
            try {
                sender.token = Utils.hashMac(sender.getParamsString());
            } catch (SignatureException e) {
                e.printStackTrace();
            }

            RequestApi requestApi = new RequestApi();
            requestApi.installAds(sender, new Callback<InstallAdsResponse>() {
                @Override
                public void success(InstallAdsResponse installAdsResponse, Response response) {

                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }

    public interface IOnClick {
        void onClick(NormalAdsResponse item);
    }
}
