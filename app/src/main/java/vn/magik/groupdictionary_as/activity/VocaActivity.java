package vn.magik.groupdictionary_as.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import vn.magik.groupdictionary_as.R;
import vn.magik.groupdictionary_as.adapter.VocaAdapter;
import vn.magik.groupdictionary_as.database.TableVocaHelper;
import vn.magik.groupdictionary_as.entities.ResponseVoca;
import vn.magik.groupdictionary_as.entities.Vocabulary;
import vn.magik.groupdictionary_as.util.ApiInterface;
import vn.magik.groupdictionary_as.util.AppAnalyze;
import vn.magik.groupdictionary_as.util.AudioHelper;
import vn.magik.groupdictionary_as.util.GlobalParams;

import static vn.magik.groupdictionary_as.download.FileUtils.isValid;

public class VocaActivity extends AppCompatActivity
        implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private List<Vocabulary> vocaList = null;
    public int pos;          //  group's position
    private int dotsCount;
    private ImageView[] dots;
    private ViewPager viewPager;
    private ImageButton btnNext;
    private ImageButton btnBack;
    private Button btnPractice;
    private VocaAdapter mAdapter;
    private ProgressBar pbVoca;
    private TableVocaHelper dbHelper;
    long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        startTime = System.currentTimeMillis();

        getIntentMain();
        addBannerAdmod();
    }

    private void getIntentMain() {
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);
    }

    private void initView() {
        btnNext = (ImageButton)
                findViewById(R.id.btnNext);
        btnBack = (ImageButton)
                findViewById(R.id.btnBack);
        btnPractice = (Button)
                findViewById(R.id.btnPractice);

        viewPager = (ViewPager)
                findViewById(R.id.pager_introduction);

        pbVoca = (ProgressBar) findViewById(R.id.pbVoca);
        pbVoca.setProgress(1);
        pbVoca.setMax(vocaList.size());
        pbVoca.getProgressDrawable().setColorFilter(
                getResources().getColor(R.color.selected),
                PorterDuff.Mode.SRC_IN);

        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnPractice.setOnClickListener(this);

        //  set adapter
        mAdapter = new VocaAdapter(this, vocaList);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(this);

        //  play aidio
        String STREAM_URL = vocaList.get(0).getAudio();
        String folder = String.valueOf(vocaList.get(0).getGroup());
        AudioHelper.getInstance(this).play(folder, STREAM_URL);

        setUiPageViewController();
    }

    private void getVocaData() {
        vocaList = new ArrayList<Vocabulary>();
        dbHelper = new TableVocaHelper(this, pos);

        if (dbHelper.numberOfRows() > 0) {
            vocaList = dbHelper.getAllVoca();
            initView();
        } else {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalParams.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInterface apiService =
                    retrofit.create(ApiInterface.class);

            Call<ResponseVoca> call = apiService.getListVoca(String.valueOf(pos));
            call.enqueue(new Callback<ResponseVoca>() {
                @Override
                public void onResponse(Response<ResponseVoca> response,
                                       Retrofit retrofit) {
                    ResponseVoca areaResponse = response.body();
                    if (areaResponse.getErrorCode() == 0) {
                        // cover data
                        vocaList = areaResponse.getVocaList();
                        if (vocaList.size() > 0) {
                            // save into sqlite
                            boolean saveStatus = saveData();
                            initView();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Plesea connect internet to download data !",
                                    Toast.LENGTH_LONG).show();
                            closeActivity();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable t) {
//                    Log.d("error", t.getMessage());
                    Toast.makeText(VocaActivity.this,
                            "Error connect to Server.",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    // set indicator circle
    private void setUiPageViewController() {
        try {
            dotsCount = mAdapter.getCount();
            dots = new ImageView[dotsCount];

            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(this);
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.dot_nonselected));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                params.setMargins(10, 0, 10, 0);
//                pager_indicator.addView(dots[i], params);
            }

            dots[0].setImageDrawable(getResources().getDrawable(
                    R.drawable.dot_selected));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNext:
                int itemIndex = (viewPager.getCurrentItem() < dotsCount)
                        ? viewPager.getCurrentItem() + 1 : 0;
                viewPager.setCurrentItem(itemIndex);
                break;

            case R.id.btnBack:
                int itemIndex1 = (viewPager.getCurrentItem() > -1)
                        ? viewPager.getCurrentItem() - 1 : 0;
                viewPager.setCurrentItem(itemIndex1);
                break;

            case R.id.btnPractice:
                startTestActivity();
                break;
        }
    }

    private void startTestActivity() {
        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra("vocaList", (ArrayList<Vocabulary>) vocaList);
        startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position,
                               float positionOffset,
                               int positionOffsetPixels) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(
                    R.drawable.dot_nonselected));
        }

        dots[position].setImageDrawable(getResources().getDrawable(
                R.drawable.dot_selected));

        if (position + 1 == dotsCount) {
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }

        if (position  == 0) {
            btnBack.setVisibility(View.INVISIBLE);
        } else {
            btnBack.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageSelected(int position) {
        String STREAM_URL = vocaList.get(position).getAudio();
        int itemIndex = (viewPager.getCurrentItem() < dotsCount)
                ? viewPager.getCurrentItem() + 1 : 0;
        pbVoca.setProgress(position + 1);
        String folder = String.valueOf(
                vocaList.get(position).getGroup());
        //  play aidio
        AudioHelper.getInstance(this).play(folder, STREAM_URL);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private boolean saveData() {
        try {
            if (dbHelper.numberOfRows() == 0
                    && vocaList.size() > 0) {

                for (Vocabulary voca : vocaList) {
                    boolean status = dbHelper.insertVoca(voca);
                    if (!status)
                        return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private AdView adView;

    private void addBannerAdmod() {
        if (GlobalParams.acti_ads == 1) {
            adView = new AdView(this);
            adView.setVisibility(View.GONE);
            adView.setAdUnitId(GlobalParams.AD_UNIT_ID);
            adView.setAdSize(AdSize.BANNER);

            LinearLayout layout_banner_voca =
                    (LinearLayout) findViewById(R.id.layout_banner_voca);
            layout_banner_voca.addView(adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    adView.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isValid(String.valueOf(pos)))
            getVocaData();

        //  google analytic
        Tracker tracker = ((AppAnalyze) getApplication()).getDefaultTracker();
        tracker.setScreenName(getPackageName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void closeActivity() {
        Intent returnIntent = new Intent();
        int diff = Math.round((System.currentTimeMillis() - startTime) / 600);

        returnIntent.putExtra("distance", diff);
        setResult(1000, returnIntent);
        finish();
    }
}
