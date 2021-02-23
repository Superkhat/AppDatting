package vn.com.supertao.datting.model.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.com.supertao.datting.R;
import vn.com.supertao.datting.model.object.Diary;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.ViewHodler> {
    private Context mContext;
    private List<Diary> mDiaryList;

    public DiaryAdapter(Context mContext, List<Diary> mDiaryList) {
        this.mContext = mContext;
        this.mDiaryList = mDiaryList;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_diary, parent, false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
            String temp=mDiaryList.get(position).getImageUserDiay();
        Log.e("temp",temp);

        switch (temp)
            {
                case "default":
                    Toast.makeText(mContext,mDiaryList.get(position).getImageCentenDiary()+"",Toast.LENGTH_LONG).show();
                    break;
                case "null":
                    Toast.makeText(mContext,mDiaryList.get(position).getImageCentenDiary()+"",Toast.LENGTH_LONG).show();
                    break;
                default:
                    Glide.with(mContext).load(mDiaryList.get(position).getImageUserDiay()).into(holder.ivUserDiary);
                    break;
            }
        switch (mDiaryList.get(position).getImageCentenDiary())
        {
            case "default":
                holder.ivContentImageDiary.setVisibility(View.GONE);
                break;
            case "null":

                Toast.makeText(mContext,mDiaryList.get(position).getImageCentenDiary()+"null",Toast.LENGTH_LONG).show();
                break;
            default:
                Glide.with(mContext).load(mDiaryList.get(position).getImageCentenDiary()).into(holder.ivContentImageDiary);
                break;
        }
        holder.tvNameUserDiary.setText(mDiaryList.get(position).getNameUserDiary());
        holder.tvContentDiary.setText(mDiaryList.get(position).getContentDiary());
        holder.tvDateTime.setText(mDiaryList.get(position).getDate());


    }

    @Override
    public int getItemCount() {
        Log.e("sizze", mDiaryList.size() + "");
        return mDiaryList.size();
    }


    public class ViewHodler extends RecyclerView.ViewHolder {
        private ImageView ivUserDiary;
        private TextView tvNameUserDiary;
        private TextView tvContentDiary;
        private CheckBox cbLike;
        private ImageView ivContentImageDiary;
        private TextView tvComment;
        private TextView tvDateTime;

        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            ivUserDiary = itemView.findViewById(R.id.ivImageUserDiary);
            tvNameUserDiary = itemView.findViewById(R.id.tvNameUserDiary);
            tvContentDiary = itemView.findViewById(R.id.tvContentDiary);
            cbLike = itemView.findViewById(R.id.cbLike);
            ivContentImageDiary = itemView.findViewById(R.id.ivImageDiary);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvDateTime=itemView.findViewById(R.id.tvDateDiary);
        }
    }
}
