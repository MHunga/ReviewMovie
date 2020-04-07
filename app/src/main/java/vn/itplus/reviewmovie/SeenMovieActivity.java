package vn.itplus.reviewmovie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import vn.itplus.reviewmovie.OpenHelperDataBase.OpenHelperDatabaseSeen;
import vn.itplus.reviewmovie.adapter.SeenAdapter;
import vn.itplus.reviewmovie.model.movie.seen.Seen;
import vn.itplus.reviewmovie.onclickitem.OnClickItem2;

public class SeenMovieActivity extends AppCompatActivity implements OnClickItem2 {
    RecyclerView recyclerSeen;
    ArrayList<Seen> seenArrayList;
    SeenAdapter seenAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seen_movie);
        addControls();
        try {
            getSeenMovie();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getSeenMovie() {
        OpenHelperDatabaseSeen openHelperDatabaseSeen = new OpenHelperDatabaseSeen(getApplicationContext());
        seenArrayList = openHelperDatabaseSeen.getAll();

        seenAdapter = new SeenAdapter(seenArrayList, getApplicationContext(), this);
        recyclerSeen.setHasFixedSize(true);
        recyclerSeen.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3, RecyclerView.VERTICAL, false));
        recyclerSeen.setItemAnimator(new DefaultItemAnimator());
        recyclerSeen.setAdapter(seenAdapter);
    }

    private void addControls() {
        recyclerSeen = findViewById(R.id.recyclerSeen);
        seenArrayList = new ArrayList<>();
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(getApplicationContext(), DetailsMovie.class);
        Log.d("TAG: ID", String.valueOf(seenArrayList.get(position).getId()));
        intent.putExtra("id", String.valueOf(seenArrayList.get(position).getId()).trim());
        intent.putExtra("title", seenArrayList.get(position).getName());
        startActivity(intent);
    }
}
