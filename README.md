#### Install
```
implementation "com.github.testimio:coralogix-android-sdk:0.1"
```

#### Use
Init from your Main Application:
```
CoralogixLogger.init(
            "private-key",
            "app-name",
            "subsystem-name",
            "computer-name",
            Mode.CHUNCKS
        )
```

And log `CoralogixLogger.log(...)`

#### Other
##### Timber
Use this snippet to create a timber tree:
```
class CoralogixLogTree : Timber.DebugTree() {
    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.VERBOSE && SKIP_VERBOSE) {
            return
        }

        if (t == null) {
            val coralogixSeverity = when (priority) {
                Log.VERBOSE -> Severity.VERBUS
                Log.DEBUG -> Severity.DEBUG
                Log.INFO -> Severity.INFO
                Log.WARN -> Severity.WARN
                Log.ERROR -> Severity.ERROR
                else -> {
                    // We are not using Timber here to not create some recursive method calling
                    Log.e("CoralogixLogTree", "Log isn't being reported due to unknown severity")
                    return
                }
            }

            CoralogixLogger.log(System.currentTimeMillis(), tag, "($tag) $message", coralogixSeverity)
        } else {
            CoralogixLogger.log(System.currentTimeMillis(), t.message, Arrays.toString(t.stackTrace), Severity.CRITICAL)
        }
    }

    companion object {
        private const val SKIP_VERBOSE = true
    }
}
```
