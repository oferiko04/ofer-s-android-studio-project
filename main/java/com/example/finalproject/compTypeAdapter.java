package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class compTypeAdapter extends ArrayAdapter  {
    Context context ;
    List<compType> objects;
    public compTypeAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<compType> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context = context ;
        this.objects = objects ;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.typeofcomp,parent, false);

        TextView tvcompName = (TextView)view.findViewById(R.id.name);
        TextView tvdescription = (TextView)view.findViewById(R.id.description);
        ImageView ivimage=(ImageView)view.findViewById(R.id.image);

        compType temp = objects.get(position);
        ivimage.setImageResource(temp.getImage());
        tvcompName.setText(String.valueOf(temp.getName()));
        tvdescription.setText(temp.getDescription());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compType chosenComp = objects.get(position);
                MainActivityComp.selected_comp = chosenComp ;

                Bundle b = new Bundle();
                b.putSerializable("compType", tvcompName.getText().toString());
                Intent startComplistActivity = new Intent(context, MainActivityCompList.class);
                startComplistActivity.putExtras(b);

                context.startActivity(startComplistActivity);



            }
        });
        return view;
    }

}
