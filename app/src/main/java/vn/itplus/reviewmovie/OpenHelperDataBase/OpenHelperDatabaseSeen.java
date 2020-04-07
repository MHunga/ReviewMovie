package vn.itplus.reviewmovie.OpenHelperDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import vn.itplus.reviewmovie.model.movie.seen.Seen;

public class OpenHelperDatabaseSeen extends SQLiteOpenHelper {
    private static final String DATA_SEEN = "data_seen";
    private static final int VERSION = 1;

    public static final String TABLE_SEEN = "seen";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PHOTO_URL = "photo_url";
    public static final String USER_SCORE = "user_score";
    public static final String RELEASE_DATE = "release_date";

    SQLiteDatabase sqLiteDatabase;

    public OpenHelperDatabaseSeen(Context context){
        super(context,DATA_SEEN,null,VERSION);
        sqLiteDatabase = this.getWritableDatabase();
    }
    public OpenHelperDatabaseSeen(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + TABLE_SEEN
                + " (" +ID+" INTEGER, "
                + NAME + " TEXT NOT NULL, "
                + USER_SCORE + " FLOAT, "
                + RELEASE_DATE +" TEXT, "
                + PHOTO_URL + " TEXT ); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEEN);
            onCreate(db);
        }
    }
    public ArrayList<Seen> getAll(){
        String sql = "SELECT * FROM " + TABLE_SEEN;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        ArrayList<Seen> list = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            float userScore = cursor.getFloat(2);
            String date = cursor.getString(3);
            String photo = cursor.getString(4);
            list.add(new Seen(id,name,photo,userScore,date));
        }
        return list;
    }
    public void Add(Seen seen){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(NAME,seen.getName());
        values.put(ID,seen.getId());
        values.put(PHOTO_URL,seen.getPhotoUrl());
        values.put(USER_SCORE,seen.getUserScore());
        values.put(RELEASE_DATE,seen.getRelease_date());


        sqLiteDatabase.insert(TABLE_SEEN,null,values);
        sqLiteDatabase.close();

    }
}
