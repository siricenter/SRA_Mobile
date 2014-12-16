package com.example.chad.sraMobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sra.objects.Areas;
import com.sra.objects.Households;
import com.sra.objects.Question;
import com.sra.objects.QuestionSet;
import com.sra.objects.QuestionSetBank;
import com.sra.objects.Region;

import org.quickconnectfamily.json.JSONException;
import org.quickconnectfamily.json.JSONUtilities;
import org.quickconnectfamily.kvkit.kv.KVStore;

import java.util.ArrayList;

public class DataGather extends FragmentActivity {

    private int numQuestions;
    private QuestionSet questionSet;

    private TextView progressView;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private Region region;
    private Households household;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_gather);

        Intent intent = getIntent();
        int area = intent.getIntExtra("area", 0);
        int house = intent.getIntExtra("household", 0);
        if (area < 0) area = 0;
        if (house < 0) house = 0;
        getRegion(area, house);
        String questionSetName = intent.getStringExtra("questionSetName");
        if (household != null) {
            questionSet = household.getQuestionSet(questionSetName);
        }
        else {
            questionSet = new QuestionSet("", "");
        }

        questionSet = QuestionSetBank.getQuestionSet(questionSetName);
        if (questionSet == null) {
            questionSet = new QuestionSet("", "");
        }
        numQuestions = questionSet.getQuestions().size();

        progressView = (TextView) findViewById(R.id.question_progress_view);
        TextView pageTitle = (TextView) findViewById(R.id.question_header_view);
        pageTitle.setText(questionSet.getName());

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.question_view_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                sliderChanged(position);
            }
        });

        sliderChanged(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.data_gather, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            save();
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void done(MenuItem mi) {
        save();
        finish();
    }

    public void save() {
        try {
            KVStore.storeValue("Field", region);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not save region");
        }
    }

    public void sliderChanged(int position) {
        String newLabel = "" + (position + 1) + "/" + numQuestions;
        progressView.setText(newLabel);
    }

    public Question getQuestion(int index) {
        return questionSet.getQuestions().get(index);
    }

    public void getRegion(int area, int house) {
        region = null;
        try {
            String json = JSONUtilities.stringify(KVStore.getValue("Field"));
            System.out.println("Loading Areas");
            System.out.println(json);
            Gson gson = new GsonBuilder().create();
            region = gson.fromJson(json,Region.class);

            household = null;
            ArrayList<Areas> areas = region.getAreas();
            if (areas.size() > 0) {
                ArrayList<Households> households = areas.get(area).getHouseholds();
                if (households.size() > house) {
                    household = households.get(house);
                }
            }
            if (household == null) {
                household = new Households();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("Nothing Here");
        }
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle arguments = new Bundle();
            arguments.putInt("questionIndex", position);
            DataGatherQuestionFragment fragment = new DataGatherQuestionFragment();
            fragment.setArguments(arguments);
            return fragment;
        }

        @Override
        public int getCount() {
            return numQuestions;
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
