package io.testim.coralogixsdk;


import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;

interface ApiInterface {
    @Headers({"Content-Type: application/json", "Cache-Control: max-age=640000"})
    @POST("v1/logs/")

    Call<String> sendLogs(@retrofit2.http.Body Body body);

}
