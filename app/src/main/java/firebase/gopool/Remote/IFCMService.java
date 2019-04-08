package firebase.gopool.Remote;

import firebase.gopool.models.FCMResponse;
import firebase.gopool.models.Sender;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAA6IlTuVc:APA91bGJkIVAz5-WH0ZYrq0TJ8HFftgOccsm7dLgPlEWN5_rJyLCavgqWgxwywTcH16NeGYPHb54cHZk6YSM_VEV4eijCA2zH40pywg2GqDxVCq5cl09tSb4Uw1n6w_MBgQFtVP4UWNm"
    })
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body Sender body);
}
