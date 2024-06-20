package org.firstinspires.ftc.teamcode.command;

import org.firstinspires.ftc.teamcode.subsystem.Subsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CommandScheduler {

    private static CommandScheduler instance;

    public static synchronized CommandScheduler getInstance() {
        if (instance == null) {
            instance = new CommandScheduler();
        }
        return instance;
    }

    public synchronized void reset() {
        instance = null;
    }

    private enum CommandState {
        INTERRUPTIBLE,
        NON_INTERRUPTIBLE
    }

    // A map from registered subsystems to their default command
    // Also used as a list of currently-registered subsystems
    private final Map<Subsystem, Command> registeredSubsystems = new LinkedHashMap<>();

    // A map from subsystems to their requiring commands. (FTCLib calls this requirements
    // Also used as a list of currently-required subsystems (ie a command is running that requires it)
    private final Map<Subsystem, Command> runningSubsystems = new LinkedHashMap<>();

    // A map from commands to their scheduling state.
    // Also used as a set of currently-running commands.
    private final Map<Command, CommandState> scheduledCommands = new LinkedHashMap<>();

    private final Collection<Runnable> buttons = new LinkedHashSet<>();

    private final Map<Command, CommandState> toSchedule = new LinkedHashMap<>();
    private final List<Command> toCancel = new ArrayList<>();

    private boolean inRunLoop;
    private boolean disabled;

    public CommandScheduler() {

    }

    /*
    In run:
    Call periodic on all subsystems
    Poll buttons
    Go through each command in scheduled commands
        execute them,
        remove them if finished
        etc.
    Schedule things that might have come up
    Cancel command we need to
    Schedule default commands
     */
    public void run() {
        if (disabled) return;

        for (Subsystem subsystem : registeredSubsystems.keySet()) {
            subsystem.periodic();
        }

        for (Runnable button : buttons) {
            button.run();
        }

        inRunLoop = true;
        // Run scheduled commands, remove finished commands.
        for (Iterator<Command> iterator = scheduledCommands.keySet().iterator(); iterator.hasNext(); ) {
            Command command = iterator.next();

            // TODO: Check cases for being disabled

            // By here, the command is sure to be still running.
            command.execute();
            if (command.isFinished()) {
                command.end(false);
                iterator.remove();

                runningSubsystems.keySet().removeAll(command.getRequirements());
            }
        }
        inRunLoop = false;

        // TODO: Schedule the commands in toSchedule

        // TODO: Cancel commands in toCancel

        // TODO: Clear toSchedule and toCancel

        // Schedule the default commands for the subsystems that weren't required by a running command
        for (Map.Entry<Subsystem, Command> subsystemCommand : registeredSubsystems.entrySet()) {
            // If the command didn't run, and it has a default command, schedule it
            if (!runningSubsystems.containsKey(subsystemCommand.getKey()) && subsystemCommand.getValue() != null) {
                // TODO: Schedule
            }
        }
    }

    public void registerSubsystem(Subsystem... subsystems) {
        for (Subsystem subsystem : subsystems) {
            registeredSubsystems.put(subsystem, null);
        }
    }

    public void unregisterSubsystem(Subsystem... subsystems) {
        registeredSubsystems.keySet().removeAll(Arrays.asList(subsystems));
    }

    public void setDefaultCommand(Subsystem subsystem, Command defaultCommand) {
        // TODO: Check if command doesn't require it's subsystem

        if (defaultCommand.isFinished()) {
            throw new IllegalArgumentException("Default commands should not end!");
        }

        registeredSubsystems.put(subsystem, defaultCommand);
    }

    public Command getDefaultCommand(Subsystem subsystem) {
        return registeredSubsystems.get(subsystem);
    }

    public Command requiring(Subsystem subsystem) {
        return runningSubsystems.get(subsystem);
    }

    private void schedule(CommandState interruptible, Command command) {
        if (inRunLoop) {
            toSchedule.put(command, interruptible);
            return;
        }

        // TODO: Check for grouped command

        // TODO: Check for disabled

        // We are not in the run loop, the command could run if its subsystem aren't being used
        Set<Subsystem> requirements = command.getRequirements();

        // Check if the command is being used by another running command
        if (Collections.disjoint(runningSubsystems.keySet(), requirements)) {
            initCommand(command, interruptible, requirements);
        }
        else {
            // If it is, see if we can interrupt it by looping through each requirement
            for (Subsystem requirement : requirements) {
                if (runningSubsystems.containsKey(requirement) && scheduledCommands.get(runningSubsystems.get(requirement)) == CommandState.NON_INTERRUPTIBLE) {
                    return;
                }
            }
            for (Subsystem requirement : requirements) {
                if (runningSubsystems.containsKey(requirement)) {
                    cancel(runningSubsystems.get(requirement));
                }
            }
            initCommand(command, interruptible, requirements);
        }
    }

    public void schedule(CommandState interruptible, Command... commands) {
        for (Command command : commands) {
            schedule(interruptible, command);
        }
    }

    public void schedule(boolean interruptible, Command... commands) {
        for (Command command : commands) {
            schedule(interruptible ? CommandState.INTERRUPTIBLE : CommandState.NON_INTERRUPTIBLE, command);
        }
    }

    public void schedule(Command... commands) {
        schedule(CommandState.INTERRUPTIBLE, commands);
    }

    public boolean isScheduled(Command... command) {
        return scheduledCommands.keySet().containsAll(Arrays.asList(command));
    }

    public void initCommand(Command command, CommandState interruptible, Set<Subsystem> requirements) {
        command.initialize();
        scheduledCommands.put(command, interruptible);
        // Each subsystem is now being used by this command
        for (Subsystem requirement : requirements) {
            runningSubsystems.put(requirement, command);
        }
    }

    public void cancel(Command... commands) {
        if (inRunLoop) {
            toCancel.addAll(Arrays.asList(commands));
            return;
        }

        for (Command command : commands) {
            // If the command isn't scheduled, we can't cancel it
            if (!scheduledCommands.containsKey(command)) {
                continue;
            }

            command.end(true);
            scheduledCommands.remove(command);
            // The subsystem this command requires are no longer running
            runningSubsystems.keySet().removeAll(command.getRequirements());
        }
    }

    public void cancelAll() {
        for (Command command : scheduledCommands.keySet()) {
            cancel(command);
        }
    }

    public void disable() {
        disabled = true;
    }

    public void enable() {
        disabled = false;
    }
}
