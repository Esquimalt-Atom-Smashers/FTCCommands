package org.firstinspires.ftc.teamcode.command;

import org.firstinspires.ftc.teamcode.subsystem.Subsystem;

public class InstantCommand extends CommandBase {
    
    protected final Runnable toRun;
    
    public InstantCommand(Runnable toRun, Subsystem... requirements) {
        this.toRun = toRun;
        
        addRequirement(requirements);
    }
    
    @Override
    public void execute() {
        toRun.run();
    }
    
    @Override
    public boolean isFinished() {
        return true;
    }
}
