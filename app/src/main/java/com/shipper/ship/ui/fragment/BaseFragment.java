package com.shipper.ship.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.shipper.ship.ui.activity.MainActivity;
import com.shipper.ship.MyApplication;
import com.shipper.ship.R;
import com.shipper.ship.ui.activity.BaseActivity;
import com.shipper.ship.utils.FragmentUtils;
import com.shipper.ship.utils.GlobalSingleton;
import com.shipper.ship.utils.Log;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

public abstract class BaseFragment extends Fragment implements Serializable {

    private View view;

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager
                .LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        Log.i("onActivityCreated: " + this.getClass().getSimpleName());
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager
                .LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        Log.i("onResume: " + this.getClass().getSimpleName());
    }

    @Override
    public void onDestroyView() {
        Log.i("onDestroyView: " + this.getClass().getSimpleName());
        // unbindToView(view);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i("onDestroy: " + this.getClass().getSimpleName());
        //unbindToView(view);
        super.onDestroy();
    }

    protected View findViewById(int id) {
        if (view != null) {
            return view.findViewById(id);
        }
        return null;

    }

    public void changeLanguage() {
        /*if (getActivity() != null) {
            if (GlobalSingleton.INSTANCE.getStrLocaleLanguage() != null) {
                Configuration configuration = getResources().getConfiguration();
                Locale locale = new Locale(GlobalSingleton.INSTANCE.getStrLocaleLanguage());
                Log.d("BaseFragment Locale chosen: " + locale.toString());
                Log.d("BaseFragment Locale resources: " + configuration.locale.toString());
                if (!locale.equals(configuration.locale)) {
                    Utils.setLanguage(GlobalSingleton.INSTANCE.getStrLocaleLanguage(), MyApplication.applicationContext);
                }
            }
        }*/
    }


    /**
     * get activity of fragment, and cast to MainActivity.
     * Note: return null if activity of fragment not instance of MainActivity
     */
    protected MainActivity getMainActivity() {
        MainActivity mainActivity = null;
        if (getActivity() != null
                && MainActivity.class.isInstance(getActivity())) {
            mainActivity = (MainActivity) getActivity();
            return mainActivity;
        }
        return mainActivity;
    }

    public BaseActivity getBaseActivity() {
        BaseActivity baseActivity = null;
        if (getActivity() != null
                && BaseActivity.class.isInstance(getActivity())) {
            baseActivity = (BaseActivity) getActivity();
            return baseActivity;
        }
        return baseActivity;
    }

    protected abstract int getContentView();

    protected abstract void initView();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("onCreateView: " + this.getClass().getSimpleName());
        int layoutId = getContentView();
        if (layoutId > 0) {
            view = inflater.inflate(layoutId, null);

        } else {
            view = super.onCreateView(inflater, container,
                    savedInstanceState);
        }
        initView();
        return view;
    }

    protected String getResourceString(int resId) {
        if (getActivity() == null) {
            Log.i("getResourceString: activity null");
        }
        return MyApplication.getContext().getString(resId);
    }

    public void openFragment(Class<? extends Fragment> fragmentClazz) {
        openFragment(fragmentClazz, null, true);
    }

    public void openFragment(Class<? extends Fragment> fragmentClazz,
                             boolean addBackStack) {
        openFragment(fragmentClazz, null, addBackStack);
    }

    public void openFragment(Class<? extends Fragment> fragmentClazz,
                             Bundle args) {
        openFragment(fragmentClazz, args, true);
    }

    public void openFragment(Class<? extends Fragment> fragmentClazz,
                             Bundle args, boolean addBackStack) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        FragmentManager manager = getChildFragmentManager();
        String tag = fragmentClazz.getName();
        Fragment fragment;
        try {
            fragment = fragmentClazz.newInstance();
            if (args != null) {
                fragment.setArguments(args);
            }
            FragmentTransaction transaction = manager.beginTransaction();
            if (!isFragmentAdded(tag)) {
                if (addBackStack) {
                    List<Fragment> fragmentVisibleList = FragmentUtils.getCurrentChildrenFragment(getActivity());
                    if (fragmentVisibleList != null) {
                        for (Fragment aFrag : fragmentVisibleList)
                            if (aFrag != null)
                                transaction.hide(aFrag);
                    }
                    transaction.add(R.id.content_frame, fragment, tag/* + sb.toString()*/);
                    transaction.addToBackStack(tag/* + sb.toString()*/);
                } else {
                    transaction.add(R.id.content_frame, fragment);
                }
            } else
                showFragment(tag, transaction);

            if (GlobalSingleton.getInstance().isAllowStateLoss()) {
                GlobalSingleton.getInstance().setAllowStateLoss(false);
                transaction.commitAllowingStateLoss();
            } else
                transaction.commit();

            Log.i("KHM base fragmnet commit " + tag);

        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            //getParentFragment().getChildFragmentManager().popBackStack();
            //GlobalSingleton.INSTANCE.setAllowStateLoss(true);
            e.printStackTrace();
        }
    }

    private Boolean isFragmentAdded(String tag) {
        FragmentManager manager = getChildFragmentManager();
        List<Fragment> fragmentList = manager.getFragments();
        if (fragmentList != null) {
            for (int i = fragmentList.size() - 1; i > -1; i--) {
                Fragment fragment = fragmentList.get(i);
                if (fragment != null && fragment.getClass().getName().equals(tag) && !fragment.isRemoving())
                    return true;
            }
        }
        return false;
    }

    private void showFragment(String tag, FragmentTransaction transaction) {
        FragmentManager manager = getChildFragmentManager();
        List<Fragment> fragmentList = manager.getFragments();
        if (fragmentList != null)
            for (Fragment fragment : fragmentList) {
                if (fragment != null) {
                    if (fragment.getClass().getName().equals(tag)) {
                        transaction.show(fragment);
                    } else {
                        transaction.hide(fragment);
                    }
                }
            }
        transaction.commit();
    }

    // Check fragment have visible
    public boolean isFragmentVisible(Class<? extends Fragment> clzz) {
        List<Fragment> listFrags = getParentFragment().getChildFragmentManager().getFragments();
        if (listFrags != null) {
            for (Fragment fragTemp : listFrags) {
                if (fragTemp != null && clzz.getName()
                        .equals(fragTemp.getClass().getName()) && fragTemp.isVisible())
                    return true;
            }
        }
        return false;
    }

    public String getString2(int resId) {
        return MyApplication.getContext().getString(resId);
    }
}

