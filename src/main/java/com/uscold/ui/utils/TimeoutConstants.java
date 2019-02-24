package com.uscold.ui.utils;

public class TimeoutConstants {

    private TimeoutConstants() {
        throw new IllegalStateException("Utility class!");
    }

    public static final int GET_ELEMENT_TIMEOUT = 60;
    public static final int PAGE_LOAD_TIMEOUT = 90;
    public static final int LONG_TIMEOUT = 150;
    public static final int TERMINAL_DEFAULT_TIMEOUT = 30;
    public static final int TERMINAL_POLLING_TIMEOUT_MS = 250;
    public static final int SHORT_TIMEOUT = 7;
    public static final int EXTRA_SHORT_TIMEOUT = 1;
    public static final int GET_ELEMENT_TIMEOUT_MS = GET_ELEMENT_TIMEOUT * 1000;
    public static final int SHORT_TIMEOUT_MS = SHORT_TIMEOUT * 1000;
    public static final int LONG_TIMEOUT_MS = LONG_TIMEOUT * 1000;
}
