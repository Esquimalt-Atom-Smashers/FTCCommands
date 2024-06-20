package org.firstinspires.ftc.teamcode.subsystem;

import org.firstinspires.ftc.teamcode.command.CommandScheduler;

public class SubsystemBase implements Subsystem {

    protected String name = this.getClass().getSimpleName();

    public SubsystemBase() {
        CommandScheduler.getInstance().registerSubsystem(this);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubsystem() {
        return getName();
    }

    public void setSubsystem(String subsystem) {
        setName(subsystem);
    }
}
