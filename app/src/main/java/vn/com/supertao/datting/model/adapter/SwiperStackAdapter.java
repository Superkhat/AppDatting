package vn.com.supertao.datting.model.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

import vn.com.supertao.datting.R;
import vn.com.supertao.datting.model.object.User;

public class SwiperStackAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> mUsers;

    public SwiperStackAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        Log.e("sizeok",mUsers.size()+"");
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public String getItem(int i) {
        return mUsers.get(i).getId();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view==null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_swipestack, viewGroup, false);
        }
        TextView tvName=view.findViewById(R.id.tvNameUser);
        tvName.setText(mUsers.get(position).getName());
        TextView tvAge=view.findViewById(R.id.tvAge);
        tvAge.setText(mUsers.get(position).getYear()+" tuổi");
        TextView tvCNS=view.findViewById(R.id.tvChamNgonSong);
        tvCNS.setText("Châm ngôn sống: "+mUsers.get(position).getChamNgonSong());
        ImageView ivImageUser=view.findViewById(R.id.ivImageUser);
       switch (mUsers.get(position).getImage1())
       {
           case "default":
               Glide.with(mContext).load(R.drawable.ic_person).into(ivImageUser);
           default:
               Glide.with(mContext).load(mUsers.get(position).getImage1()).into(ivImageUser);
       }

        return view;
    }
}
