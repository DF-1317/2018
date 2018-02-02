package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PlaceCube extends CommandGroup {

    public PlaceCube() {
    	addSequential(new ArmDown());
    	addSequential(new Wait (0.2));
    	addSequential(new ClawOpen());
    	addSequential(new Wait (1.2));
    	addParallel(new ArmUp());
    	addSequential(new ClawClose());
    }
}