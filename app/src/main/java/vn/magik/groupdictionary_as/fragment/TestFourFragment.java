package vn.magik.groupdictionary_as.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.magik.groupdictionary_as.R;

/**
 * Created by Administrator on 2/27/2016.
 */
public class TestFourFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test_four, null);
        initiateView(view);

        return view;
    }

    private void initiateView(View view) {
    }
}
