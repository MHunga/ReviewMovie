package vn.itplus.reviewmovie;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;


import vn.itplus.reviewmovie.fragment.SearchFragment;
import vn.itplus.reviewmovie.fragment.HomeFragment;
import vn.itplus.reviewmovie.fragment.NotificationFragment;
import vn.itplus.reviewmovie.fragment.ProfileFragment;

import vn.itplus.reviewmovie.utils.BottomNavigationBehavior;

public class MainActivity extends AppCompatActivity  {
    boolean doublePressToExit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.ALPHA_CHANGED,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
     //   NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
     //   NavigationUI.setupWithNavController(navigationView, navController);


        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior() );
    }

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment;
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:

                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_search:

                    fragment = new SearchFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_notification:

                    fragment= new NotificationFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:

                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onBackPressed() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        int seletedItemId = bottomNavigationView.getSelectedItemId();
        if (R.id.navigation_home != seletedItemId) {
            setHomeItem(MainActivity.this);
            Fragment fragment = new HomeFragment();
            loadFragment(fragment);
        } else {
            if (doublePressToExit) {
                finish();
                return;
            }
            this.doublePressToExit = true;
            Toast.makeText(MainActivity.this, getString(R.string.click_back_to_exit), Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doublePressToExit = false;
                }
            }, 2000);

        }


    }

    private void setHomeItem(Activity activity) {
        BottomNavigationView bottomNavigationView =
                activity.findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }


}
