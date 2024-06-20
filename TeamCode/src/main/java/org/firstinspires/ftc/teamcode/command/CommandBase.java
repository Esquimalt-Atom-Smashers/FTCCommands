package org.firstinspires.ftc.teamcode.command;

import org.firstinspires.ftc.teamcode.subsystem.Subsystem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class CommandBase implements Command {

    protected String name = this.getClass().getSimpleName();
    protected String subsystem = "Ungrouped";
    protected Set<Subsystem> requirements = new HashSet<>();

    public final void addRequirements(Subsystem... requirements) {
        this.requirements.addAll(Arrays.asList(requirements));
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return requirements;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubsystem() {
        return subsystem;
    }

    public void setSubsystem(String subsystem) {
        this.subsystem = subsystem;
    }
}
