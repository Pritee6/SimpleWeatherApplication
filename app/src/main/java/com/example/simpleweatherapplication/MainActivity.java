package com.example.simpleweatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button btnShowWeather;
    private TextView txtDiplay1, txtDiplay2;
    private EditText edtCityName;

    String url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
    String apikey = "ce123a5181a3429d67c62617a099b934";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init()
    {
        btnShowWeather = findViewById(R.id.btnShowWeather);
        txtDiplay1 = findViewById(R.id.txtDiplay1);
        txtDiplay2 = findViewById(R.id.txtDiplay2);

        edtCityName = findViewById(R.id.edtCityName);

        btnShowWeather.setOnClickListener(new BtnShowWeatherOnClickListener());
    }

    public class BtnShowWeatherOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            weatherapi myapi = retrofit.create(weatherapi.class);
            Call<Example> exampleCall = myapi.getweather(edtCityName.getText().toString().trim(),apikey);
            exampleCall.enqueue(new Callback<Example>() {
                @Override
                public void onResponse(Call<Example> call, Response<Example> response) {
                        if(response.code() == 404)
                            Toast.makeText(MainActivity.this, "Incorrect City Name", Toast.LENGTH_LONG).show();

                        else if(!response.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this,response.code(),Toast.LENGTH_LONG).show();
                        }

                        Example mydata = response.body();
                        Main main = mydata.getMain();

                        Double temp = main.getTemp();
                        Integer temperature = (int)(temp-273.15);

                        Integer humidity = main.getHumidity();

                        txtDiplay1.setText("Temperature : "+String.valueOf(temperature)+"C");
                        txtDiplay2.setText("Humidity : "+String.valueOf(humidity)+"%");
                        }

                @Override
                public void onFailure(Call<Example> call, Throwable t) {
                        Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}