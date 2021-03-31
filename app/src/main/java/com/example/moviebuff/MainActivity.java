package com.example.moviebuff;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView ;
    EditText editText1;
    EditText editText2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         textView=findViewById(R.id.textView);
         editText1=(EditText)findViewById(R.id.editText1);
         editText2=(EditText)findViewById(R.id.editText2);
         button =(Button)findViewById(R.id.button);


         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String  musername=editText1.getText().toString();
                 String mpassword=editText2.getText().toString();
                 textView.setText(musername);
                 CheckConnection connection = new CheckConnection();
                 connection.execute("https://abhash.rulesbroken.in/android.php",musername,mpassword);
             }
         });




    }
    class CheckConnection extends AsyncTask<String,Void,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // textView.setText("abhash");
        }

        @Override
        protected String doInBackground(String... strings) {
            String result="";
            String s="";
            try {
                URL url =new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                Uri.Builder builder =new Uri.Builder()
                        .appendQueryParameter("username",strings[1])
                        .appendQueryParameter("password",strings[2]);
                OutputStream outputStream =urlConnection.getOutputStream();
                BufferedWriter bufferedWriter =new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(builder.build().getEncodedQuery());
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                urlConnection.connect();

                InputStream inputStream =urlConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                bufferedReader.close();
                result=sb.toString();
                  s =urlConnection.getResponseMessage();
                Log.i("Response",String.valueOf(urlConnection.getResponseCode()));
            } catch (IOException e) {
                Log.e("Error",e.getMessage());
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);

        }
    }
}