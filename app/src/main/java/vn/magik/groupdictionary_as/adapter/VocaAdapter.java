package vn.magik.groupdictionary_as.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import vn.magik.groupdictionary_as.R;
import vn.magik.groupdictionary_as.entities.Vocabulary;
import vn.magik.groupdictionary_as.util.AudioHelper;
import vn.magik.groupdictionary_as.util.ImageHelper;
import vn.magik.groupdictionary_as.util.ResizableImageView;


public class VocaAdapter extends PagerAdapter {

    private Context mContext;
    private List<Vocabulary> vocaList;

    public VocaAdapter(Context mContext,
                       List<Vocabulary> vocaList) {
        this.mContext = mContext;
        this.vocaList = vocaList;
    }

    @Override
    public int getCount() {
        return vocaList.size();
    }

    @Override
    public boolean isViewFromObject(View view,
                                    Object object) {
        return view == ((ScrollView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container,
                                  int position) {

        final Vocabulary voca   = vocaList.get(position);
        final String STREAM_URL = voca.getAudio();

        // initiate
        final View view = LayoutInflater.from(mContext).
                inflate(R.layout.item_voca, container, false);
        ImageView  imgItem    = (ImageView) view.findViewById(R.id.imgItem);
        ImageView  imgAudio   = (ImageView) view.findViewById(R.id.imgAudio);
        TextView   tvName     = (TextView)  view.findViewById(R.id.tvName);
        TextView   tvSentence = (TextView)  view.findViewById(R.id.tvSentence);
        TextView   tvSpelling = (TextView)  view.findViewById(R.id.tvSpelling);
        LinearLayout lnImageVoca = (LinearLayout) view.findViewById(R.id.lnImageVoca);

        // get size screen
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)mContext).getWindowManager()
                .getDefaultDisplay().getMetrics(displaymetrics);

        // set hight for layout Linear
        int width = displaymetrics.widthPixels -
                ResizableImageView.dipToPixels(view.getContext(), 100);
        ViewGroup.LayoutParams params =
                new LinearLayout.LayoutParams(width, width);
        lnImageVoca.setLayoutParams(params);

        // set property
        tvName.setText(voca.getName());
        tvSentence.setText(voca.getSentence());
        tvSpelling.setText(voca.getSpell());

        //  set image
        String folder = String.valueOf(vocaList.get(1).getGroup());
        String pathImage = folder + "/" + voca.getImage();
        ImageHelper.getInstance(mContext).setImage(imgItem, pathImage);

        imgAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load and play audio
                String folder = String.valueOf(voca.getGroup());
                //  play audio
                AudioHelper.getInstance(mContext).play(folder,STREAM_URL );
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ScrollView) object);
    }


}