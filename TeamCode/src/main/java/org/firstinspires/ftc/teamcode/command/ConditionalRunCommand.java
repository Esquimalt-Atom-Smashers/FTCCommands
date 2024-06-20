package org.firstinspires.ftc.teamcode.command;

import org.firstinspires.ftc.teamcode.subsystem.Subsystem;

import java.util.function.BooleanSupplier;

public class ConditionalRunCommand extends CommandBase {

    protected final Command command1;
    protected final Command command2;
    protected final BooleanSupplier condition;

    public ConditionalRunCommand(Command command1, Command command2, BooleanSupplier condition, Subsystem... requirements) {
        this.command1 = command1;
        this.command2 = command2;
        this.condition = condition;
        
        addRequirement(requirements);
    }
    
    public ConditionalRunCommand(Command command, BooleanSupplier condition, Subsystem... requirements) {
        this(command, new RunCommand(() -> {}), condition, requirements);
    }

    @Override
    public void execute() {
        if (condition.getAsBoolean()) {
            command1.execute();
        } else {
            command2.execute();
        }
    }
}
