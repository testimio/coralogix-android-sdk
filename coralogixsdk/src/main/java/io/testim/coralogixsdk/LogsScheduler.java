package io.testim.coralogixsdk;


import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.atomic.AtomicBoolean;

class LogsScheduler {

    private static final long TIME_THRESH_HOLD = 10 * 1000;
    private static final long QUANTITY_THRESH_HOLD = 50;
    private static final java.lang.String TAG = LogsScheduler.class.getSimpleName();
    private final AtomicBoolean threadLock;
    private final AtomicBoolean timerStart;
    private boolean shouldStartTimer;
    private Handler handlerProcessData;
    private HandlerThread handlerProcessDataThread;

    @Mode
    private final int mode;
    private final LogsSchedulerEvents logsSchedulerEvents;
    private int entriesCounter;

    public LogsScheduler(@Mode int mode, LogsSchedulerEvents logsSchedulerEvents){
        this.mode = mode;
        this.logsSchedulerEvents = logsSchedulerEvents;
        threadLock = new AtomicBoolean(false);
        timerStart = new AtomicBoolean(false);
        entriesCounter = 0;
    }

    private void clear(){
        timerStart.set(false);
        entriesCounter = 0;
    }

    public void countEntrie(){
        if(this.mode == Mode.STREAM){
            onThreshHoldDone();
            return;
        }
        startTimer();
        entriesCounter = entriesCounter + 1;
        if(entriesCounter > QUANTITY_THRESH_HOLD){
            onThreshHoldDone();
        }
    }

    private void onThreshHoldDone(){
        if(logsSchedulerEvents != null){
            logsSchedulerEvents.onScheduleEvent();
        }
        clear();
        destroy();
    }

    private void startTimer(){
        if(threadLock.get()){
            shouldStartTimer = true;
            return;
        }

        if(timerStart.getAndSet(true)){
            return;
        }

        shouldStartTimer = false;
        if(handlerProcessData == null && handlerProcessDataThread == null){
            setmHandlerProcessDataThread();
            handlerProcessData.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onThreshHoldDone();
                }
            },TIME_THRESH_HOLD);
        }
    }
    private void setmHandlerProcessDataThread() {
        if(threadLock.get()){
            return;
        }
        threadLock.set(true);
        try {
            if (handlerProcessDataThread != null && handlerProcessDataThread.isAlive()) {
                return;
            }
            handlerProcessDataThread = new HandlerThread(TAG);
            handlerProcessDataThread.setPriority(Thread.NORM_PRIORITY);
            handlerProcessDataThread.start();
            handlerProcessData = new Handler(handlerProcessDataThread.getLooper());
        } catch (Exception e) {
            e.printStackTrace();
        }
        threadLock.set(false);
    }

    private void destroy() {
        if(threadLock.get()){
            return;
        }
        threadLock.set(true);
        try {
            if(handlerProcessData != null){
                handlerProcessData.removeCallbacks(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(handlerProcessDataThread != null){
                handlerProcessDataThread.quitSafely();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }finally {
            handlerProcessData = null;
            handlerProcessDataThread = null;
        }
        threadLock.set(false);
        if(shouldStartTimer){
            startTimer();
        }
    }
}
