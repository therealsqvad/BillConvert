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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    String[] data = {"EUR", "USD", "RUB"};
    String source;
    String result;
    Button btnConvert;
    TextView textView;

    private static final String TAG = "RESPLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnConvert = (Button) findViewById(R.id.btnConvert);
        textView = (TextView) findViewById(R.id.textView3);

        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Title");
        // выделяем элемент
        spinner.setSelection(2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setAdapter(adapter);
        // заголовок
        spinner2.setPrompt("Title");
        // выделяем элемент
        spinner2.setSelection(0);
        // устанавливаем обработчик нажатия
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText(getBaseContext(), "Source = " + data[position], Toast.LENGTH_SHORT).show();
                source = data[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText(getBaseContext(), "Result = " + data[position], Toast.LENGTH_SHORT).show();
                result = data[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        OnClickListener oclBtnOk = new OnClickListener() {
           @Override
            public void onClick(View v) {
                /*Log.d(TAG, "Button click");
                NetworkService.getInstance()
                        .getJSONApi()
                        .getPostWithID(source+ "_" + result)
                        .enqueue(new Callback<Post>() {
                            @Override
                            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                                Log.d(TAG, "Response: "+response);

                                Post post = response.body();
                               // Log.d(TAG, "results: "+post.getResults());


                                Map<String, Double> langs = post.getLangs();
                                Log.d(TAG, "Resp ok"+langs);

                                for (Map.Entry<String, Double> entry : langs.entrySet()) {

                                    textView.append(entry.getKey()+" : "+ entry.getValue()+"\n");

                                }

                               // textView.append(post + "\n");
                            }

                            @Override
                            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {


                            }
                        });
*/
               NetworkService.getInstance()
                       .getJSONApi()
                       .readJsonFromFileUri(source+ "_" + result, "ultra")
                       .enqueue(new Callback<JsonObject>() {
                           @Override
                           public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                               Log.d(TAG, "Response: "+response);
                               String jsonString = response.body().toString();
                               Log.d(TAG, "jsonString: "+jsonString);
                                double kf = Double.parseDouble(
                                        jsonString.substring(
                                                jsonString.indexOf(':') + 1,
                                                jsonString.indexOf('}')
                                        ));
                               Gson gson = new Gson();

                               Log.d(TAG, "jsonString: "+jsonString);

                               Log.i(TAG, String.valueOf(kf));
                           }

                           @Override
                           public void onFailure(Call<JsonObject> call, Throwable t) {
                               textView.append("Error occurred while getting request!");
                               t.printStackTrace();
                           }
                       });




            }
        };
        btnConvert.setOnClickListener(oclBtnOk);



    }



}

