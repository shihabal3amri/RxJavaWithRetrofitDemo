package com.example.rxjavademo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private Disposable userDisposable;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.tvPhoneNo)
    TextView tvPhoneNo;
    @BindView(R.id.tvHouseNo)
    TextView tvHouseNo;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        userDisposable = getUser().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .defaultIfEmpty(new User())
                .subscribe(user -> {
                    progressBar.setVisibility(View.GONE);
                    if(user != null) {
                        layout.setVisibility(View.VISIBLE);
                        tvName.setText("Name: " + user.getName());
                        tvAge.setText("Age: " + user.getAge());
                        tvPhoneNo.setText("Phone No: " + user.getAddress().getPhoneNo());
                        tvHouseNo.setText("House No: " + user.getAddress().getHouseNo());
                    } else {
                        Toast.makeText(this, "User is empty", Toast.LENGTH_LONG).show();
                    }
                }, error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                });

    }

    private Maybe<User> getUser() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        return apiService.getUser().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    protected void onDestroy() {
        if(userDisposable != null) {
            userDisposable.dispose();
        }
        super.onDestroy();
    }
}
