package org.firstinspires.ftc.teamcode.command;

import java.util.ArrayList;
import java.util.List;

public class SequentialCommandGroup {

    private final List<Command> commands = new ArrayList<>();
    private int currentCommandIndex = -1;
    private boolean runsWhenDisabled = true;

    public SequentialCommandGroup(Command... commands) {

    }


}
