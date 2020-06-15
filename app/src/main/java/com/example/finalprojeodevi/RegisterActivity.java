package com.example.finalprojeodevi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojeodevi.api.ApiService;
import com.example.finalprojeodevi.api.ApiUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private TextView register_username;
    private TextView register_pass;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn = findViewById(R.id.buttonLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
            }
        });
    }


    public void sendUserToLoginActivity(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void customerRegister(View v) {
        register_username = findViewById(R.id.kullaniciAdiInput);
        register_pass = findViewById(R.id.sifreInput);

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiUrl.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apis = retrofit.create(ApiService.class);
        String username = register_username.getText().toString();
        String password = register_pass.getText().toString();
        Call<CustomerRegisterResponse> call = apis.registerCustomer( username, password);
        call.enqueue(new Callback<CustomerRegisterResponse>() {
            @Override
            public void onResponse(Call<CustomerRegisterResponse> call, Response<CustomerRegisterResponse> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this,"Kayıt Başarılı Bir Şekilde Oluşturuldu!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String resp = null;
                    String errorMessage = "";
                    try {
                        resp = response.errorBody().string();
                        JSONObject jObject = new JSONObject(resp);
                        errorMessage = jObject.getString("error");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerRegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"Hata", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
