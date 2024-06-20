package org.firstinspires.ftc.teamcode.command;

import java.util.concurrent.TimeUnit;

public class WaitCommand extends CommandBase {

    protected Timer timer;

    public WaitCommand(long millis) {
        timer = new Timer(millis, TimeUnit.MILLISECONDS);
        setName(name + ": " + millis + " milliseconds");
    }

    @Override
    public void initialize() {
        timer.start();
    }

    @Override
    public void end(boolean interrupted) {
        timer.pause();
    }

    @Override
    public boolean isFinished() {
        return timer.done();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
