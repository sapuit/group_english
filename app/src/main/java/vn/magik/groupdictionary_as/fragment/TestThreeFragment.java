package vn.magik.groupdictionary_as.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vn.magik.groupdictionary_as.R;
import vn.magik.groupdictionary_as.activity.TestActivity;
import vn.magik.groupdictionary_as.entities.Vocabulary;
import vn.magik.groupdictionary_as.util.ImageHelper;
import vn.magik.groupdictionary_as.util.RandomUtil;
import vn.magik.groupdictionary_as.util.ResizableImageView;

public class TestThreeFragment extends Fragment
        implements View.OnClickListener {

    public boolean result = false;
    TextView tvA, tvB, tvC, tvD;
    ImageView imgTest3;
    Vocabulary vocaKey;
    List<Vocabulary> vocaListRamdom;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_three, null);

        initiateView(view);
        return view;
    }

    private void initiateView(View view) {
        LinearLayout lnImage3 = (LinearLayout) view.findViewById(R.id.lnImage3);
        imgTest3 = (ImageView)
                view.findViewById(R.id.imgTest3);
        tvA = (TextView) view.findViewById(R.id.tvA);
        tvB = (TextView) view.findViewById(R.id.tvB);
        tvC = (TextView) view.findViewById(R.id.tvC);
        tvD = (TextView) view.findViewById(R.id.tvD);


        tvA.setOnClickListener(this);
        tvB.setOnClickListener(this);
        tvC.setOnClickListener(this);
        tvD.setOnClickListener(this);

//        lnNameText3.setOnClickListener(this);

        TestActivity.visialbleBtnSubmit(false);

        vocaKey = TestActivity.vocaList.get(TestActivity.vocaIndex);

        //  get size screen
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displaymetrics);

        //  set hight for layout Linear
        int width = displaymetrics.widthPixels -
                ResizableImageView.dipToPixels(view.getContext(), 150);
        ViewGroup.LayoutParams params =
                new LinearLayout.LayoutParams(width, width);
        lnImage3.setLayoutParams(params);

        String folder = String.valueOf(vocaKey.getGroup());
        ImageHelper.getInstance(view.getContext()).setImage(
                imgTest3, folder + "/" + vocaKey.getImage());
        result = false;

        //  set result A,B,C random
        vocaListRamdom = new ArrayList<Vocabulary>();
        vocaListRamdom.add(vocaKey);
        int i = 0;
        while (i < 3) {
            //  get random number in vocaList size
            int pos = RandomUtil.run(0, TestActivity.vocaList.size() - 1);
            Vocabulary voca = TestActivity.vocaList.get(pos);
            //  check contain in vocaImgList
            if (!vocaListRamdom.contains(voca)) {   //  if it haven't, add  vocaImgList
                vocaListRamdom.add(voca);
                i++;
            }
        }
        Collections.shuffle(vocaListRamdom);

        tvA.setText(vocaListRamdom.get(0).getName());
        tvB.setText(vocaListRamdom.get(1).getName());
        tvC.setText(vocaListRamdom.get(2).getName());
        tvD.setText(vocaListRamdom.get(3).getName());
    }

    int posSelected;

    @Override
    public void onClick(View v) {
        TestActivity.visialbleBtnSubmit(true);

        switch (v.getId()) {
            case R.id.tvA:
                selected(tvA, tvB, tvC, tvD);
                posSelected = 0;
                break;

            case R.id.tvB:
                selected(tvB, tvA, tvC, tvD);
                posSelected = 1;
                break;

            case R.id.tvC:
                selected(tvC, tvB, tvA, tvD);
                posSelected = 2;
                break;
            case R.id.tvD:
                selected(tvD, tvB, tvC, tvA);
                posSelected = 3;
                break;
        }
    }


    private void selected(TextView select, TextView et1, TextView et2, TextView et3) {
        et1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_text));
        et2.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_text));
        et3.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_text));
        select.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_text_selected));
    }

//    public void showResult() {
//        etNameTest3.setText(vocaKey.getName());
////        etNameTest3.setTextColor(Color.parseColor("#64DD17"));
//        etNameTest3.setEnabled(false);
//        etNameTest3.setTypeface(null, Typeface.BOLD);
//    }

    public boolean getResult() {
        setResult(posSelected);
        return result;
    }

    private void setResult(int pos) {
        if (vocaListRamdom.get(pos).equals(vocaKey))
            result = true;
        else result = false;
    }

    public void showResult(Context context) {
        int index = vocaListRamdom.indexOf(vocaKey);
        TextView key = tvA;
        switch (index) {
            case 0:
                key = tvA;
                break;
            case 1:
                key = tvB;
                break;
            case 2:
                key = tvC;
                break;
            case 3:
                key = tvD;
                break;
        }

        setBoderResult(context, key);
        tvA.setOnClickListener(null);
        tvB.setOnClickListener(null);
        tvC.setOnClickListener(null);
        tvD.setOnClickListener(null);
    }

    private void setBoderResult(Context context, TextView key) {
        key.setBackgroundDrawable(
                context.getResources()
                        .getDrawable(R.drawable.bd_text_correct));
        if (!result) {
            setImageIncorrect();
        }
    }

    private void setImageIncorrect() {
        if (posSelected == 0) {
            tvA.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_text_incorrect));
        }
        if (posSelected == 1) {
            tvB.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_text_incorrect));
        }
        if (posSelected == 2) {
            tvC.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_text_incorrect));
        }
        if (posSelected == 3) {
            tvD.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_text_incorrect));
        }
    }
}
