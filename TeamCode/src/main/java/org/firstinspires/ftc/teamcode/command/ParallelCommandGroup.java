package org.firstinspires.ftc.teamcode.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParallelCommandGroup extends CommandGroupBase {

    private final Map<Command, Boolean> commands = new HashMap<>();
    private boolean runWhenDisabled = true;

    public ParallelCommandGroup(Command... commands) {
        addCommands(commands);
    }

    @Override
    public final void addCommands(Command... commands) {
        requireUngrouped(commands);

        if (this.commands.containsValue(true)) {
            throw new IllegalStateException(
                    "Commands cannot be added to a CommandGroup while the group is running");
        }

        registerGroupedCommands(commands);

        for (Command command : commands) {
            if (!Collections.disjoint(command.getRequirements(), requirements)) {
                throw new IllegalArgumentException("Multiple commands in a parallel group cannot require the same subsystems");
            }
            this.commands.put(command, false);
            requirements.addAll(command.getRequirements());
            runWhenDisabled &= command.runsWhenDisabled();
        }
    }

    @Override
    public void initialize() {
        for (Map.Entry<Command, Boolean> commandRunning : commands.entrySet()) {
            commandRunning.getKey().initialize();
            commandRunning.setValue(true);
        }
    }

    @Override
    public void execute() {
        for (Map.Entry<Command, Boolean> commandRunning : commands.entrySet()) {
            if (!commandRunning.getValue()) {
                continue;
            }
            commandRunning.getKey().execute();
            if (commandRunning.getKey().isFinished()) {
                commandRunning.getKey().end(false);
                commandRunning.setValue(false);
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            for (Map.Entry<Command, Boolean> commandRunning : commands.entrySet()) {
                if (commandRunning.getValue()) {
                    commandRunning.getKey().end(true);
                }
            }
        }
    }

    @Override
    public boolean isFinished() {
        return !commands.containsValue(true);
    }

    @Override
    public boolean runsWhenDisabled() {
        return runWhenDisabled;
    }
}
