package vn.com.supertao.datting.model.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<Uri> mUriImages;

    public ViewPagerAdapter(Context mContext, List<Uri> mUriImages) {
        this.mContext = mContext;
        this.mUriImages = mUriImages;
    }

    @Override
    public int getCount() {
        return mUriImages.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView=new ImageView(mContext);
        Glide.with(mContext).load(mUriImages.get(position)).into(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }
}
