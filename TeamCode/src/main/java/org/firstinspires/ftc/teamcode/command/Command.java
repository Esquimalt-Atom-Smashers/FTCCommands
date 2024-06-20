package org.firstinspires.ftc.teamcode.command;

import org.firstinspires.ftc.teamcode.subsystem.Subsystem;

import java.util.Set;

public interface Command {

    default void initialize() {
    }

    default void execute() {
    }

    default void end(boolean interrupted) {
    }

    default boolean isFinished() {
        return false;
    }

    Set<Subsystem> getRequirements();

    default boolean hasRequirements(Subsystem requirement) {
        return getRequirements().contains(requirement);
    }

    default void schedule(boolean interruptible) {
        CommandScheduler.getInstance().schedule(interruptible, this);
    }

    default void schedule() {
        schedule(true);
    }

    default void cancel() {
        CommandScheduler.getInstance().cancel(this);
    }

    default boolean isScheduled() {
        return CommandScheduler.getInstance().isScheduled(this);
    }

    default boolean runsWhenDisabled() {
        return false;
    }

    default String getName() {
        return this.getClass().getSimpleName();
    }

}
