package io.testim.coralogixsdk;



import android.annotation.SuppressLint;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoralogixLogger implements LogsSchedulerEvents {

    private static final String TAG = CoralogixLogger.class.getSimpleName();
    private static CoralogixLogger INSTANCE;
    private final Body body;
    private final ApiInterface apiService;
    private final LogsScheduler logsScheduler;

    public static void init(String privateKey, String applicationName, String subsystemName, String computerName, @Mode int mode){
        if (INSTANCE == null) {
            INSTANCE = new CoralogixLogger(privateKey, applicationName, subsystemName, computerName, mode);
        }
    }

    private CoralogixLogger(String privateKey, String applicationName, String subsystemName, String computerName, @Mode int mode) {
        body = new Body(privateKey, applicationName, subsystemName, computerName);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        logsScheduler = new LogsScheduler(mode, this);
    }

    @SuppressLint("LogNotTimber")
    private static boolean checkInitialized() {
        if(INSTANCE == null) {
            Log.e(TAG, "CoralogixLogger not initialized");
            return false;
        }
        return true;
    }

    public static void log(long timeStamp, String tag, String msg, int severity) {
        if(checkInitialized()) {
            INSTANCE.body.log(timeStamp, tag, msg, severity);
            INSTANCE.logsScheduler.countEntrie();
        }
    }

    private void sendLogs(){
        Body copyBody = new Body(body);
        body.clearAll();

        Call<String> call =  apiService.sendLogs(copyBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call call, Response response) {}

            @Override
            public void onFailure(Call<String> call, Throwable t) {}
        });
    }

    @Override
    public void onScheduleEvent() {
        sendLogs();
    }

}
