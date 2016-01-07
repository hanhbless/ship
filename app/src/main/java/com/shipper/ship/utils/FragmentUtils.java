package com.shipper.ship.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class FragmentUtils {

    public static void clearBackStackByPopping(FragmentManager fm) {
        if (fm == null)
            return;

        try {
            //fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }

    public static Fragment getCurrentFragment(FragmentActivity activity) {
        if (activity == null)
            return null;

        FragmentManager manager = activity.getSupportFragmentManager();
        List<Fragment> fragmentList = manager.getFragments();
        if (fragmentList != null) {
            for (int i = fragmentList.size() - 1; i > -1; i--) {
                Fragment aFragmentList = fragmentList.get(i);
                if (aFragmentList != null && aFragmentList.isVisible())
                    return aFragmentList;
            }
        }
        return null;
    }

    public static List<Fragment> getCurrentChildrenFragment(FragmentActivity activity) {
        if (activity == null)
            return null;

        Fragment currentFragment = getCurrentFragment(activity);
        if (currentFragment == null)
            return null;
        FragmentManager manager = currentFragment.getChildFragmentManager();
        List<Fragment> fragmentList = manager.getFragments();
        List<Fragment> fragVisibleList = new ArrayList<Fragment>();
        if (fragmentList != null) {
            for (int i = fragmentList.size() - 1; i > -1; i--) {
                Fragment aFragmentList = fragmentList.get(i);
                if (aFragmentList != null
                        && !aFragmentList.getClass().getName().equals(currentFragment.getClass().getName())
                        && aFragmentList.isVisible())
                    fragVisibleList.add(aFragmentList);
            }
        }

        if (fragmentList != null && fragmentList.size() > 0)
            return fragVisibleList;
        return null;
    }

    public static Fragment getCurrentChildFragment(FragmentActivity activity) {
        if (activity == null)
            return null;

        Fragment currentFragment = getCurrentFragment(activity);
        if (currentFragment == null)
            return null;
        FragmentManager manager = currentFragment.getChildFragmentManager();
        List<Fragment> fragmentList = manager.getFragments();
        if (fragmentList != null) {
            for (int i = fragmentList.size() - 1; i > -1; i--) {
                Fragment aFragmentList = fragmentList.get(i);
                if (aFragmentList != null && aFragmentList.isVisible())
                    return aFragmentList;
            }
        }
        return null;
    }


}
