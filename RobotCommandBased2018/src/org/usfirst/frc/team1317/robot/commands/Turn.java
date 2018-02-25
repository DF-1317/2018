
package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

class Turn {
    private Turn() {}

    public static Command right() {
        return new TurnDegrees(90.0, 0.1);
    }

    public static Command left() {
        return new TurnDegrees(-90.0, 0.1);
    }

    public static Command around() {
        return new TurnDegrees(180.0, 0.1);
    }
}