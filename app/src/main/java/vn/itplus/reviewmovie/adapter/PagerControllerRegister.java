package vn.itplus.reviewmovie.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import vn.itplus.reviewmovie.MailRegisterFragment;
import vn.itplus.reviewmovie.PhoneRegisterFragment;

public class PagerControllerRegister extends FragmentPagerAdapter {
    int tabCounts;

    public PagerControllerRegister(FragmentManager fm, int tabCounts) {
        super(fm);
        this.tabCounts = tabCounts;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new MailRegisterFragment();
        }else
            return new PhoneRegisterFragment();


        }



    @Override
    public int getCount() {
        return tabCounts;
    }
}
