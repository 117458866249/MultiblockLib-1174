package com.qwq117458866249.multiblocklib.api;

public enum IOMode {
    INPUT,
    OUTPUT,
    BOTH;

    public static IOMode get(String string) {
        if (string.equals("input")) {
            return INPUT;
        } else if (string.equals("both")) {
            return BOTH;
        } else {
            return OUTPUT;
        }
    }
}
