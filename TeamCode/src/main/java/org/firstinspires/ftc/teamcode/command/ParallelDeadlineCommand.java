package org.firstinspires.ftc.teamcode.command;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParallelDeadlineCommand extends CommandGroupBase {

    private final Map<Command, Boolean> commands = new HashMap<>();
    private boolean runsWhenDisabled = true;
    private Command deadline;

    public ParallelDeadlineCommand(Command deadline, Command... commands) {
        this.deadline = deadline;
        addCommands(commands);
        if (!this.commands.containsKey(deadline)) {
            addCommands(deadline);
        }
    }

    public void setDeadline(Command deadline) {
        if (!commands.containsKey(deadline)) {
            addCommands(deadline);
        }
        this.deadline = deadline;
    }

    @Override
    public void addCommands(Command... commands) {
        requireUngrouped(commands);

        if (this.commands.containsValue(true)) {
            throw new IllegalStateException("Commands cannot be added to a CommandGroup while the group is running");
        }

        registerGroupedCommands(commands);

        for (Command command : commands) {
            if (!Collections.disjoint(command.getRequirements(), requirements)) {
                throw new IllegalArgumentException("Multiple commands in a parallel group cannot require the same subsystems");
            }
            this.commands.put(command, false);
            requirements.addAll(command.getRequirements());
            runsWhenDisabled &= command.runsWhenDisabled();
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
        for (Map.Entry<Command, Boolean> commandRunning : commands.entrySet()) {
            if (commandRunning.getValue()) {
                commandRunning.getKey().end(true);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return deadline.isFinished();
    }

    @Override
    public boolean runsWhenDisabled() {
        return runsWhenDisabled;
    }
}
