package com.android.common.http;

/**
 * Exception style class encapsulating Okhttp errors
 */
public class OkhttpException extends Exception {
    public final NetworkResponse refactorResponse;

    public OkhttpException() {
        refactorResponse = null;
    }

    public OkhttpException(NetworkResponse response, String exceptionMessage) {
        super(exceptionMessage);
        refactorResponse = response;
    }

    public OkhttpException(String exceptionMessage) {
       super(exceptionMessage);
        refactorResponse = null;
    }

    public OkhttpException(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
        refactorResponse = null;
    }

    public OkhttpException(Throwable cause) {
        super(cause);
        refactorResponse = null;
    }
}
