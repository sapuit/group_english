package vn.magik.groupdictionary_as.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

//import com.daimajia.androidanimations.library.Techniques;
//import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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
import vn.magik.groupdictionary_as.adapter.GroupAdapter;
import vn.magik.groupdictionary_as.database.TableGroupHelper;
import vn.magik.groupdictionary_as.download.DownloadListener;
import vn.magik.groupdictionary_as.download.FileDownloadAsync;
import vn.magik.groupdictionary_as.entities.Config;
import vn.magik.groupdictionary_as.entities.Group;
import vn.magik.groupdictionary_as.entities.ResponseConfig;
import vn.magik.groupdictionary_as.entities.ResponseGroup;
import vn.magik.groupdictionary_as.util.ApiInterface;
import vn.magik.groupdictionary_as.util.AppAnalyze;

import static vn.magik.groupdictionary_as.download.FileUtils.isValid;
import static vn.magik.groupdictionary_as.download.FileUtils.makeAppFolderIfNotExist;

import vn.magik.groupdictionary_as.util.GlobalParams;

public class MainActivity extends AppCompatActivity {

    private List<Group> groupList = null;
    GroupAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        makeAppFolderIfNotExist();
        //  Load config
        loadConfig();
        //  Load group
        if (!isValid(GlobalParams.FOLDER_ICON)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DownloadGroups();
                            if (!isValid(String.valueOf(1)))
                                DownloadVoca(1);
                        }
                    });
                }
            }).start();
        } else
            loadGroupData();
    }

    private void initView() {
        groupList = new ArrayList<Group>();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    private void loadConfig() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalParams.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInterface apiService =
                    retrofit.create(ApiInterface.class);

            Call<ResponseConfig> call = apiService.getConfig();
            call.enqueue(new Callback<ResponseConfig>() {
                @Override
                public void onResponse(Response<ResponseConfig> response,
                                       Retrofit retrofit) {

                    ResponseConfig areaResponse = response.body();

                    if (areaResponse.getErrorCode() == 0) {
                        Config config = areaResponse.getConfig();

                        //  show admod
                        GlobalParams.acti_ads = config.getActi_ads();
                        //  show dialog update
                        //  number_group > group hiện tại, clear data sql + load from server again

                        // show admod
                        addBannerAdmod();
                        loadInterstitial();

                    }
                }

                @Override
                public void onFailure(Throwable t) {
//                    Log.d("error", t.getMessage());
                    Toast.makeText(MainActivity.this,
                            "Error connect to Server.",
                            Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGroupData() {
        TableGroupHelper dbHelper = new TableGroupHelper(this);
        if (dbHelper.numberOfRows() > 0) {
            groupList = dbHelper.getAllGroup();
            setUpView();
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GlobalParams.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiInterface apiService =
                    retrofit.create(ApiInterface.class);

            Call<ResponseGroup> call = apiService.getListGroup();
            call.enqueue(new Callback<ResponseGroup>() {
                @Override
                public void onResponse(Response<ResponseGroup> response,
                                       Retrofit retrofit) {

                    ResponseGroup areaResponse = response.body();

                    if (areaResponse.getErrorCode() == 0) {
                        groupList = areaResponse.getGroupList();
                        //  save into sqlite
                        saveData();
                        //  set view
                        setUpView();
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.d("error", t.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Error connect to Server.",
                            Toast.LENGTH_LONG).show();
                }
            });
        }

        dbHelper.close();
    }

    private boolean saveData() {
        try {
            TableGroupHelper dbHelper = new TableGroupHelper(this);
            if (dbHelper.numberOfRows() == 0
                    && groupList.size() > 0) {

                for (Group group : groupList) {
                    boolean status = dbHelper.insertGroup(group);
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

    private void setUpView() {
        try {
            ListView lvGroups;
            lvGroups = (ListView) findViewById(R.id.lvGroups);
            mAdapter = new GroupAdapter(this, R.layout.item_group, handleList());
            lvGroups.setAdapter(mAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<List<Group>> handleList() {
        List<List<Group>> result = new ArrayList<>();
        int[] key = {1, 2};
        int d = 0;

        for (int i = 0; i < groupList.size(); i++) {
            List<Group> list = new ArrayList<>();

            if (key[d] == 1) {
                list.add(groupList.get(i));
                d = 1;
            } else {
                list.add(groupList.get(i));
                list.add(groupList.get(i + 1));
                i++;
                d = 0;
            }
            result.add(list);
        }
        return result;
    }

    private void DownloadGroups() {
        try {
            String url = GlobalParams.BASE_URL + "data/" + GlobalParams.FOLDER_ICON + ".zip";
            Log.i("url", url);
            FileDownloadAsync fileDownload =
                    new FileDownloadAsync(this, url, true, new DownloadListener() {
                        @Override
                        public void onStart(int minValue, int maxValue) {
                            Log.i("download", "Download Start.");
                        }

                        @Override
                        public void onSuccess(String fileName, String directory) {
                            Log.i(fileName, "Download Success.");
                        }

                        @Override
                        public void onQueue(int progress) {

                        }

                        @Override
                        public void onFail() {
                            Log.i("download", "Fail.");
                        }

                        @Override
                        public void onFinish() {
                            loadGroupData();
                            Log.i("download", "finish.");
                        }

                        @Override
                        public void onDownloadCancel() {
                        }
                    });
            fileDownload.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DownloadVoca(int pos) {
        try {
            String url = GlobalParams.BASE_URL + "data/" + String.valueOf(pos) + ".zip";
            Log.i("url", url);
            FileDownloadAsync fileDownload = new FileDownloadAsync(this, url, true, new DownloadListener() {
                @Override
                public void onStart(int minValue, int maxValue) {
                    Log.i("download", "Download Start.");
                }

                @Override
                public void onSuccess(String fileName, String directory) {
                    Log.i(fileName, "Download Success.");
                }

                @Override
                public void onQueue(int progress) {

                }

                @Override
                public void onFail() {
                    Log.i("download", "Fail.");
                }

                @Override
                public void onFinish() {
                }

                @Override
                public void onDownloadCancel() {
                }

            });
            fileDownload.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        groupList.clear();
        if (mAdapter != null)
            mAdapter.clear();
        TableGroupHelper dbHelper = new TableGroupHelper(this);
        if (dbHelper.numberOfRows() > 0) {
            groupList = dbHelper.getAllGroup();
            setUpView();
        }
        dbHelper.close();

        //  google analytic
        Tracker tracker = ((AppAnalyze) getApplication()).getDefaultTracker();
        tracker.setScreenName(getPackageName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void addBannerAdmod() {
        final AdView adView;
        if (GlobalParams.acti_ads == 1) {
            adView = new AdView(this);
            adView.setVisibility(View.GONE);
            adView.setAdUnitId(GlobalParams.AD_UNIT_ID);
            adView.setAdSize(AdSize.BANNER);
            LinearLayout layout_banner = (LinearLayout)
                    findViewById(R.id.layout_banner);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private InterstitialAd interstitialAd;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
//        initDialogRate();
//        if (showFullAdv && EnglishMainR.EQUEST_CODE == resultCode) {
            int distance = data.getIntExtra("distance", 0);
            if (distance >= 60 && GlobalParams.acti_ads == 1) {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();  // show google
                }
            }
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadInterstitial() {

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(GlobalParams.ADMOD_AD_UNIT_ID_FULL);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                AdRequest adRequest = new AdRequest.Builder().build();
                interstitialAd.loadAd(adRequest);
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }
}
