package vn.itplus.reviewmovie.notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import vn.itplus.reviewmovie.model.notification.Notification;

public class OpenHelperDataBaseNotification extends SQLiteOpenHelper {
    private static final String DATA_NOTIFICATION = "data_notification";
    private static final int VERSION = 1;

    public static final String TABLE_NOTIFICATION = "notification";
    public static final String TITLE_NOTIFICATION = "title";
    public static final String BODY_NOTIFICATION = "body";

    SQLiteDatabase sqLiteDatabase;

    public OpenHelperDataBaseNotification(Context context) {
        super(context, DATA_NOTIFICATION, null, VERSION);
        sqLiteDatabase = this.getWritableDatabase();

    }


    public OpenHelperDataBaseNotification(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + TABLE_NOTIFICATION
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_NOTIFICATION + " TEXT NOT NULL, "
                + BODY_NOTIFICATION + " TEXT ); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
            onCreate(db);
        }
    }

    public ArrayList<Notification> getAll() {
        String sql = "SELECT * FROM " + TABLE_NOTIFICATION;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);

        ArrayList<Notification> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String body = cursor.getString(2);
            list.add(new Notification(id,title,body));
        }
        return list;
    }
    public void Add(Notification notification){
        sqLiteDatabase = this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(TITLE_NOTIFICATION,notification.getTitle());
        values.put(BODY_NOTIFICATION,notification.getBody());


        sqLiteDatabase.insert(TABLE_NOTIFICATION,null,values);
        sqLiteDatabase.close();

    }
}
