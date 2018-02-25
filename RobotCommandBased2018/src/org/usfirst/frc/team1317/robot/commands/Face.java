
package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

class Face {
    private Face() {}

    public static Command forward() {
        return new TurnToAngle(0.0);
    }

    public static Command backward() {
        return new TurnToAngle(180.0);
    }

    public static Command left() {
        return new TurnToAngle(-90.0);
    }

    public static Command right() {
        return new TurnToAngle(90.0);
    }
}