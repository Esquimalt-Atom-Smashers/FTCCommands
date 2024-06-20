package org.firstinspires.ftc.teamcode.command;

import org.firstinspires.ftc.teamcode.subsystem.Subsystem;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class FunctionalCommand extends CommandBase {

    protected final Runnable onInit;
    protected final Runnable onExecute;
    protected final Consumer<Boolean> onEnd;
    protected final BooleanSupplier isFinished;

    public FunctionalCommand(Runnable onInit, Runnable onExecute, Consumer<Boolean> onEnd, BooleanSupplier isFinished, Subsystem... requirements) {
        this.onInit = onInit;
        this.onExecute = onExecute;
        this.onEnd = onEnd;
        this.isFinished = isFinished;

        addRequirements(requirements);
    }

    @Override
    public void initialize() {
        onInit.run();
    }

    @Override
    public void execute() {
        onExecute.run();
    }

    @Override
    public void end(boolean interrupted) {
        onEnd.accept(interrupted);
    }

    @Override
    public boolean isFinished() {
        return isFinished.getAsBoolean();
    }
}
