package io.testim.coralogixsdk;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@Retention(SOURCE)
@IntDef({
        Mode.STREAM,
        Mode.CHUNCKS,

})
public @interface Mode {
     int STREAM = 0;
     int CHUNCKS = 1;

}
