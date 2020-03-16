package vn.itplus.reviewmovie.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

    public  BottomNavigationBehavior(){
        super();
    }
    public BottomNavigationBehavior(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }
    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull BottomNavigationView child, @NonNull View dependency) {

       boolean dependsOn = dependency instanceof FrameLayout;
        return dependsOn;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {

        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy<0){
            showBottomNavigation(child);
        }
        else if(dy >0){
            hideBottomNavigation(child);
        }
    }

    private void hideBottomNavigation(BottomNavigationView child) {
        child.animate().translationY(child.getHeight());
    }

    private void showBottomNavigation(BottomNavigationView child) {
        child.animate().translationY(0);

    }

}
