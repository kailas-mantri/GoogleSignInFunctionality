package com.scrimatec.scrimsinternalstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;

    String filename = "Internal Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=findViewById(R.id.tvtext);
        editText=findViewById(R.id.edtext);
    }


    public void onWrite(View view) {
    String getmessage= editText.getText().toString();

    if (TextUtils.isEmpty(getmessage)){
        editText.setError("PLease Write Something!");
        editText.requestFocus();
        }
        else{
            try {
                FileOutputStream fileOutputStream = openFileOutput(filename,MODE_APPEND);
                OutputStreamWriter fileOutputStream1 = new OutputStreamWriter(fileOutputStream);

                fileOutputStream1.write(getmessage);
                fileOutputStream1.close();

                editText.setText("");
                Toast.makeText(this, "Data Stored", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) { e.printStackTrace(); }
        }
    }

    public void onRead(View view) {
        try {
            FileInputStream inputStream = openFileInput(filename);

            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder stringBuilder = new StringBuilder();
            // converted into string by buffer reader & string builder

            String string=null;

            while ((string=bufferedReader.readLine())!=null)
                { stringBuilder.append(string); }

            inputStream.close();
            reader.close();

            Toast.makeText(this, "File Read Successfully", Toast.LENGTH_SHORT).show();
            textView.setText("Name :"+stringBuilder.toString());
        }
        catch (Exception e) { e.printStackTrace(); }
    }
}