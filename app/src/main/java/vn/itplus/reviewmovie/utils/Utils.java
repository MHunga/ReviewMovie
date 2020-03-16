package vn.itplus.reviewmovie.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vn.itplus.reviewmovie.R;

public class Utils {

    private static final String TAG = "TAG";
    private static AlertDialog progressDialog;


    public static void SetProgressDialogIndeterminate(final Context context, String message) {
        try {
            LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.progress_dialog_layout, null);

            TextView title = view.findViewById(R.id.title);
            title.setText(message);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(view);
            builder.setCancelable(true);
            builder.setPositiveButton("Bỏ qua", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((Activity) context).finish();
                    dialog.dismiss();
                }
            });
            progressDialog = builder.create();
            progressDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    progressDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.progressDialogPositiveTextColor));
                }
            });
            progressDialog.show();
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
    }

    public static void UnSetProgressDialogIndeterminate() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
    public static String DateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy ") ;
        String dateformat = simpleDateFormat.format(date);
        return dateformat;
    }
    public static Date StringToDate(String s){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yy ") ;
        try {
            date   = simpleDateFormat.parse(s);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String getTimeStampComment(Date dateTime){
        StringBuffer sb =new StringBuffer();
        Date current = Calendar.getInstance().getTime();
        long diffInSeconds = (current.getTime()- dateTime.getTime())/1000;

        long sec = (diffInSeconds >= 60 ? diffInSeconds%60 : diffInSeconds);
        long min = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
        long hrs = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
        long days = (diffInSeconds = (diffInSeconds / 24)) >= 30 ? diffInSeconds % 30 : diffInSeconds;
        long months = (diffInSeconds = (diffInSeconds / 30)) >= 12 ? diffInSeconds % 12 : diffInSeconds;
        long years = (diffInSeconds = (diffInSeconds / 12));

        if (years>0){
            if (years ==1 ){
                sb.append("a year");
            }
            else {
                sb.append(years+" years");
            }
        }else if (months>0){
            if (months ==1){
                sb.append("a month");
            }else {
                sb.append(months+" months");
            }
        }
        else if (days>0){
            if (days ==1){
                sb.append("a day");
            }else {
                sb.append(days+" days");
            }
        }
        else if (hrs>0){
            if (hrs ==1){
                sb.append("a hour");
            }else {
                sb.append(hrs+" hours");
            }
        }
        else if (min>0){
            if (min ==1){
                sb.append("a minute");
            }else {
                sb.append(min+" minutes");
            }
        }
        else {
            if (sec <= 1) {
                sb.append("about a second");
            } else {
                sb.append("about " + sec + " seconds");
            }
        }

        sb.append(" ago");
        return sb.toString();
    }
}
