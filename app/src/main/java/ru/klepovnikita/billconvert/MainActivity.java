package ru.klepovnikita.billconvert;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String[] bills = {"ALL","AMD","ANG","AOA","ARS",    // если получать список валют с сервера,
            "AUD", "AWG", "AZN", "BAM","BBD","BDT",     // то для каждой валюты необходим свой POJO
            "BGN","BHD","BIF","BND","BOB","BRL","BSD",  // объект. Поэтому было решено использовать
            "BTC","BTN","BWP","BYN","BYR","BZD","CAD",  // массив для хранения всех доступных валют
            "CDF","CHF","CLP","CNY","COP","CRC","CUP",
            "CVE","CZK","DJF","DKK","DOP","DZD","EGP",
            "ERN","ETB","EUR","FJD","FKP","GBP","GEL",
            "GHS","GIP","GMD","GNF","GTQ","GYD","HKD",
            "HNL","HRK","HTG","HUF","IDR","ILS","INR",
            "IQD","IRR","ISK","JMD","JOD","JPY","KES",
            "KGS","KHR","KMF","KPW","KRW","KWD","KYD",
            "KZT","LAK","LBP","LKR","LRD","LSL","LVL",
            "LYD","MAD","MDL","MGA","MKD","MMK","MNT",
            "MOP","MRO","MUR","MVR","MWK","MXN","MYR",
            "MZN","NAD","NGN","NIO","NOK","NPR","NZD",
            "OMR","PAB","PEN","PGK","PHP","PKR","PLN",
            "PYG","QAR","RON","RSD","RUB","RWF","SAR",
            "SBD","SCR","SDG","SEK","SGD","SHP","SLL",
            "SOS","SRD","STD","SYP","SZL","THB","TJS",
            "TMT","TND","TOP","TRY","TTD","TWD","TZS",
            "UAH","UGX","USD","UYU","UZS","VEF","VND",
            "VUV","WST","XAF","XCD","XDR","XOF","XPF",
            "YER","ZAR","ZMW"};
    String source;
    String result;
    Button btnConvert;
    TextView sourceText, resultText;
    Spinner sourceBillsList, resultBillsList;

    private static final String TAG = "RESPLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnConvert = (Button) findViewById(R.id.btnConvert);
        sourceText = (EditText) findViewById(R.id.sourceValue);
        resultText = (EditText) findViewById(R.id.resultValue);
        sourceBillsList = (Spinner) findViewById(R.id.sourceBill);
        resultBillsList = (Spinner) findViewById(R.id.resultBill);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bills);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bills);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceBillsList.setAdapter(adapter);
        sourceBillsList.setSelection(0);
        resultBillsList.setAdapter(adapter);
        resultBillsList.setSelection(1);
        sourceBillsList.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                source = bills[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        resultBillsList.setOnItemSelectedListener(new OnItemSelectedListener() {
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
                    getCurrency();
               }

            }
        };
        btnConvert.setOnClickListener(oclBtnOk);
    }

    // функция отправки запроса к API для получения курса
    private void getCurrency() {
        NetworkService.getInstance()
                .getJSONApi()
                .readJsonFromFileUri(source + "_" + result, "ultra")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            String jsonString = response.body().toString();
                            if (jsonString=="{}") {
                                throw new Exception("Не существует исходной или целевой валюты");
                            }
                            double kf = Double.parseDouble(  // коэффициент текущего курса
                                    jsonString.substring(
                                            jsonString.indexOf(':') + 1,
                                            jsonString.indexOf('}')
                                    ));
                            double res = kf * (Double.parseDouble(sourceText.getText().toString()));
                            resultText.setText(String.format("%.3f", res));
                        }
                        catch (Exception e) {
                            Toast.makeText(getBaseContext(), "Ошибка " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getBaseContext(), "Ошибка! Проверьте подключение к сети", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
    }
}

