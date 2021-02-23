package vn.com.supertao.datting.model.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;
    private String mNameFragment[]={"Khám Phá","Nhật kí","Thông báo","Cài đặt"};
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
    public void addFragment(List<Fragment> mFragmentList)
    {
        this.mFragmentList=mFragmentList;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }
}
