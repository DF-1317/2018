package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Command lowers the arm, opens the claw, raises the arm, and closes the claw.
 */
public class PlaceCube extends CommandGroup {

    public PlaceCube() {
    	super("PlaceCube");
    	addSequential(new ArmDown());
    	addSequential(new Wait (0.2));
    	addSequential(new ClawOpen());
    	addSequential(new Wait (1.2));
    	addParallel(new ArmUp());
    	addSequential(new ClawClose());
    }
}
