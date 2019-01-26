package ru.klepovnikita.billconvert;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String[] bills = {"ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM",
            "BBD", "BDT","BGN","BHD","BIF","BND","BOB",
            "BRL","BSD","BTC","BTN","BWP","BYN","BYR",
            "BZD","CAD","CDF","CHF","CLP","CNY","COP",
            "CRC","CUP","CVE","CZK","DJF","DKK","DOP",
            "DZD","EGP","ERN","ETB","EUR","FJD","FKP",
            "GBP","GEL","GHS","GIP","GMD","GNF","GTQ",
            "GYD","HKD","HNL","HRK","HTG","HUF","IDR",
            "ILS","INR","IQD","IRR","ISK","JMD","JOD",
            "JPY","KES","KGS","KHR","KMF","KPW","KRW",
            "KWD","KYD","KZT","LAK","LBP","LKR","LRD",
            "LSL","LVL","LYD","MAD","",
            "","","","","","","","","","","","","","","","","", "", ""};
    String source;
    String result;
    Button btnConvert;
    TextView sourceText;
    TextView resultText;

    private static final String TAG = "RESPLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnConvert = (Button) findViewById(R.id.btnConvert);
        sourceText = (EditText) findViewById(R.id.sourceValue);
        resultText = (EditText) findViewById(R.id.resultValue);

        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bills);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.sourceBill);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bills);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner2 = (Spinner) findViewById(R.id.resultBill);
        spinner2.setAdapter(adapter);
        spinner2.setSelection(1);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                source = bills[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                result = bills[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        OnClickListener oclBtnOk = new OnClickListener() {
           @Override
            public void onClick(View v) {
               if (sourceText.getText().toString().trim().length() == 0) {
                   Toast.makeText(getBaseContext(), "Не введено исходное значение", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   NetworkService.getInstance()
                           .getJSONApi()
                           .readJsonFromFileUri(source + "_" + result, "ultra")
                           .enqueue(new Callback<JsonObject>() {
                               @Override
                               public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                   Log.d(TAG, "Response: " + response);
                                   String jsonString = response.body().toString();
                                   Log.d(TAG, "jsonString: " + jsonString);
                                   double kf = Double.parseDouble(
                                           jsonString.substring(
                                                   jsonString.indexOf(':') + 1,
                                                   jsonString.indexOf('}')
                                           ));
                                   Gson gson = new Gson();

                                   Log.d(TAG, "jsonString: " + jsonString);
                                   double res = kf * (Double.parseDouble(sourceText.getText().toString()));

                                   resultText.setText(String.format("%.3f", res));
                                   Log.i(TAG, String.valueOf(kf));

                               }

                               @Override
                               public void onFailure(Call<JsonObject> call, Throwable t) {
                                   Toast.makeText(getBaseContext(), "Ошибка! Проверьте подключение к сети", Toast.LENGTH_SHORT).show();
                                   t.printStackTrace();
                               }
                           });

               }

            }
        };
        btnConvert.setOnClickListener(oclBtnOk);



    }



}

