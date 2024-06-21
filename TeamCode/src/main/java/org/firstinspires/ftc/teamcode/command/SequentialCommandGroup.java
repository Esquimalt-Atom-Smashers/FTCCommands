package org.firstinspires.ftc.teamcode.command;

import java.util.ArrayList;
import java.util.List;

public class SequentialCommandGroup extends CommandGroupBase {

    private final List<Command> commands = new ArrayList<>();
    private int currentCommandIndex = -1;
    private boolean runsWhenDisabled = true;

    public SequentialCommandGroup(Command... commands) {
        addCommands(commands);
    }

    @Override
    public void addCommands(Command... commands) {
        requireUngrouped(commands);

        if (currentCommandIndex != -1) {
            throw new IllegalStateException(
                    "Commands cannot be added to a CommandGroup while the group is running");
        }

        registerGroupedCommands(commands);

        for (Command command : commands) {
            this.commands.add(command);
            requirements.addAll(command.getRequirements());
            runsWhenDisabled &= command.runsWhenDisabled();
        }
    }

    @Override
    public void initialize() {
        currentCommandIndex = 0;

        if (!commands.isEmpty()) {
            commands.get(0).initialize();
        }
    }

    @Override
    public void execute() {
        if (commands.isEmpty()) {
            return;
        }

        Command currentCommand = commands.get(currentCommandIndex);

        currentCommand.execute();
        if (currentCommand.isFinished()) {
            currentCommand.end(false);
            currentCommandIndex++;
            if (currentCommandIndex < commands.size()) {
                commands.get(currentCommandIndex).initialize();
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted && !commands.isEmpty()) {
            commands.get(currentCommandIndex).end(true);
        }
        currentCommandIndex = -1;
    }

    @Override
    public boolean isFinished() {
        return currentCommandIndex == commands.size();
    }

    @Override
    public boolean runsWhenDisabled() {
        return runsWhenDisabled;
    }
}
