package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonomousScale extends CommandGroup {

    public AutonomousScale(String plateLocations, int startingPosition, boolean crossCourt, double delay) {
       
    	Boolean ScaleLeft;
    	if (plateLocations.charAt(1) == 'L')
    		ScaleLeft = true;
    	else if (plateLocations.charAt(1) == 'R')
    		ScaleLeft = false;
    	addSequential(new Wait(delay));
    	if (startingPosition == Robot.Center_Position) {
    		
    	}
    	else {
    		
    	}
    }
}
