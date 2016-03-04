package vn.magik.groupdictionary_as.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import vn.magik.groupdictionary_as.R;
import vn.magik.groupdictionary_as.activity.MainActivity;
import vn.magik.groupdictionary_as.activity.VocaActivity;
import vn.magik.groupdictionary_as.download.DownloadListener;
import vn.magik.groupdictionary_as.download.FileDownloadAsync;
import vn.magik.groupdictionary_as.entities.Group;
import vn.magik.groupdictionary_as.util.GlobalParams;
import vn.magik.groupdictionary_as.util.ImageHelper;

import static vn.magik.groupdictionary_as.download.FileUtils.isValid;

public class GroupAdapter extends ArrayAdapter<List<Group>> {

    private List<List<Group>> groupsList = null;
    private Context context = null;
    int layoutId;

    public GroupAdapter(Context context, int layoutId, List<List<Group>> groupsList) {
        super(context, layoutId, groupsList);
        this.layoutId = layoutId;
        this.context = context;
        this.groupsList = groupsList;
    }

    static class ViewHolder {
        public TextView tvNameChild1, tvNameChild2;
        public CircularImageView imageChild1, imageChild3;
        public ImageView ivOk1,ivOk2;
        public LinearLayout lnGroup2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final ViewHolder holder; // to reference the child views for later actions

        if (v == null) {
            v = LayoutInflater.from(context).inflate(R.layout.item_group, null, false);
            // cache view fields into the holder
            holder = new ViewHolder();
            holder.tvNameChild1 = (TextView) v.findViewById(R.id.tvNameChild1);
            holder.tvNameChild2 = (TextView) v.findViewById(R.id.tvNameChild2);
            holder.imageChild1 = (CircularImageView) v.findViewById(R.id.imageChild1);
            holder.imageChild3 = (CircularImageView) v.findViewById(R.id.imageChild2);
            holder.ivOk1 = (ImageView) v.findViewById(R.id.ivOk1);
            holder.ivOk2 = (ImageView) v.findViewById(R.id.ivOk2);
            holder.lnGroup2 = (LinearLayout) v.findViewById(R.id.lnGroup2);
            // associate the holder with the view for later lookup
            v.setTag(holder);
        } else {
            // view already exists, get the holder instance from the view
            holder = (ViewHolder) v.getTag();
        }

        List<Group> groups = groupsList.get(position);
        holder.lnGroup2.setVisibility(View.VISIBLE);
        holder.ivOk1.setVisibility(View.INVISIBLE);
        holder.ivOk2.setVisibility(View.INVISIBLE);

        if (groups.size() == 1) {
            Group group = groups.get(0);
            setItem(group, holder.tvNameChild1, holder.imageChild1);
            holder.lnGroup2.setVisibility(View.GONE);

            if (group.getFinish().equals("true")) {
                holder.ivOk1.setVisibility(View.VISIBLE);
            }

        } else {
            Group group = groups.get(0);
            setItem(group, holder.tvNameChild1, holder.imageChild1);

            if (group.getFinish().equals("true")) {
                holder.ivOk1.setVisibility(View.VISIBLE);
            }

            Group group2 = groups.get(1);
            setItem(group2, holder.tvNameChild2, holder.imageChild3);

            if (group2.getFinish().equals("true")) {
                holder.ivOk2.setVisibility(View.VISIBLE);
            }
        }
        return v;
    }

    private void setItem(final Group group, TextView tv, final CircularImageView iv) {

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            YoYo.with(Techniques.ZoomIn)
                .duration(1000)
                .playOn(iv);
        }

        tv.setText(group.getName());
        ImageHelper.getInstance(context).setImage(iv, "icon/" + group.getImage());
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickImage(iv, group.getId());
            }
        });
    }

    public void clickImage(final CircularImageView imageView,
                           final int pos) {

        imageView.setBorderColor(context.getResources().getColor(R.color.selected));
        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setBorderColor(context.getResources().getColor(R.color.white));
                if (!isValid(String.valueOf(pos))) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DownloadVoca(pos);
                                }
                            });
                        }
                    }).start();
                } else
                    moveVocaActivity(pos);
            }
        }, 400);


    }

    private void DownloadVoca(final int pos) {
        try {
            String url = GlobalParams.BASE_URL + "data/" + String.valueOf(pos) + ".zip";
            Log.i("url", url);
            FileDownloadAsync fileDownload = new FileDownloadAsync(context, url, true, new DownloadListener() {
                @Override
                public void onStart(int minValue, int maxValue) {
                    Log.i("download", "Download Start.");
                }

                @Override
                public void onSuccess(String fileName, String directory) {
                    Log.i(fileName, "Download Success.");
                    moveVocaActivity(pos);
                }

                @Override
                public void onQueue(int progress) { }

                @Override
                public void onFail() {
                    Log.i("download", "Fail.");
                }

                @Override
                public void onFinish() { }

                @Override
                public void onDownloadCancel() {}

            });
            fileDownload.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moveVocaActivity(int pos) {
        try {
            Intent intent = new Intent(context, VocaActivity.class);
            intent.putExtra("pos", pos);
            ((Activity) context).startActivityForResult(intent, 1000);
        } catch (Exception e) {
        }
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

}