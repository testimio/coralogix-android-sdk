package io.testim.coralogixsdk;


class LogEntrie {

    private final long timestamp;
    private final
    int severity;
    private final String text;
    private String category;
    private final String className;
    private String methodName;

    public LogEntrie(long timestamp, int severity, String text, String className) {
        this.timestamp = timestamp;
        this.severity = severity;
        this.text = text;
        this.className = className;
    }

    public LogEntrie(long timestamp, int severity, String text, String category, String className, String methodName) {
        this.timestamp = timestamp;
        this.severity = severity;
        this.text = text;
        this.category = category;
        this.className = className;
        this.methodName = methodName;
    }


    @Override
    public String toString() {
        return "LogEntrie{" +
                ", text='" + text + '\'' +
                '}';
    }
}
