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
	 * @param plateLocations - a string representing the positions of the plates. The code is expecting a string a three characters that are either 'L' or 'R'. If it does not get this, it will throw a NullPointerException.
	 * @param startingPosition - a number representing the starting position of the robot in autonomous
	 * @param crossFront - a boolean value representing whether the robot will cross in front of the switch if it must cross the field. <code>true</code> represents that the robot will cross between the player station wall and the switch. <code>false</code> means that the robot will cross between the switch and the platform. 
	 * @param delay - the time in seconds the robot will wait before starting its routine
	 * 
	 * @see AutonomousSwitch
	 */
    public AutonomousScale(String plateLocations, int startingPosition, boolean crossFront, double delay) {
       
    	//a variable representing where the scale is
    	Boolean ScaleLeft = null;
    	double heading = 90.0;
    	//commands for turning right or left
    	Command TurnLeft = new TurnDegrees(-90.0, 1.0);
        Command TurnRight = new TurnDegrees(90.0, 1.0);
        //stores plate locations in variable
    	if (plateLocations.charAt(1) == 'L') {
    		ScaleLeft = true;
    		heading = -heading;
    	}
    	else if (plateLocations.charAt(1) == 'R')
    		ScaleLeft = false;
    	//elevator starts moving up
    	addParallel(new PositionElevator(PositionElevator.TOP_POS));
    	//waits the specified amount of time
    	addSequential(new Wait(delay));
    	//if the robot is in the center position
    	if (startingPosition == Robot.Center_Position) {
    		addSequential(new DriveInchesPID(DistanceMap.MIDWAY_AUTO_LINE, 0.0));
    		addSequential(ScaleLeft ? TurnLeft:TurnRight);
    		addSequential(new DriveInchesPID(DistanceMap.HORIZONTAL_PAST_SWITCH, heading));
    		addSequential(ScaleLeft ? TurnRight:TurnLeft);
    		addSequential(new DriveInchesPID(DistanceMap.MIDWAY_AUTO_TO_SCALE, 0.0));
    		addSequential(ScaleLeft ? TurnRight:TurnLeft);
    		addSequential(new DriveInchesPID(DistanceMap.APPROACH_SCALE, -heading));
    	}
    	else {
    		//if the scale is on the same position as the robot
    		if((startingPosition==Robot.Left_Position&&ScaleLeft)||(startingPosition==Robot.Right_Position&&!ScaleLeft)) {
    			addSequential(new DriveInchesPID(DistanceMap.MIDWAY_AUTO_LINE+DistanceMap.MIDWAY_AUTO_TO_SCALE, 0.0));
    			addSequential(ScaleLeft ? TurnRight:TurnLeft);
    			addSequential(new DriveInchesPID(DistanceMap.APPROACH_SCALE, -heading));
    		} else { //if we have to cross the court to get to the scale
    			//if we're crossing in front of the switch
    			if(crossFront) {
    				addSequential( new DriveInchesPID(DistanceMap.MIDWAY_AUTO_LINE, 0.0) );
	    			addSequential( ScaleLeft ? TurnRight : TurnLeft );
	    			addSequential( new DriveInchesPID(DistanceMap.CROSS_COURT, -heading) );
	    			addSequential( ScaleLeft ? TurnLeft : TurnRight );
	    			addSequential( new DriveInchesPID(DistanceMap.MIDWAY_AUTO_TO_SWITCH, 0.0) );
	    			addSequential( ScaleLeft ? TurnLeft : TurnRight );
    			} else { //if we're crossing behind the switch
    				addSequential( new DriveInchesPID(DistanceMap.MIDWAY_AUTO_LINE + DistanceMap.MIDWAY_AUTO_TO_SWITCH + DistanceMap.SWITCH_TO_MIDWAY_SCALE, 0.0) );
	    			addSequential( ScaleLeft ? TurnLeft : TurnRight );
	    			addSequential( new DriveInchesPID(DistanceMap.CROSS_COURT, heading) );
	    			addSequential( ScaleLeft ? TurnRight : TurnLeft );
	    			addSequential( new DriveInchesPID(DistanceMap.SWITCH_TO_SCALE - DistanceMap.SWITCH_TO_MIDWAY_SCALE, 0.0) );
	    			addSequential( ScaleLeft ? TurnRight : TurnLeft );
    			}

    		}
    	}
    	//approach the scale, regardless of path taken
    	addSequential(new DriveInches(DistanceMap.APPROACH_SCALE, ScaleLeft ? 90.0 : -90.0));
    	//always place cube at the end of autonomous
    	addSequential(new PlaceCube());
    }
}
