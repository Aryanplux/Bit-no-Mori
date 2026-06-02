package util;

public class Time {
    private static final long startTime = System.nanoTime();

    public static float getTime(){
        return (System.nanoTime() - startTime) * 1E-9f;
    }
}
