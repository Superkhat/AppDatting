package vn.com.supertao.datting.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.com.supertao.datting.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHodler> {
    private Context mContext;
    private List<String> urlImage;

    public ImageAdapter(Context mContext, List<String> urlImage) {
        this.mContext = mContext;
        this.urlImage = urlImage;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.item_list_image,parent,false);
        return new ViewHodler(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHodler holder, int position) {
        switch (urlImage.get(position))
        {
            case "default":

                break;
            default:
                Glide.with(mContext).load(urlImage.get(position)).into(holder.ivImageInformation);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return urlImage.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        private ImageView ivImageInformation;
        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            ivImageInformation=itemView.findViewById(R.id.ivImageInformation);
        }
    }
}
