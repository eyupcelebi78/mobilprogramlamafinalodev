package com.example.finalprojeodevi;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojeodevi.api.ApiService;
import com.example.finalprojeodevi.api.ApiUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserDetailActivity extends AppCompatActivity {

    private Integer user_id;
    private Customer customer;

    private EditText detail_ad, detail_soyad;
    private ImageView profileImage;
    private Button updateBtn;
    private Button deleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail);

        detail_ad = findViewById(R.id.detail_ad);
        detail_soyad = findViewById(R.id.detail_soyad);
        profileImage = findViewById(R.id.imageView);
        updateBtn = findViewById(R.id.updateButton);
        deleteBtn = findViewById(R.id.deleteButton);

        Bundle b = getIntent().getExtras();
        user_id = -1; // or other values
        if(b != null)
        {
            user_id = b.getInt("userid");
            initCustomer();
        }

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCustomer(v);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCustomer(v);
            }
        });
    }


    public void initCustomer() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiUrl.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        ApiService apis = retrofit.create(ApiService.class);
        Call<Customer> call = apis.getCustomer(user_id);
        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                CustomerDatum customer = response.body().data;
                detail_ad.setText(customer.firstName);
                detail_soyad.setText(customer.lastName);
                Picasso.get().load(customer.avatar).into(profileImage);
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                Log.d("snow", t.getMessage().toString());
            }
        });
    }

    public void updateCustomer(View v){
        String firstName = detail_ad.getText().toString();
        String lastName = detail_soyad.getText().toString();

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiUrl.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apis = retrofit.create(ApiService.class);
        CustomerUpdateDatum cd = new CustomerUpdateDatum();
        cd.name = firstName;
        cd.job = lastName;

        Call<CustomerUpdateResponse> call = apis.updateCustomer( user_id, cd);
        call.enqueue(new Callback<CustomerUpdateResponse>() {
            @Override
            public void onResponse(Call<CustomerUpdateResponse> call, Response<CustomerUpdateResponse> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(UserDetailActivity.this,"Güncelleme Başarılı", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(UserDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CustomerUpdateResponse> call, Throwable t) {
                Toast.makeText(UserDetailActivity.this,"Sistemsel Hata", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteCustomer(View v){

        final Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiUrl.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apis = retrofit.create(ApiService.class);
        CustomerUpdateDatum cd = new CustomerUpdateDatum();

        Call<String> call = apis.deleteCustomer(user_id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(UserDetailActivity.this,"Silme Başarılı Şekilde Gerçekleşti!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(UserDetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(UserDetailActivity.this,"Hata", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
