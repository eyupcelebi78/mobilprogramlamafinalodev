package com.example.finalprojeodevi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojeodevi.api.ApiService;
import com.example.finalprojeodevi.api.ApiUrl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserListActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ArrayList<String> imageNames = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();
    private ArrayList<Integer> IDs = new ArrayList<>();
    private int myPage;
    private int my_total_page;
    private List<CustomerDatum> dat;
    private Button newCustomerButton;
    private RecyclerView recyclerView;
    private Object apiUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);
        myPage = 1;
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.addOnScrollListener(new CustomScrollListener());

        newCustomerButton = findViewById(R.id.newCustomer);
        newCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserListActivity.this, UserCreateActivity.class);
                startActivity(intent);
            }
        });

        initUserList(myPage);
    }

    public void openUserDetail(){
        Intent intent = new Intent(UserListActivity.this, UserDetailActivity.class);
        Bundle b = new Bundle();
        b.putInt("userid", 1); //Your id
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }
    public void initUserList(int page) {

        Toast.makeText(UserListActivity.this, "Listenin devamı yükleniyor", Toast.LENGTH_SHORT).show();

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiUrl.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        ApiService apis = retrofit.create(ApiService.class);
        Call<CustomerListResponse> call = apis.getCustomers(page);
        call.enqueue(new Callback<CustomerListResponse>() {
            @Override
            public void onResponse(Call<CustomerListResponse> call, Response<CustomerListResponse> response) {
                my_total_page = response.body().totalPages;
                dat = response.body().data;

                for (int i = 0; i < dat.size(); i++){
                    imageNames.add(dat.get(i).firstName);
                    imageUrls.add(dat.get(i).avatar);
                    IDs.add(dat.get(i).id);
                }

                initRecyclerView();
            }

            @Override
            public void onFailure(Call<CustomerListResponse> call, Throwable t) {
                Log.d("snow", t.getMessage().toString());
            }
        });
    }
    private void initRecyclerView(){
        RecyclerView recycleView = findViewById(R.id.recycler_view);
        UserListViewAdapter adapter = new UserListViewAdapter(this, imageNames,imageUrls,IDs);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        myPage++;
    }

    public class CustomScrollListener extends RecyclerView.OnScrollListener {

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE) {
                if(myPage <= my_total_page)
                {
                    Log.d("-----","end");
                    initUserList(myPage);
                }
            }
        }

    }

}
