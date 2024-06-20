package org.firstinspires.ftc.teamcode.subsystem;

import org.firstinspires.ftc.teamcode.command.Command;
import org.firstinspires.ftc.teamcode.command.CommandScheduler;

public interface Subsystem {

    // This method is called periodically by the CommandScheduler.
    // Be consistent about what responsibilities are handled by Commands
    // and what is handled here
    default void periodic() {

    }

    default void setDefaultCommand(Command defaultCommand) {
        CommandScheduler.getInstance().setDefaultCommand(this, defaultCommand);
    }

    default Command getDefaultCommand() {
        return CommandScheduler.getInstance().getDefaultCommand(this);
    }

    default Command getCurrentCommand() {
        return CommandScheduler.getInstance().requiring(this);
    }

    default void register() {

    }
}
