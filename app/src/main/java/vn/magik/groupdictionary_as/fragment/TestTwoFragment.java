package vn.magik.groupdictionary_as.fragment;


import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import vn.magik.groupdictionary_as.R;
import vn.magik.groupdictionary_as.activity.TestActivity;
import vn.magik.groupdictionary_as.activity.VocaActivity;
import vn.magik.groupdictionary_as.entities.Vocabulary;
import vn.magik.groupdictionary_as.util.AudioHelper;
import vn.magik.groupdictionary_as.util.ImageHelper;
import vn.magik.groupdictionary_as.util.ResizableImageView;


public class TestTwoFragment extends Fragment
        implements TextWatcher, View.OnClickListener {

    public boolean result = false;
    private Vocabulary vocaKey;
    public EditText etNameTest2;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(
                R.layout.fragment_test_two, container, false);

        initiateView(view);
        return view;
    }

    private void initiateView(View view) {

        //  initiate view
        ImageView imgTest2 = (ImageView)
                view.findViewById(R.id.imgTest2);
        ImageView imgAudio = (ImageView)
                view.findViewById(R.id.imgAudio);
        etNameTest2 = (EditText)
                view.findViewById(R.id.etNameTest2);
        LinearLayout lnImage2 = (LinearLayout)
                view.findViewById(R.id.lnImage2);

        result = false;
        TestActivity.visialbleBtnSubmit(false);
        //  get size screen
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displaymetrics);

        //  set hight for layout Linear
        int width = displaymetrics.widthPixels -
                ResizableImageView.dipToPixels(view.getContext(), 110);
        ViewGroup.LayoutParams params =
                new LinearLayout.LayoutParams(width, width);
        lnImage2.setLayoutParams(params);

        vocaKey = TestActivity.vocaList.get(TestActivity.vocaIndex);

        //  set image
        String folder = String.valueOf(vocaKey.getGroup());
        ImageHelper.getInstance(view.getContext()).setImage(
                imgTest2, folder + "/" + vocaKey.getImage());

        etNameTest2.addTextChangedListener(this);
        imgAudio.setOnClickListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        s = s.toString().trim();

        if (s.equals(""))
            TestActivity.visialbleBtnSubmit(false);
        else
            TestActivity.visialbleBtnSubmit(true);

        if (s.toString().equals(vocaKey.getName().toLowerCase())) {
            result = true;
        } else result = false;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imgAudio) {
            String folder = String.valueOf(vocaKey.getGroup());
            AudioHelper.getInstance(
                    v.getContext())
                    .play(folder, vocaKey.getAudio());
        }
    }

    public void showResult() {
        etNameTest2.setEnabled(false);
//        etNameTest2.setTextColor(Color.parseColor("#64DD17"));
        etNameTest2.setText(vocaKey.getName());
        etNameTest2.setTypeface(null, Typeface.BOLD);
    }

    public boolean getResult() {
        etNameTest2.setEnabled(false);
        return result;
    }
}