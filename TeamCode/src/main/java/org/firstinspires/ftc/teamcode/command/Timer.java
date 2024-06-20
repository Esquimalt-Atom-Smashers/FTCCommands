package org.firstinspires.ftc.teamcode.command;

import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.concurrent.TimeUnit;

public class Timer {
    private ElapsedTime time;
    private long timerLength;
    private long pauseTime; // in nanoseconds, regardless of unit
    private TimeUnit unit;
    private boolean timerOn;

    public Timer(long timerLength, TimeUnit unit) {
        this.timerLength = timerLength;
        this.unit = unit;
        this.time = new ElapsedTime();
        time.reset();
    }

    public Timer(long timerLength) {
        this(timerLength, TimeUnit.SECONDS);
    }

    public void start() {
        time.reset();
        pauseTime = 0;
        timerOn = true;
    }

    public void pause() {
        if (timerOn) {
            pauseTime = time.nanoseconds();
            timerOn = false;
        }
    }

    public void resume() {
        if (!timerOn) {
            // we start the timer with a time in the past, since we're starting in the middle of the timer
            time = new ElapsedTime(System.nanoTime() - pauseTime);
            timerOn = true;
        }
    }

    public long elapsedTime() {
        if (timerOn) return time.time(unit);
        else return unit.convert(pauseTime, TimeUnit.NANOSECONDS);
    }

    public long remainingTime() {
        return timerLength - elapsedTime();
    }

    public boolean done() {
        return elapsedTime() >= timerLength;
    }

    public boolean isTimerOn() {
        return timerOn;
    }
}
