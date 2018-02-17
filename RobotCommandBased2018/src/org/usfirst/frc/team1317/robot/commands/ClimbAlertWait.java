package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * This command notifies the drivers when there are only 30 seconds left in the match.
 */
public class ClimbAlertWait extends CommandGroup {

    public ClimbAlertWait() {
    	addSequential(new Wait(105.0));
    	addSequential(new ClimbAlert());
    	addSequential(new Wait(15.0));
    	addSequential(new ClimbAlert());
    }
}
