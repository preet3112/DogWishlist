package com.assignment.jas.dogwishlist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView imgDog;
    ImageView imgLike;
    ImageView imgDislike;
    Button btnList;
    String url="https://dog.ceo/api/breeds/image/random";
    String TAG = MainActivity.class.getSimpleName();
    String tstatus;
    Uri tmsg;
    Activity activity;
    DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        imgDog=(ImageView)findViewById(R.id.imageDog);
        imgLike=(ImageView)findViewById(R.id.imgLike);
        imgDislike=(ImageView)findViewById(R.id.imgDislike);
        btnList=(Button)findViewById(R.id.imgbtn);
        new MyConnectionAsyncTask().execute();
        setListner();
        databaseHelper=new DatabaseHelper(this);







    }

    private void setListner() {
        imgDislike.setOnClickListener(new nextImage());
        imgLike.setOnClickListener(new listSave());
        btnList.setOnClickListener(new Click());
    }

    private class MyConnectionAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override

        protected void onPreExecute(){
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Picasso.with(activity)
                    .load(tmsg)
                    .error(R.drawable.temp)
                    .fit()
                    .placeholder(R.drawable.animate_load)
                    .into(imgDog);


        }

        protected Void doInBackground(Void... params) {

            String response=null;
            try{
                URL url1=new URL(url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url1.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                response = convertStreamToString(in);
                System.out.print("Ress"+response);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response!=null){
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    String statusS=jsonObject.getString("status");
                    String msg=jsonObject.getString("message");

                    tstatus=statusS;
                    tmsg=Uri.parse(msg);
                    System.out.print("TT"+tmsg);

                } catch (final JSONException e) {

                    Log.e(TAG,"Json parsing error"+e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Json  Parsing error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    e.printStackTrace();
                }
            }
            else {
                Log.e(TAG,"couldn't get json from server");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "couldn't get json from server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }
    }

    private String convertStreamToString(InputStream in) {
        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            {
                while ((line = reader.readLine()) != null){
                    sb.append(line).append('\n');
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private class nextImage implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new MyConnectionAsyncTask().execute();
        }
    }

    private class listSave implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c);
            System.out.print(""+formattedDate);
            String url=tmsg.toString();
            boolean in=databaseHelper.insert(url,formattedDate);
            if(in=true){
                Toast.makeText(MainActivity.this, "Data added successfully", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private class Click implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Cursor re=databaseHelper.getData();
            if(re.getCount()==0){
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
            StringBuffer stringBuffer=new StringBuffer();
            while (re.moveToNext()){
                stringBuffer.append("ID"+re.getString(0)+"\n");
                stringBuffer.append("URL"+re.getString(1)+"\n");
                stringBuffer.append("DATE"+re.getString(2)+"\n");

            }
            Toast.makeText(MainActivity.this, ""+stringBuffer.toString(), Toast.LENGTH_SHORT).show();


            Intent intent=new Intent(MainActivity.this,LikedDog.class);

            startActivity(intent);
        }
    }
}
