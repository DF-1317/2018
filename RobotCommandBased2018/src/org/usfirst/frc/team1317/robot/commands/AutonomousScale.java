package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.DistanceMap;
import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * A command to deposit a cube in autonomous
 */
public class AutonomousScale extends CommandGroup {

	/**
	 * creates a new command to deposit cube in autonomous
	 * 
	 * @param plateLocations - a string representing the positions of the plates
	 * @param startingPosition - a number representing the starting position of the robot in autonomous
	 * @param crossFront - a boolean value representing whether the robot will cross in front of the switch if it must cross the field. <code>true</code> represents that the robot will cross between the player station wall and the switch. <code>false</code> means that the robot will cross between the switch and the platform. 
	 * @param delay - the time in seconds the robot will wait before starting its routine
	 * 
	 * @see AutonomousSwitch
	 */
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
    		addSequential(new DriveInches(DistanceMap.MIDWAY_AUTO_LINE));
    		addSequential(ScaleLeft ? TurnLeft:TurnRight);
    		addSequential(new DriveInches(DistanceMap.HORIZONTAL_PAST_SWITCH));
    		addSequential(ScaleLeft ? TurnRight:TurnLeft);
    		addSequential(new DriveInches(DistanceMap.MIDWAY_AUTO_TO_SCALE));
    		addSequential(ScaleLeft ? TurnRight:TurnLeft);
    		addSequential(new DriveInches(DistanceMap.APPROACH_SCALE));
    	}
    	else {
    		//if the scale is on the same position as the robot
    		if((startingPosition==Robot.Left_Position&&ScaleLeft)||(startingPosition==Robot.Right_Position&&!ScaleLeft)) {
    			addSequential(new DriveInches(DistanceMap.MIDWAY_AUTO_LINE+DistanceMap.MIDWAY_AUTO_TO_SCALE));
    			addSequential(ScaleLeft ? TurnRight:TurnLeft);
    			addSequential(new DriveInches(DistanceMap.APPROACH_SCALE));
    		} else {
    			if(crossFront) {
    				addSequential( new DriveInches(DistanceMap.MIDWAY_AUTO_LINE) );
	    			addSequential( ScaleLeft ? TurnRight : TurnLeft );
	    			addSequential( new DriveInches(DistanceMap.CROSS_COURT) );
	    			addSequential( ScaleLeft ? TurnLeft : TurnRight );
	    			addSequential( new DriveInches(DistanceMap.MIDWAY_AUTO_TO_SWITCH) );
	    			addSequential( ScaleLeft ? TurnLeft : TurnRight );
    			} else {
    				addSequential( new DriveInches(DistanceMap.MIDWAY_AUTO_LINE + DistanceMap.MIDWAY_AUTO_TO_SWITCH + DistanceMap.SWITCH_TO_MIDWAY_SCALE) );
	    			addSequential( ScaleLeft ? TurnLeft : TurnRight );
	    			addSequential( new DriveInches(DistanceMap.CROSS_COURT) );
	    			addSequential( ScaleLeft ? TurnRight : TurnLeft );
	    			addSequential( new DriveInches(DistanceMap.SWITCH_TO_SCALE - DistanceMap.SWITCH_TO_MIDWAY_SCALE) );
	    			addSequential( ScaleLeft ? TurnRight : TurnLeft );
    			}
    			addSequential(new DriveInches(DistanceMap.APPROACH_SWITCH));
    		}
    	}
    	addSequential(new PlaceCube());
    }
}
