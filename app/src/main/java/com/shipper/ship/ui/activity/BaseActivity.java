package com.shipper.ship.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.shipper.ship.MyApplication;
import com.shipper.ship.utils.FragmentUtils;
import com.shipper.ship.utils.GlobalSingleton;
import com.shipper.ship.utils.Log;
import com.shipper.ship.utils.OSUtils;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Stack;

public abstract class BaseActivity extends FragmentActivity {

    private static WeakReference<BaseActivity> wrActivity = null;
    private static final String FRAGMENT_STACK_KEY = "FRAGMENT_STACK_KEY";

    private Stack<StackEntry> fragmentsStack = new Stack<StackEntry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        MyApplication.setContext(getBaseContext());

        //updateLanguage(Locale.getDefault());

        if (OSUtils.IsSupportVersionOS(Build.VERSION_CODES.ICE_CREAM_SANDWICH)) {
            onTrimMemory(TRIM_MEMORY_UI_HIDDEN);
        }
        wrActivity = new WeakReference<BaseActivity>(this);
        if (savedInstanceState == null) {
            // Init for the first time - not restore
        } else {
            Serializable serializable = savedInstanceState.getSerializable(FRAGMENT_STACK_KEY);
            if (serializable != null) {
                @SuppressWarnings("unchecked")
                List<StackEntry> arrayList = (List<StackEntry>) serializable;
                fragmentsStack = new Stack<StackEntry>();
                fragmentsStack.addAll(arrayList);
            }

            // Hide all the restored fragments instead of the last one
            if (fragmentsStack.size() > 1) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                for (int i = 0; i < fragmentsStack.size() - 1; i++) {
                    String fragTag = fragmentsStack.get(i).getFragTag();
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragTag);
                    if (fragment != null) {
                        fragmentTransaction.hide(fragment);
                    }
                }
                fragmentTransaction.commit();
            }
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment lastFragment = getLastFragment();
                if (lastFragment != null && lastFragment.isHidden()) {
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.show(lastFragment);
                    fragmentTransaction.commit();
                }
            }
        });
        initView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(FRAGMENT_STACK_KEY, fragmentsStack);
    }

    @Override
    public void onBackPressed() {
        if (!fragmentsStack.isEmpty()) {
            fragmentsStack.pop();
        }
    }

    public void putFragmentIntoStack(String tag) {
        if (fragmentsStack == null)
            fragmentsStack = new Stack<StackEntry>();
        fragmentsStack.add(new StackEntry(tag));
    }

    protected abstract void initView();

    protected abstract int getContentFrame();

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume " + this.getClass().getName());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * @param resId
     * @return
     */
    protected String getResourceString(int resId) {
        return BaseActivity.this.getResources().getString(resId);
    }

    private static class StackEntry implements Serializable {
        private static final long serialVersionUID = -6162805540320628024L;

        private String fragTag = null;

        public StackEntry(String fragTag) {
            super();
            this.fragTag = fragTag;
        }

        public String getFragTag() {
            return fragTag;
        }
    }

    private Fragment getLastFragment() {
        if (fragmentsStack.isEmpty()) return null;
        String fragTag = fragmentsStack.peek().getFragTag();
        Fragment fragment = /*FragmentUtils.getCurrentFragment(this);*/
                getSupportFragmentManager().findFragmentByTag(fragTag);
        return fragment;
    }

    public void openFragment(Class<? extends Fragment> fragmentClazz) {
        openFragment(fragmentClazz, null);
    }

    public void openFragment(Class<? extends Fragment> fragmentClazz, Bundle bundle) {
        openFragment(fragmentClazz, bundle, true);
    }

    public void openFragment(Class<? extends Fragment> fragmentClazz, Bundle bundle, boolean isAddToBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        String tag = fragmentClazz.getName();
        try {
            Fragment currentFragment = FragmentUtils.getCurrentFragment(this);
            if (!isFragmentAdded(tag)) {

                /*if (!((SplashFragment.class != null && SplashFragment.class.getName().equals(tag)))) {
                    transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out,
                            R.anim.left_in, R.anim.right_out);
                }*/

                Fragment fragment;

                fragment = fragmentClazz.newInstance();
                Log.i("KHM base activity " + tag);
                if (bundle != null)
                    fragment.setArguments(bundle);
                if (isAddToBackStack)
                    transaction.addToBackStack(tag);
                if (currentFragment != null)
                    transaction.hide(currentFragment);
                transaction.add(getContentFrame(), fragment, tag);
                if (GlobalSingleton.getInstance().isAllowStateLoss()) {
                    GlobalSingleton.getInstance().setAllowStateLoss(false);
                    transaction.commitAllowingStateLoss();
                } else {
                    transaction.commit();
                }
                Log.i("KHM base activity commit" + tag);
                // Put fragment into stack
                putFragmentIntoStack(tag);

            } else {
                showFragment(tag, transaction);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private Boolean isFragmentAdded(String tag) {
        FragmentManager manager = getSupportFragmentManager();
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
        FragmentManager manager = getSupportFragmentManager();
        List<Fragment> fragmentList = manager.getFragments();
        if (fragmentList != null)
            for (Fragment fragment : fragmentList) {
                if (fragment != null) {
                    if (fragment.getClass().getName().equals(tag)) {
                        transaction.show(fragment);
                        Log.i("KHM base activity show " + tag);
                    } else {
                        transaction.hide(fragment);
                    }
                }
            }
        transaction.commit();
    }

    /**
     * remove a fragment in stack of this activity
     */
    public void finishParentFragment(Class<?> Frag) {
        if (Frag == null)
            return;
        try {
            String tag = Frag.getName();
            FragmentManager manager, childManager;
            if ((wrActivity.get() != null)
                    && (wrActivity.get().isFinishing() != true)) {
                manager = wrActivity.get().getSupportFragmentManager();
            } else {
                manager = getSupportFragmentManager();
            }
            Fragment storedFrag = null, fragTemp;
            List<Fragment> fragmentList = manager.getFragments();
            if (fragmentList != null) {
                for (int i = fragmentList.size() - 1; i > -1; i--) {
                    fragTemp = fragmentList.get(i);
                    if (fragTemp != null && fragTemp.getClass().getName().equals(tag) && !fragTemp.isRemoving()) {
                        storedFrag = fragTemp;
                        break;
                    }
                }
            }

            /*if (storedFrag == null)
                storedFrag = manager.findFragmentByTag(tag);*/

            if (storedFrag != null) {
                childManager = storedFrag.getChildFragmentManager();
                FragmentTransaction transaction = childManager.beginTransaction();
                List<Fragment> fragmentList1 = childManager.getFragments();
                for (Fragment childFrag : fragmentList1) {
                    if (childFrag != null) {
                        transaction.remove(childFrag).detach(childFrag);
                    }
                }
                transaction.commit();
                childManager.executePendingTransactions();
                for (int i = 0; i < childManager.getBackStackEntryCount(); i++)
                    childManager.popBackStack();

                manager.beginTransaction().remove(storedFrag).commit();
                manager.executePendingTransactions();
                Log.i("finishParentFragment: " + tag);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeFragment(Class<?> parentFrag, Class<?> childFragRemove) {
        try {
            String tag = parentFrag.getName();
            FragmentManager manager, childManager;
            if ((wrActivity.get() != null)
                    && (wrActivity.get().isFinishing() != true)) {
                manager = wrActivity.get().getSupportFragmentManager();
            } else {
                manager = getSupportFragmentManager();
            }
            Fragment storedFrag = null, fragTemp;
            List<Fragment> fragmentList = manager.getFragments();
            if (fragmentList != null) {
                for (int i = fragmentList.size() - 1; i > -1; i--) {
                    fragTemp = fragmentList.get(i);
                    if (fragTemp != null && fragTemp.getClass().getName().equals(tag) && !fragTemp.isRemoving()) {
                        storedFrag = fragTemp;
                        break;
                    }
                }
            }

            /*if (storedFrag == null)
                storedFrag = manager.findFragmentByTag(tag);*/

            if (storedFrag != null) {
                childManager = storedFrag.getChildFragmentManager();
                FragmentTransaction transaction = childManager.beginTransaction();
                List<Fragment> fragmentList1 = childManager.getFragments();
                for (Fragment frag : fragmentList1) {
                    if (frag != null && frag.getClass().getName().equals(childFragRemove.getName())) {
                        transaction.remove(frag).detach(frag);
                        Log.i("finishChildFragment: " + childFragRemove.getName());
                    }
                }
                transaction.commit();
                childManager.executePendingTransactions();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
