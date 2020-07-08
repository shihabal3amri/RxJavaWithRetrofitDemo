package com.example.rxjavademo;

import io.reactivex.rxjava3.core.Maybe;
import retrofit2.http.GET;

public interface ApiService {
    @GET("user")
    Maybe<User> getUser();
}
