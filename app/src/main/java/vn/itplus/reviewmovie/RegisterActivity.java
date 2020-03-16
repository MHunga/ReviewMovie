package vn.itplus.reviewmovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import vn.itplus.reviewmovie.adapter.PagerControllerRegister;

public class RegisterActivity extends AppCompatActivity {
   static ViewPager viewPagerRegister;
    TabLayout tabLayoutRegister;
    TabItem itemTabMail,itemTabPhone;
    PagerControllerRegister pagerControllerRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        addControls();
        addEvents();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }

    private void addEvents() {

    }


    private void addControls() {
        viewPagerRegister = findViewById(R.id.viewPagerRegister);
        tabLayoutRegister = findViewById(R.id.tabLayoutRegister);
        itemTabMail = findViewById(R.id.itemTabMail);
        itemTabPhone = findViewById(R.id.itemTabPhone);

        pagerControllerRegister = new PagerControllerRegister(getSupportFragmentManager(),tabLayoutRegister.getTabCount());
        viewPagerRegister.setAdapter(pagerControllerRegister);

        tabLayoutRegister.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerRegister.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPagerRegister.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutRegister));
    }

}
