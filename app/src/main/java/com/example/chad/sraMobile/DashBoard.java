package com.example.chad.sraMobile;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;

import com.sra.helperClasses.SyncCompare;
import com.sra.helperClasses.SyncDownlaod;
import com.sra.sliderFragments.CustomDrawerAdapter;
import com.sra.sliderFragments.DrawerItem;
import com.sra.sliderFragments.FragmentFive;
import com.sra.sliderFragments.FragmentFour;
import com.sra.sliderFragments.FragmentOne;
import com.sra.sliderFragments.FragmentThree;
import com.sra.sliderFragments.FragmentTwo;

import org.quickconnectfamily.kvkit.kv.KVStore;

import java.util.ArrayList;
import java.util.List;


public class DashBoard extends Activity {



    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;
    FragmentManager frgManager;
    String name;
    List<DrawerItem> dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        dataList = new ArrayList<DrawerItem>();
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);



        adapter = new CustomDrawerAdapter(this, R.layout.customdrawer,
                dataList,getStatusBarHeight());

        mDrawerList.setAdapter(adapter);

        dataList.add(new DrawerItem("Dash", R.drawable.dashboard));
        dataList.add(new DrawerItem("Areas",R.drawable.map));
        dataList.add(new DrawerItem("Q Sets",R.drawable.questionmark));
        dataList.add(new DrawerItem("Notes",android.R.drawable.ic_menu_edit));
        dataList.add(new DrawerItem("Stats",R.drawable.stats));





        adapter = new CustomDrawerAdapter(this, R.layout.customdrawer,
                dataList,getStatusBarHeight());

        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            SelectItem(0);
        }


    }


    public void syncDatabase(MenuItem item){
        SyncDownlaod downlaod = new SyncDownlaod(this);
                     downlaod.beginSync();
        SyncCompare compare = new SyncCompare(downlaod.getRegion());
                    compare.startCompare();

    }

    public void SelectItem(int position) {
        name = new String();
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (position) {
            case 0:
                args.putString(FragmentTwo.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentTwo.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                fragment = new FragmentTwo();
                name = "Dashboard";
                break;
            case 1:
                fragment = new FragmentOne();
                name = "Areas";
                break;
            case 2:
                fragment = new FragmentThree();
                args.putString(FragmentThree.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentThree.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                name = "Questions";
                break;
            case 3:
                fragment = new FragmentFour();
                args.putString(FragmentFour.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentFour.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                name = "Notes";
                break;
            case 4:
                fragment = new FragmentFive();
                args.putString(FragmentFive.ITEM_NAME, dataList.get(position)
                        .getItemName());
                args.putInt(FragmentFive.IMAGE_RESOURCE_ID, dataList.get(position)
                        .getImgResID());
                name = "Stats";
                break;
        }

        fragment.setArguments(args);
        frgManager = getFragmentManager();
        frgManager.beginTransaction().replace(R.id.content_frame, fragment,name)
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(dataList.get(position).getItemName());
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);


        return true;
    }

    public void logOut(MenuItem menuItem){
        try{
            KVStore.setActivity(getApplication());
            KVStore.removeValue("User");
        }catch (NullPointerException e){

        }
        Intent intent = new Intent(this,MyActivity.class);
        startActivity(intent);
    }

    public void goBack(MenuItem menuItem){

    }

    public void goToQuestionGen(MenuItem item){
        Intent intent = new Intent(this,QuestionSetList.class);
        startActivity(intent);
    }

    public class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            SelectItem(position);

        }
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onBackPressed(){
        Fragment fragment = getFragmentManager().findFragmentByTag(name);
        if(fragment.isVisible()){
            if(name.equals("Dashboard")){

            }else if(name.equals("Areas")){
            FragmentOne fragmentOne = (FragmentOne)fragment;
                if(fragmentOne.navigationPosition.equals("areas")){
                    super.onBackPressed();
                }else if(fragmentOne.navigationPosition.equals("households")){
                    fragmentOne.setAreaListeners();
                    fragmentOne.loadAreasIntoView();

                }else if(fragmentOne.navigationPosition.equals("members")){
                    fragmentOne.setHouseholdListeners();
                    fragmentOne.loadHouseholdsIntoView();
                }
            }else if(name.equals("Questions")){

            }else if(name.equals("Notes")){

            }else if(name.equals("Stats")){

            }
        }
    }

}

