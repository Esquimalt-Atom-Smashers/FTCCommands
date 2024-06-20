package org.firstinspires.ftc.teamcode.command;

import org.firstinspires.ftc.teamcode.command.CommandBase;

import java.util.function.BooleanSupplier;

public class WaitUntilCommand extends CommandBase {

    private final BooleanSupplier condition;
    private final boolean needsToStart;
    private boolean hasStarted;

    public WaitUntilCommand(BooleanSupplier condition, boolean needsToStart) {
        this.condition = condition;
        this.needsToStart = needsToStart;
        this.hasStarted = false;
    }

    @Override
    public void initialize() {
        hasStarted = true;
    }

    @Override
    public boolean isFinished() {
        if (needsToStart) return hasStarted && condition.getAsBoolean();
        else return condition.getAsBoolean();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
