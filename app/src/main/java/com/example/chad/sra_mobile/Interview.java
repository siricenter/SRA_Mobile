package com.example.chad.sra_mobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.ActionBar;
import android.app.Fragment;

public class Interview extends Activity {

    Fragment nutritionTab = new NutritionTab();
    Fragment agronomyTab  = new AgronomyTab();
    Fragment sasTab       = new SASTab();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interview);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab1 = actionBar.newTab().setText("Nutrition");
        ActionBar.Tab tab2 = actionBar.newTab().setText("Agronomy");
        ActionBar.Tab tab3 = actionBar.newTab().setText("Small Animal Science");
        tab1.setTabListener(new InterviewTabListener(nutritionTab));
        tab2.setTabListener(new InterviewTabListener(agronomyTab));
        tab3.setTabListener(new InterviewTabListener(sasTab));
        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.interview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
