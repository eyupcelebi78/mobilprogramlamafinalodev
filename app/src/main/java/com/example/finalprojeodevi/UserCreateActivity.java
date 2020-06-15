package com.example.finalprojeodevi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class UserCreateActivity extends AppCompatActivity {

    private EditText edtKullaniciAdi;
    private EditText edtKullaniciSoyad;
    private Button createBtn;
    private Button cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_create);

        edtKullaniciAdi = findViewById(R.id.kullaniciAdi);
        edtKullaniciSoyad = findViewById(R.id.kullaniciSoyad);
        createBtn = findViewById(R.id.createButton);
        cancelBtn = findViewById(R.id.cancelButton);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCustomer(v);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void createCustomer(View v){
        String name = edtKullaniciAdi.getText().toString();
        String job = edtKullaniciSoyad.getText().toString();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiUrl.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apis = retrofit.create(ApiService.class);

        Call<CustomerCreateResponse> call = apis.createCustomer( name, job);
        call.enqueue(new Callback<CustomerCreateResponse>() {
            @Override
            public void onResponse(Call<CustomerCreateResponse> call, Response<CustomerCreateResponse> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(UserCreateActivity.this,"Kullanıcı başarılı bir şekilde eklendi!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(UserCreateActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerCreateResponse> call, Throwable t) {
                Toast.makeText(UserCreateActivity.this,"Hata", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
