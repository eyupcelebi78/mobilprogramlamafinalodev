package com.example.finalprojeodevi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class LoginActivity extends AppCompatActivity {

    private TextView txtKullaniciAdi;
    private TextView txtSifre;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn = findViewById(R.id.register_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
            }
        });
    }

    public void sendUserToRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void customerLogin(View v) {
        txtKullaniciAdi = findViewById(R.id.edtKullaniciAdi);
        txtSifre = findViewById(R.id.edtSifre);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiUrl.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apis = retrofit.create(ApiService.class);
        String username = txtKullaniciAdi.getText().toString();
        String password = txtSifre.getText().toString();
        Call<CustomerLoginResponse> call = apis.loginCustomer( username, password);
        call.enqueue(new Callback<CustomerLoginResponse>() {
            @Override
            public void onResponse(Call<CustomerLoginResponse> call, Response<CustomerLoginResponse> response) {

                if (response.isSuccessful()) {
                    if(response.errorBody() != null){
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
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(LoginActivity.this,"Giriş Başarılı!", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent gecis = new Intent(LoginActivity.this, UserListActivity.class);
                                startActivity(gecis);
                                finish();
                            }
                        }, 2000);
                    }

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
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerLoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Hata", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
