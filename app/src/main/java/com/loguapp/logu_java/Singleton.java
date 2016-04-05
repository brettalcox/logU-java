package com.loguapp.logu_java;

/**
 * Created by BA042808 on 4/5/2016.
 */
public class Singleton {
    private static Singleton singleton = new Singleton( );
    private String test;
    private Boolean shouldUpdateDash = false;
    private Boolean shouldUpdateStats = false;

    private Singleton(){ }

    public static Singleton getInstance( ) {
        return singleton;
    }

    protected void setTest(String data) {
        test = data;
    }

    protected String getTest() {
        return test;
    }

    protected void setShouldUpdateDash(Boolean value) {
        shouldUpdateDash = value;
    }

    protected Boolean getShouldUpdateDash() {
        return shouldUpdateDash;
    }

    protected void setShouldUpdateStats(Boolean value) {
        shouldUpdateStats = value;
    }

    protected Boolean getShouldUpdateStats() {
        return shouldUpdateStats;
    }
}
