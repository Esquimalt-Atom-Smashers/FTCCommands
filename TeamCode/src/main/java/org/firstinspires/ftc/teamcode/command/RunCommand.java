package org.firstinspires.ftc.teamcode.command;

import org.firstinspires.ftc.teamcode.subsystem.Subsystem;

public class RunCommand extends CommandBase {

    protected final Runnable toRun;

    public RunCommand(Runnable toRun, Subsystem... requirements) {
        this.toRun = toRun;
        
        addRequirement(requirements);
    }
    
    @Override
    public void execute() {
        toRun.run();
    }
}
