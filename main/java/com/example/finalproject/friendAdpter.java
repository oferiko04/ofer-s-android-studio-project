package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class friendAdpter extends ArrayAdapter {
    Context context ;
    List<friend> objects;
    public friendAdpter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<friend> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context ;
        this.objects = objects ;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.friend,parent, false);

        TextView tv_username = (TextView)view.findViewById(R.id.tv_username);
        TextView tv_adapterDate = (TextView)view.findViewById(R.id.tv_adapterDate);
        TextView tv_age = (TextView)view.findViewById(R.id.tv_age);
        TextView tv_daysUntil = (TextView)view.findViewById(R.id.tv_daysUntil);
        ImageView iv_friendFace=(ImageView)view.findViewById(R.id.iv_friendFace);



        friend temp = objects.get(position);
        tv_username.setText(tv_username.getText()+" "+temp.getUsername());
        if(temp.getBirthday()!=null) {
            tv_adapterDate.setText(tv_adapterDate.getText()+" "+temp.getBirthday().getDate()+"/"+temp.getBirthday().getMonth()+"/"+temp.getBirthday().getYear());
        }
        tv_age.setText(tv_age.getText()+" "+temp.getAge());
        tv_daysUntil.setText(tv_daysUntil.getText()+" "+ get_count_of_days(temp.getBirthday().getDate(),temp.getBirthday().getMonth(),temp.getBirthday().getYear()));
        if (temp.getFace().equals("pic")){
            iv_friendFace.setImageResource(R.drawable.nophotos);
        }
        else{
            Picasso.get().load(temp.getFace()).into(iv_friendFace);
        }



        return view;
    }
    public String get_count_of_days(int yourBirthday_day,int yourBirthday_month, int yourBirthday_year) {
        String answer;
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int today_year = calendar.get(Calendar.YEAR);
        int today_month = calendar.get(Calendar.MONTH) + 1 ;
        int today_day = calendar.get(Calendar.DAY_OF_MONTH) ;

        if (today_month == yourBirthday_month || today_month + 1 == yourBirthday_month || (yourBirthday_month==1 && today_month==12)){
            int today_month_has;
            if(today_month == 4 || today_month == 6 || today_month == 9 || today_month == 11) today_month_has = 30;
            else if (today_month == 2) today_month_has = 28;
            else today_month_has = 31;
            if (today_month == yourBirthday_month){
                if(today_day > yourBirthday_day){
                    return "in more than one month";
                }
                if (today_day == yourBirthday_day){
                    return "today!" ;
                }
                else{
                    answer = "in"+(yourBirthday_day-today_day)+ " days";
                    return answer;
                }
            }
            else{
                answer = "in"+ (yourBirthday_day + (today_month_has-today_day))+ " days";
                return answer;
            }

        }
        else{
            return "in more than one month";
        }
    }
}
