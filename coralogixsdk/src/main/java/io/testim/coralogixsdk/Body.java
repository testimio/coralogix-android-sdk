package io.testim.coralogixsdk;

import java.util.concurrent.CopyOnWriteArrayList;

class Body {
    private final String privateKey;
    private final String applicationName;
    private final String subsystemName;
    private final String computerName;
    private CopyOnWriteArrayList<LogEntrie> logEntries;

    public Body(String privateKey, String applicationName, String subsystemName, String computerName) {
        this.privateKey = privateKey;
        this.applicationName = applicationName;
        this.subsystemName = subsystemName;
        this.computerName = computerName;
        logEntries = new CopyOnWriteArrayList<>();
    }

    public Body(Body body) {
        this(body.privateKey, body.applicationName, body.subsystemName, body.computerName);
        logEntries = new CopyOnWriteArrayList<>(body.logEntries);
    }

    public synchronized void clearAll(){
        logEntries.clear();
    }


    public void log(long timeStamp, String tag, String msg, int severity){
        LogEntrie logEntrie = new LogEntrie(timeStamp, severity, msg, tag);
        addEntrie(logEntrie);
    }

    private synchronized void addEntrie(LogEntrie logEntrie){
        logEntries.add(logEntrie);
    }


    @Override
    public String toString() {
        return "Body{" +
                "logEntries=" + logEntries +
                '}';
    }
}
