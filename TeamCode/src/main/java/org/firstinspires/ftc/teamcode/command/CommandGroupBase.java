package org.firstinspires.ftc.teamcode.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class CommandGroupBase extends CommandBase implements Command {

    private static final Set<Command> m_groupedCommands =
            Collections.newSetFromMap(new WeakHashMap<>());

    static void registerGroupedCommands(Command... commands) {
        m_groupedCommands.addAll(Arrays.asList(commands));
    }

    public static void clearGroupedCommands() {
        m_groupedCommands.clear();
    }

    public static void clearGroupedCommand(Command command) {
        m_groupedCommands.remove(command);
    }

    public static void requireUngrouped(Command... commands) {
        requireUngrouped(Arrays.asList(commands));
    }

    public static void requireUngrouped(Collection<Command> commands) {
        if (!Collections.disjoint(commands, getGroupedCommands())) {
            throw new IllegalArgumentException("Commands cannot be added to more than one CommandGroup");
        }
    }

    static Set<Command> getGroupedCommands() {
        return m_groupedCommands;
    }

    public abstract void addCommands(Command... commands);

}
