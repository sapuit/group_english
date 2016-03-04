package vn.magik.groupdictionary_as.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.magik.groupdictionary_as.R;
import vn.magik.groupdictionary_as.database.TableGroupHelper;
import vn.magik.groupdictionary_as.entities.Vocabulary;
import vn.magik.groupdictionary_as.fragment.TestFourFragment;
import vn.magik.groupdictionary_as.fragment.TestOneFragment;
import vn.magik.groupdictionary_as.fragment.TestThreeFragment;
import vn.magik.groupdictionary_as.fragment.TestTwoFragment;
import vn.magik.groupdictionary_as.notice.OptAnimationLoader;
import vn.magik.groupdictionary_as.notice.SuccessTickView;
import vn.magik.groupdictionary_as.util.AppAnalyze;
import vn.magik.groupdictionary_as.util.AudioHelper;
import vn.magik.groupdictionary_as.util.GlobalParams;
import vn.magik.groupdictionary_as.util.RandomUtil;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class TestActivity extends AppCompatActivity {

    public static Button btnSubmit;
    public static int vocaIndex;
    public static List<Vocabulary> vocaList;

    private FragmentManager fm;
    //    private FragmentTransaction ft;
    private int fmIndex;
    private int incorrect;
    private TextView tvResult;
    private TextView tvAnswer;
    private LinearLayout lnShowResult;
    private boolean resultTest = false;

    //    private int dotsCount;
    private ImageView[] dots;
    private long startTime;
//    private LinearLayout pager_indicator;

    //  set notice
    private FrameLayout mErrorFrame;
    private FrameLayout mSuccessFrame;
    private ImageView mErrorX;
    private SuccessTickView mSuccessTick;
    private Animation mSuccessBowAnim;
    private Animation mErrorInAnim;
    private View mSuccessRightMask;
    private View mSuccessLeftMask;
    private AnimationSet mSuccessLayoutAnimSet;
    private AnimationSet mErrorXInAnim;

    //  level
    ImageView heart1, heart2, heart3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        startTime = System.currentTimeMillis();

        getDataIntent();
        initView();
        addBannerAdmod();
        analytic();
    }

    private void getDataIntent() {
        Intent intent = getIntent();

        vocaList = new ArrayList<Vocabulary>();
        vocaList = (ArrayList<Vocabulary>)
                intent.getSerializableExtra("vocaList");

        //  change pos in vocaImgList
        Collections.shuffle(vocaList);
    }

    public void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_test);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Practice");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        heart1 = (ImageView) findViewById(R.id.heart1);
        heart2 = (ImageView) findViewById(R.id.heart2);
        heart3 = (ImageView) findViewById(R.id.heart3);

        tvResult = (TextView) findViewById(R.id.tvResult);
        tvAnswer = (TextView) findViewById(R.id.tvAnswer);
        lnShowResult = (LinearLayout) findViewById(R.id.lnShowResult);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        LinearLayout pager_indicator = (LinearLayout)
                findViewById(R.id.viewPagerCountDots);

        mErrorInAnim = OptAnimationLoader.loadAnimation(getApplicationContext(), R.anim.error_frame_in);
        mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getApplicationContext(), R.anim.error_x_in);
        mSuccessBowAnim = OptAnimationLoader.loadAnimation(getApplicationContext(), R.anim.success_bow_roate);
        mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(getApplicationContext(), R.anim.success_mask_layout);

        mErrorFrame = (FrameLayout) findViewById(R.id.error_frame);
        mSuccessFrame = (FrameLayout) findViewById(R.id.success_frame);
        mErrorX = (ImageView) mErrorFrame.findViewById(R.id.error_x);
        mSuccessTick = (SuccessTickView) mSuccessFrame.findViewById(R.id.success_tick);
        mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
        mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitClick();
            }
        });

        vocaIndex = 0;
        incorrect = 0;
        //  set indicate test
        int dotsCount = vocaList.size();
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.dot_nonselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);
            pager_indicator.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.dot_selected));

        //  first test
        fm = getSupportFragmentManager();
        selectTest();
    }

    private void submitClick() {
        btnSubmit.setFocusable(true);

        if (incorrect == 3) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setMessage("You picked the wrong 3 times !")
                    .setCancelable(false)

                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            vocaIndex = 0;
                            finish();
                        }
                    })
                    .setPositiveButton("Practice again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            TestActivity.this.recreate();
                        }
                    });

            android.app.AlertDialog alert = builder.create();
            alert.show();
        } else if (vocaIndex == vocaList.size()) {     //  Finish test
            vocaIndex = 0;

            // save group is finished
            int idGroup = vocaList.get(1).getGroup();
            try {
                TableGroupHelper dbHelper = new TableGroupHelper(this);
                boolean status = dbHelper.groupFinish(idGroup);
                closeActivity();
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                closeActivity();
            }

        } else if (btnSubmit.getText().equals("SUBMIT")) {
            btnSubmit.setText("NEXT");
            showNotice(checkResult());

            if (vocaIndex == vocaList.size() - 1) {
                btnSubmit.setText("FINISH");
                vocaIndex++;
            }
            return;
        } else
            nextTest();
    }

    private void closeActivity() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    //  check result from fragment 1,2 or 3
    private boolean checkResult() {
        String currentFragmentTag = fm.getBackStackEntryAt(
                fm.getBackStackEntryCount() - 1).getName();
        Fragment frag = fm.findFragmentByTag(currentFragmentTag);

        if (frag == null) {
            Log.i("frag", null);
        }

        try {
            if (fmIndex == 1) {
                return ((TestOneFragment) frag).getResult();
            }
            if (fmIndex == 2) {
                return ((TestTwoFragment) frag).getResult();
            }
            return ((TestThreeFragment) frag).getResult();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showNotice(boolean b) {
        resultTest = b;
        String currentFragmentTag = fm.getBackStackEntryAt(
                fm.getBackStackEntryCount() - 1).getName();
        Fragment frag = getSupportFragmentManager()
                .findFragmentByTag(currentFragmentTag);

        if (frag == null)
            return;
        //  show result
        if (fmIndex == 1) {
            ((TestOneFragment) frag).showResult(this);
        }
        if (fmIndex == 3) {
            ((TestThreeFragment) frag).showResult(this);
        }

        //  play audio result
        String urlAudio = vocaList.get(vocaIndex).getAudio();
        String folder = String.valueOf(vocaList.get(vocaIndex).getGroup());
        AudioHelper.getInstance(this).play(folder, urlAudio);

        restoreNotice();
        if (b) {
            tvResult.setText("Correct :) ");
            tvAnswer.setVisibility(View.GONE);

            tvResult.setTextColor(
                    getResources().getColor(R.color.success_stroke_color));

            mSuccessFrame.setVisibility(View.VISIBLE);
            // initial rotate layout of success mask
            mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
            mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
            playAnimation(1);

        } else {
            mErrorFrame.setVisibility(View.VISIBLE);

            incorrect++;
            killLife();

            playAnimation(2);
            tvResult.setText("Incorrect :( ");
            tvResult.setTextColor(getResources().getColor(R.color.error_stroke_color));

            if (fmIndex == 2) {
                tvResult.setText("Incorrect :( ");
                tvAnswer.setVisibility(View.VISIBLE);
                tvAnswer.setText("Correct answer : " + vocaList.get(vocaIndex).getName());
            } else
                tvAnswer.setVisibility(View.GONE);
        }

        lnShowResult.setVisibility(View.VISIBLE);
    }

    private void killLife() {
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        switch (incorrect) {
            case 1:
                if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    YoYo.with(Techniques.FlipOutY)
                            .duration(1000)
                            .playOn(heart1);
                } else
                    heart1.setVisibility(View.INVISIBLE);
                break;
            case 2:
                if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    YoYo.with(Techniques.FlipOutY)
                            .duration(1000)
                            .playOn(heart2);
                } else
                    heart2.setVisibility(View.INVISIBLE);
                break;
            case 3:
                if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    YoYo.with(Techniques.FlipOutY)
                            .duration(1000)
                            .playOn(heart3);
                } else
                    heart3.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void nextTest() {
        try {
            lnShowResult.setVisibility(View.INVISIBLE);
            restoreNotice();
            selectTest();

            vocaIndex++;
            btnSubmit.setText("SUBMIT");

            int idDotResult;
            if (resultTest)
                idDotResult = R.drawable.dot_correct;
            else idDotResult = R.drawable.dot_incorrect;

            if (vocaIndex != 0)
                dots[vocaIndex - 1].setImageDrawable(getResources().getDrawable(idDotResult));
            dots[vocaIndex].setImageDrawable(getResources().getDrawable(R.drawable.dot_selected));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectTest() {
        FragmentTransaction ft = fm.beginTransaction();
        fmIndex = RandomUtil.run(1, 3);

        switch (fmIndex) {
            case 1:
                TestOneFragment fragment1 = new TestOneFragment();
                ft.replace(R.id.fmContainer, fragment1, String.valueOf(vocaIndex));
                break;
            case 2:
                TestTwoFragment fragment2 = new TestTwoFragment();
                ft.replace(R.id.fmContainer, fragment2, String.valueOf(vocaIndex));
                break;
            case 3:
                TestThreeFragment fragment3 = new TestThreeFragment();
                ft.replace(R.id.fmContainer, fragment3, String.valueOf(vocaIndex));
                break;
            case 4:
                TestFourFragment fragment4 = new TestFourFragment();
                ft.replace(R.id.fmContainer, fragment4, String.valueOf(vocaIndex));
                break;
        }

        ft.addToBackStack(String.valueOf(vocaIndex));
        ft.commit();
    }

    public static void visialbleBtnSubmit(boolean b) {
        if (b) { //  true : visiable
            btnSubmit.setTextColor(
                    btnSubmit.getContext()
                            .getResources()
                            .getColor(R.color.white));

            btnSubmit.setEnabled(true);
        } else {//   invisiable
            //  set hide button submitClick
            btnSubmit.setTextColor(
                    btnSubmit.getContext()
                            .getResources()
                            .getColor(R.color.hide_text));
            btnSubmit.setEnabled(false);
        }
    }

    private void restoreNotice() {
        mErrorFrame.setVisibility(View.GONE);
        mSuccessFrame.setVisibility(View.GONE);

//        mConfirmButton.setBackgroundResource(R.drawable.blue_button_background);
        mErrorFrame.clearAnimation();
        mErrorX.clearAnimation();
        mSuccessTick.clearAnimation();
        mSuccessLeftMask.clearAnimation();
        mSuccessRightMask.clearAnimation();
    }

    private void playAnimation(int mAlertType) {
        if (mAlertType == 2) { //    ERROR_TYPE
            mErrorFrame.startAnimation(mErrorInAnim);
            mErrorX.startAnimation(mErrorXInAnim);
        } else if (mAlertType == 1) { // SUCCESS_TYPE
            mSuccessTick.startTickAnim();
            mSuccessRightMask.startAnimation(mSuccessBowAnim);
        }
    }

    private void addBannerAdmod() {
        if (GlobalParams.acti_ads == 1) {
            final AdView adView = new AdView(this);
            adView.setVisibility(View.GONE);
            adView.setAdUnitId(GlobalParams.AD_UNIT_ID);
            adView.setAdSize(AdSize.BANNER);

            LinearLayout layout_banner =
                    (LinearLayout) findViewById(R.id.layout_banner_test);
            layout_banner.addView(adView);
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

    private void analytic() {
        //  google analytic
        Tracker tracker = ((AppAnalyze) getApplication()).getDefaultTracker();
        tracker.setScreenName(getPackageName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        vocaIndex = 0;
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }
}
