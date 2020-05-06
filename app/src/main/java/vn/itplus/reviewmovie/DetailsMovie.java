package vn.itplus.reviewmovie;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.timqi.sectorprogressview.ColorfulRingProgressView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.itplus.reviewmovie.adapter.CastAdapter;
import vn.itplus.reviewmovie.adapter.CommentAdapter;
import vn.itplus.reviewmovie.model.cast.Cast;
import vn.itplus.reviewmovie.model.cast.Casts;
import vn.itplus.reviewmovie.model.comment.Comment;
import vn.itplus.reviewmovie.model.movie.details.Movie;
import vn.itplus.reviewmovie.model.movie.translation.Translation;
import vn.itplus.reviewmovie.model.user.User;
import vn.itplus.reviewmovie.model.video.Video;
import vn.itplus.reviewmovie.retrofit2.MService;
import vn.itplus.reviewmovie.retrofit2.RetrofitClient;
import vn.itplus.reviewmovie.utils.Utils;
import vn.itplus.reviewmovie.youtube.Config;

public class DetailsMovie extends YouTubeBaseActivity {
    ImageView imgbackdrop, imgposter;
    TextView txtTitleDetail, txtvote_average, txtdate, txtGenre, txtBudget, txtRevenue, txtOverview, checkNullComment;
    EditText txtYourComment;
    ImageButton btnSendComment;
    Button btnMoreComments, btnDatVe;
    ColorfulRingProgressView vote_average;
    Translation translation;
    CastAdapter castAdapter;
    RecyclerView recyclerCast, recyclerComment;
    Casts casts;
    Video video;
    Movie movie;
    ArrayList<Cast> castArrayList;
    boolean check = false;
    int dem;
    String id, title, overview , datVe;
    YouTubePlayerView youTubePlayerView;
    CommentAdapter commentAdapter;
    ArrayList<Comment> comments;
    DatabaseReference mDatabase;
    FirebaseUser firebaseUser;
    User user;
    String userName, photoUrl, UID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.ALPHA_CHANGED, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.details_movie);

        Intent intent = getIntent();
        datVe = intent.getStringExtra("datVe");
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        overview = intent.getStringExtra("overview");
        Log.d("ID", id);
        addControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (datVe.equalsIgnoreCase("yes")){
            btnDatVe.setVisibility(View.VISIBLE);
        }else {
            btnDatVe.setVisibility(View.GONE);
        }
        getDetails(id);
        getComments(id);
        getInfomationUser();
        addEvents();

    }

    private void getInfomationUser() {
        UID = firebaseUser.getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.child("User").getChildren()) {
                    user = ds.getValue(User.class);
                    if (user.getId().equalsIgnoreCase(UID)) {
                        userName = user.getName();
                        photoUrl = user.getPhotoURL();

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getComments(String idMovie) {

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Comment> list = new ArrayList<>();
                try {
                    comments.clear();
                    list.clear();
                    for (DataSnapshot ds : dataSnapshot.child("getcomment").child(idMovie).child("Comment").getChildren()) {
                        Comment dsComment = ds.getValue(Comment.class);
                        comments.add(dsComment);
                    }
                    if (comments.size() > 0) {
                        checkNullComment.setVisibility(View.GONE);

                        if (comments.size() > 5) {
                            btnMoreComments.setVisibility(View.VISIBLE);
                            for (int i = (comments.size() - 1); i > comments.size() - 6; i--) {
                                list.add(comments.get(i));
                            }
                        } else {
                            btnMoreComments.setVisibility(View.GONE);
                            for (int i = (comments.size() - 1); i > 0; i--) {
                                list.add(comments.get(i));

                        }
                        }
                    }
                    else {
                        checkNullComment.setVisibility(View.VISIBLE);
                        btnMoreComments.setVisibility(View.GONE);
                    }
                        commentAdapter = new CommentAdapter(list, getApplicationContext());
                        recyclerComment.setHasFixedSize(false);
                        recyclerComment.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, RecyclerView.VERTICAL, true));
                        recyclerComment.setItemAnimator(new DefaultItemAnimator());
                        recyclerComment.setAdapter(commentAdapter);
                        commentAdapter.notifyDataSetChanged();

                    }catch(Exception e){
                        e.printStackTrace();
                    }


                }

                @Override
                public void onCancelled (@NonNull DatabaseError databaseError){

                }
            });
        }

        private void getYoutubeVideo (String s){
            youTubePlayerView.initialize(Config.getApiKeyYoutube(), new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                    Log.d("YOUTUBEEE", "Done");
                    youTubePlayer.loadVideo(s);
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    Log.d("YOUTUBEEE", "Notdone");
                }
            });
        }

        public void setRecyclerCast (ArrayList < Cast > cast) {
            castAdapter = new CastAdapter(cast, getApplicationContext());
            recyclerCast.setHasFixedSize(true);
            recyclerCast.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, RecyclerView.HORIZONTAL, false));
            recyclerCast.setItemAnimator(new DefaultItemAnimator());
            recyclerCast.setAdapter(castAdapter);
        }


        private void getDetails (String id){

            MService mService = RetrofitClient.getInstance().getClient().create(MService.class);
            Call<Translation> call = mService.getAnswers(id).clone();
            Call<Video> call2 = mService.getVideo(id).clone();
            Call<Casts> call3 = mService.getCasts(id).clone();
            Call<Movie> call1 = mService.getDetails(id).clone();
            call.enqueue(new Callback<Translation>() {
                @Override
                public void onResponse(Call<Translation> call, Response<Translation> response) {

                    Translation movie = response.body();
                    translation = movie;

                    try {
                        for (int i = 0; i < translation.getTranslations().size(); i++) {

                            if (translation.getTranslations().get(i).getIso6391().equalsIgnoreCase("vi")) {
                                check = true;
                                dem = i;
                            }
                        }
                        if (check = true && !translation.getTranslations().get(dem).getData().getTitle().isEmpty()) {
                            txtTitleDetail.setText(translation.getTranslations().get(dem).getData().getTitle());

                            txtOverview.setText(translation.getTranslations().get(dem).getData().getOverview());
                        } else {
                            txtTitleDetail.setText(title);
                            txtOverview.setText(overview);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(Call<Translation> call, Throwable t) {
                    Log.d("TAG", String.valueOf(t));

                }
            });
            call1.enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    movie = response.body();
                    txtvote_average.setText(String.valueOf(movie.getVoteAverage() * 10));
                    vote_average.setPercent(Float.valueOf(String.valueOf(movie.getVoteAverage() * 10)));
                    Picasso.get().load("https://image.tmdb.org/t/p/original" + movie.getBackdropPath()).into(imgbackdrop);
                    Picasso.get().load("https://image.tmdb.org/t/p/original" + movie.getPosterPath()).into(imgposter);
                    txtdate.setText("(" + movie.getReleaseDate() + ")");
                    txtBudget.setText(String.format("%-1s%,.2f","$",movie.getBudget()));
                    txtRevenue.setText(String.format("%-1s%,.2f","$",movie.getRevenue()));
                    if (movie.getGenres().size() > 0) {
                        String s = movie.getGenres().get(0).getName() + "";
                        String s1 = "";
                        for (int i = 1; i < movie.getGenres().size(); i++) {
                            s1 += ", " + movie.getGenres().get(i).getName();
                        }
                        txtGenre.setText(s + s1);
                    }
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {

                }
            });
            call2.enqueue(new Callback<Video>() {
                @Override
                public void onResponse(Call<Video> call, Response<Video> response) {
                    video = response.body();
                    for (int i = 0; i < video.getResults().size(); i++) {
                        if (video.getResults().get(i).getType().equalsIgnoreCase("Trailer") || video.getResults().get(i).getType().equalsIgnoreCase("Teaser Trailer"))
                            getYoutubeVideo(video.getResults().get(i).getKey());
                        continue;
                    }
                }

                @Override
                public void onFailure(Call<Video> call, Throwable t) {

                }
            });
            call3.enqueue(new Callback<Casts>() {
                @Override
                public void onResponse(Call<Casts> call, Response<Casts> response) {
                    casts = response.body();

                    try {
                        Log.d("CAST111111", casts.getCast().get(0).getName());
                        for (int i = 0; i < 7; i++) {
                            castArrayList.add(casts.getCast().get(i));
                        }
                        setRecyclerCast(castArrayList);
                    } catch (Exception ex) {

                    }

                }

                @Override
                public void onFailure(Call<Casts> call, Throwable t) {

                }
            });
        }


        private void addEvents () {
        btnMoreComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ShimmerComment.class);
                intent.putExtra("idMovie",id);
                startActivity(intent);
            }
        });

            btnSendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!txtYourComment.getText().toString().isEmpty()) {
                        Date date = Calendar.getInstance().getTime();
                        String timestamp = Utils.DateToString(date);
                        Comment comment = new Comment();
                        comment.setIdUser(UID);
                        comment.setIdMovie(id);
                        comment.setDescription(txtYourComment.getText().toString());
                        comment.setTimestamp(timestamp);

                        FirebaseDatabase.getInstance().getReference().child("getcomment").child(id).child("Comment").push().setValue(comment);
                        txtYourComment.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), "Không được để trống", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }


        private void addControls () {
            btnDatVe = findViewById(R.id.btnDatVe);
            checkNullComment = findViewById(R.id.checkNullComment);
            btnMoreComments = findViewById(R.id.btnMoreComments);
            btnSendComment = findViewById(R.id.btnSendComment);
            txtYourComment = findViewById(R.id.txtYourComment);
            imgbackdrop = findViewById(R.id.imgbackdrop);
            imgposter = findViewById(R.id.imgposter);
            txtTitleDetail = findViewById(R.id.txtTitleDetail);
            txtvote_average = findViewById(R.id.txtvote_average);
            txtdate = findViewById(R.id.txtdate);
            txtGenre = findViewById(R.id.txtGenre);
            txtBudget = findViewById(R.id.txtBudget);
            txtRevenue = findViewById(R.id.txtRevenue);
            txtOverview = findViewById(R.id.txtOverview);
            vote_average = findViewById(R.id.vote_average);
            recyclerCast = findViewById(R.id.recyclerCast);
            recyclerComment = findViewById(R.id.recyclerComment);
            translation = new Translation();
            youTubePlayerView = findViewById(R.id.youtube_view);
            casts = new Casts();
            castArrayList = new ArrayList<>();
            video = new Video();
            movie = new Movie();
            comments = new ArrayList<>();
            user = new User();


        }

    }
