package org.firstinspires.ftc.teamcode.subsystem;

import org.firstinspires.ftc.teamcode.command.Command;
import org.firstinspires.ftc.teamcode.command.CommandScheduler;

public class Robot {

    public static boolean isDisabled = false;

    public void reset() {
        CommandScheduler.getInstance().reset();
    }

    public void run() {
        CommandScheduler.getInstance().run();
    }

    public void schedule(Command... commands) {
        CommandScheduler.getInstance().schedule(commands);
    }

    public void register(Subsystem... subsystems) {
        CommandScheduler.getInstance().registerSubsystem(subsystems);
    }

    public static void disable() {
        isDisabled = true;
    }

    public static void enable() {
        isDisabled = false;
    }
}
