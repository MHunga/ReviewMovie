package vn.itplus.reviewmovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.itplus.reviewmovie.adapter.CommentAdapter;
import vn.itplus.reviewmovie.model.comment.Comment;

public class ShimmerComment extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerAllComment;
    private ArrayList<Comment> list;
    private CommentAdapter mAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;
    String idMovie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.ALPHA_CHANGED, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_shimmer_comment);
        Intent intent = getIntent();
        idMovie = intent.getStringExtra("idMovie");
        Log.d("IDDDDDD",idMovie);
        addControls();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getComment(idMovie);
            }
        },3000);

    }

    private void getComment(String idMovie) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.child("getcomment").child(idMovie).child("Comment").getChildren()) {
                        Comment dsComment = ds.getValue(Comment.class);
                        list.add(dsComment);
                    }

                    mAdapter = new CommentAdapter(list, getApplicationContext());
                    recyclerAllComment.setHasFixedSize(false);
                    recyclerAllComment.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, RecyclerView.VERTICAL, false));
                    recyclerAllComment.setItemAnimator(new DefaultItemAnimator());
                    recyclerAllComment.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);

                }catch(Exception e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addControls() {
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerAllComment = findViewById(R.id.recyclerAllComment);
        list = new ArrayList<>();
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
}
