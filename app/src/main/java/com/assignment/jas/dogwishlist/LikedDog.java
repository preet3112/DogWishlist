package com.assignment.jas.dogwishlist;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class LikedDog extends AppCompatActivity{
    DatabaseHelper databaseHelper;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        isInMultiWindowMode();
        setContentView(R.layout.list_layout);
        System.out.print("DDDD");

        Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
        databaseHelper=new DatabaseHelper(this);
        Cursor re=databaseHelper.getData();
        if(re.getCount()==0){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        StringBuffer stringBuffer=new StringBuffer();
        while (re.moveToNext()){
            stringBuffer.append("ID"+re.getString(0)+"n");
            stringBuffer.append("URL"+re.getString(1)+"n");
            stringBuffer.append("DATE"+re.getString(2)+"n");

        }
        Toast.makeText(this, ""+stringBuffer.toString(), Toast.LENGTH_SHORT).show();
        System.out.print("DD"+stringBuffer.toString());

    }



}
