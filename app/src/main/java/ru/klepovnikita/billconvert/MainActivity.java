package ru.klepovnikita.billconvert;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnConvert = findViewById(R.id.btnConvert);
        sourceText = (EditText) findViewById(R.id.sourceValue);
        resultText = (EditText) findViewById(R.id.resultValue);
        sourceBillsList = findViewById(R.id.sourceBill);
        resultBillsList = findViewById(R.id.resultBill);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bills);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bills);
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
                if (sourceText.getText().toString().length() != 0) {
                    getCurrency();
                }
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
                if (sourceText.getText().toString().length() != 0) {
                    getCurrency();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        OnClickListener oclBtnConvert = new OnClickListener() {
           @Override
            public void onClick(View v) {
               if (sourceText.getText().toString().length() == 0) {
                   Toast.makeText(getBaseContext(), "Не введено исходное значение", Toast.LENGTH_SHORT).show();
               }
               else
               {
                    getCurrency();
               }

            }
        };
        btnConvert.setOnClickListener(oclBtnConvert);

        sourceText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (sourceText.getText().toString().length() != 0 && getCurrentFocus()==sourceText) {
                    getCurrency();
                }
            }
        });
    }

    // функция отправки запроса к API для получения курса
    private void getCurrency() {
        final double[] kf = {-1};
        NetworkService.getInstance(this)
                .getJSONApi()
                .readJsonFromFileUri(source + "_" + result, "ultra")
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        try {
                            // не используем возможность GSON для распарсивания JSON, полученного от сервера
                            // так как данная API для каждого курса валют формирует JSON типа {"USD_RUB"}, следовательно,
                            // нам необходимо было бы огромное количество POJO, проще разбирать ответ от сервера как строку
                            String jsonString = response.body().toString();
                            if (jsonString=="{}") { // если серверу отправить некорректный запрос, он вернет {}
                                throw new Exception("Не существует исходной или целевой валюты");
                            }
                            kf[0] = Double.parseDouble(  // коэффициент текущего курса
                                    jsonString.substring(                  // ответ от сервера вида {"USD_RUB":65.012}
                                            jsonString.indexOf(':') + 1,    // нас интересует лишь курс, который расположен
                                            jsonString.indexOf('}')         // между : и }. Пока API существует в том виде,
                                    ));                                     // в котором существует - нас устраивает данный подход
                            double res = kf[0] * (Double.parseDouble(sourceText.getText().toString()));

                            resultText.setText(String.format("%.3f", res));
                        }
                        catch (Exception e) {
                            Toast.makeText(getBaseContext(), "Ошибка " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                    if(kf[0]==-1) { // логика работы следующая: курс никогда не может быть -1, следовательно это значение
                                    // "зарезервировано" под некорректное. Так как при использовании закэшированного значения
                                    // вызывался колбэк и response и failure и сообщение об отсутствии соединения появлялось
                                    // и при корректном подсчете закэшерованных валют
                        Toast.makeText(getBaseContext(), "Ошибка! Проверьте подключение к сети", Toast.LENGTH_SHORT).show();
                        resultText.setText("");
                    }// очищаем поле с результатом, чтобы в случае ошибки, мы не наблюдали соответствия
                        // корректного значения прошлой валюты, например, запускаем приложение без интернета
                        // у нас в кэше есть некая валюта (1 USD = 65 RUB), переключаем на EUR и чтобы не увидеть
                        // 1 EUR = 65 RUB, а в toast сообщение об отсутствии соединения
                        t.printStackTrace();
                    }
                });
    }
}

