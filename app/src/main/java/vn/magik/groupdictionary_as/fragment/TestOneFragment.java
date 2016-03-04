package vn.magik.groupdictionary_as.fragment;


import android.content.Context;
import android.os.Bundle;
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
import vn.magik.groupdictionary_as.activity.VocaActivity;
import vn.magik.groupdictionary_as.entities.Vocabulary;
import vn.magik.groupdictionary_as.util.ImageHelper;
import vn.magik.groupdictionary_as.util.RandomUtil;
import vn.magik.groupdictionary_as.util.ResizableImageView;


public class TestOneFragment extends Fragment {

    private TextView tvNameTest1;
    private ImageView img1, img2, img3, img4;
    private LinearLayout boderImg1;
    private LinearLayout boderImg2;
    private LinearLayout boderImg3;
    private LinearLayout boderImg4;
    public  List<Vocabulary> vocaImgList;
    private int posSelected = 0;
    public  Vocabulary vocaKey;
    public  boolean result = false;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_one, null);
        initiateView(view);

        return view;
    }

    private void initiateView(View view) {

        img1 = (ImageView) view.findViewById(R.id.img1);
        img2 = (ImageView) view.findViewById(R.id.img2);
        img3 = (ImageView) view.findViewById(R.id.img3);
        img4 = (ImageView) view.findViewById(R.id.img4);
        boderImg1   = (LinearLayout) view.findViewById(R.id.boderImg1);
        boderImg2   = (LinearLayout) view.findViewById(R.id.boderImg2);
        boderImg3   = (LinearLayout) view.findViewById(R.id.boderImg3);
        boderImg4   = (LinearLayout) view.findViewById(R.id.boderImg4);
        tvNameTest1 = (TextView) view.findViewById(R.id.tvNameTest1);
        ViewGroup lnImage = (ViewGroup) view.findViewById(R.id.lnImage);
        TestActivity.visialbleBtnSubmit(false);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImage(v);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImage(v);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImage(v);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImage(v);
            }
        });

        result = false;
        // get size screen
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        // set hight for layout Linear
        int width = displaymetrics.widthPixels -
                ResizableImageView.dipToPixels(view.getContext(), 40);
        ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(width, width);
        lnImage.setLayoutParams(params);

        // set image random
        setupImageRandom(view);
    }

    private void setupImageRandom(View view) {

        vocaImgList = new ArrayList<>();
        vocaKey = TestActivity.vocaList.get(TestActivity.vocaIndex);
        vocaImgList.add(vocaKey);

        int i = 0;
        while (i < 3) {
            //  get random number in vocaList size
            int pos = RandomUtil.run(0, TestActivity.vocaList.size() - 1);
            Vocabulary voca = TestActivity.vocaList.get(pos);
            //  check contain in vocaImgList
            if (!vocaImgList.contains(voca)) {   //  if it haven't, add  vocaImgList
                vocaImgList.add(voca);
                i++;
            }
        }

        // set name
        tvNameTest1.setText(vocaKey.getName() + " ?");
        //  change pos in vocaImgList
        Collections.shuffle(vocaImgList);

        setImage(view, img1, vocaImgList.get(0));
        setImage(view, img2, vocaImgList.get(1));
        setImage(view, img3, vocaImgList.get(2));
        setImage(view, img4, vocaImgList.get(3));
    }

    private void setImage(View view, ImageView imgView,
                          Vocabulary voca) {
        String folder = String.valueOf(voca.getGroup());
        ImageHelper.getInstance(
                view.getContext()).setImage(
                imgView, folder + "/" + voca.getImage());
    }

    public void clickImage(View v) {
        TestActivity.visialbleBtnSubmit(true);
        switch (v.getId()) {
            case R.id.img1:
                setImageSelected(boderImg1, boderImg2, boderImg3, boderImg4);
                posSelected = 0;
                break;
            case R.id.img2:
                setImageSelected(boderImg2, boderImg1, boderImg3, boderImg4);
                posSelected = 1;
                break;
            case R.id.img3:
                setImageSelected(boderImg3, boderImg2, boderImg1, boderImg4);
                posSelected = 2;
                break;
            case R.id.img4:
                setImageSelected(boderImg4, boderImg2, boderImg3, boderImg1);
                posSelected = 3;
                break;
        }
        setResult(posSelected);
    }


    private void setResult(int pos) {
        if (vocaImgList.get(pos).equals(vocaKey))
            result = true;
        else result = false;
    }

    public void showResult(Context context) {
        int index = vocaImgList.indexOf(vocaKey);
        LinearLayout key = boderImg1;
        switch (index) {
            case 0:
                key = boderImg1;
                break;
            case 1:
                key = boderImg2;
                break;
            case 2:
                key = boderImg3;
                break;
            case 3:
                key = boderImg4;
                break;
        }
        setBoderResult(context, key);
        img1.setOnClickListener(null);
        img2.setOnClickListener(null);
        img3.setOnClickListener(null);
        img4.setOnClickListener(null);

    }

    public void setBoderResult(Context context,
                               LinearLayout ln) {
        ln.setBackgroundDrawable(
                context.getResources()
                        .getDrawable(R.drawable.bd_correct));
        if (!result) {
            setImageIncorrect();
        }

    }

    private void setImageSelected(LinearLayout select,
                                  LinearLayout ln2,
                                  LinearLayout ln3,
                                  LinearLayout ln4) {
        ln2.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_image));
        ln3.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_image));
        ln4.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_image));
        select.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_image_select));
    }

    private void setImageIncorrect() {
        if (posSelected == 0) {
            boderImg1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_incorrect));
        }
        if (posSelected == 1) {
            boderImg2.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_incorrect));
        }
        if (posSelected == 2) {
            boderImg3.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_incorrect));
        }
        if (posSelected == 3) {
            boderImg4.setBackgroundDrawable(getResources().getDrawable(R.drawable.bd_incorrect));
        }
    }

    public boolean getResult() {
        return result;
    }
}
