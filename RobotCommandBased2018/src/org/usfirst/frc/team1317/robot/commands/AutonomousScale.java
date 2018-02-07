package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonomousScale extends CommandGroup {

    public AutonomousScale(String plateLocations, int startingPosition, boolean crossFront, double delay) {
       
    	Boolean ScaleLeft = null;
    	Command TurnLeft = new TurnDegrees(-90.0, 1.0);
        Command TurnRight = new TurnDegrees(90.0, 1.0);
    	if (plateLocations.charAt(1) == 'L')
    		ScaleLeft = true;
    	else if (plateLocations.charAt(1) == 'R')
    		ScaleLeft = false;
    	addParallel(new PositionElevator(PositionElevator.TOP_POS));
    	addSequential(new Wait(delay));
    	if (startingPosition == Robot.Center_Position) {
    		addSequential(new DriveInches(50.0));
    		addSequential(ScaleLeft ? TurnLeft:TurnRight);
    		addSequential(new DriveInches(100.0));
    		addSequential(ScaleLeft ? TurnRight:TurnLeft);
    		addSequential(new DriveInches(300.0));
    		addSequential(ScaleLeft ? TurnRight:TurnLeft);
    		addSequential(new DriveInches(20.0));
    	}
    	else {
    		
    	}
    	addSequential(new PlaceCube());
    }
}
