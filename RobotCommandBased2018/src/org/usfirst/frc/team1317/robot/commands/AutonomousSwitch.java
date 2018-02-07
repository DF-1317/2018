package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.DistanceMap;
import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonomousSwitch extends CommandGroup {

    public AutonomousSwitch(String plateLocations, int startingPosition, boolean crossFront, double delay) {
        
    	Command TurnLeft = new TurnDegrees(-90.0, 1.0);
        Command TurnRight = new TurnDegrees(90.0, 1.0);
    	Boolean SwitchLeft = null; 
    	if (plateLocations.charAt(0) == 'L')
    		SwitchLeft = true;
    	else if (plateLocations.charAt(0) == 'R')
    		SwitchLeft = false;
    	addParallel( new PositionElevator(PositionElevator.SWITCH_POS));
    	addSequential( new Wait(delay) );
    	if(startingPosition == Robot.Center_Position)
    	{
    		addSequential( new DriveInches(DistanceMap.MIDWAY_AUTO_LINE) );
    		addSequential( SwitchLeft ? TurnLeft : TurnRight);
    		addSequential( new DriveInches(DistanceMap.HORIZONTAL_PAST_SWITCH));
    		if(SwitchLeft)
    			addSequential(TurnRight);
    		else
    			addSequential(TurnLeft);
    		addSequential( new DriveInches(DistanceMap.APPROACH_SWITCH_SIDE) );
    	}
    	else
    	{
    		//if the switch and robot are on the same side
    		if((startingPosition==Robot.Left_Position&&SwitchLeft)||(startingPosition==Robot.Right_Position&&!SwitchLeft)) {
    			addSequential( new DriveInches(DistanceMap.MIDWAY_AUTO_TO_SWITCH + DistanceMap.MIDWAY_AUTO_LINE) );
    			if(SwitchLeft) {
    				addSequential(TurnRight);
    			} else {
    				addSequential(TurnLeft);
    			}
    			addSequential( new DriveInches(DistanceMap.APPROACH_SWITCH) );
    		}
    		//if the switch and robot are on opposite sides
    		else {
    			if(crossFront) {
	    			addSequential( new DriveInches(DistanceMap.MIDWAY_AUTO_LINE) );
	    			addSequential( SwitchLeft ? TurnRight : TurnLeft );
	    			addSequential( new DriveInches(DistanceMap.CROSS_COURT) );
	    			addSequential( SwitchLeft ? TurnLeft : TurnRight );
	    			addSequential( new DriveInches(DistanceMap.MIDWAY_AUTO_TO_SWITCH) );
	    			addSequential( SwitchLeft ? TurnLeft : TurnRight );
    			} else {
	    			addSequential( new DriveInches(DistanceMap.MIDWAY_AUTO_LINE + DistanceMap.MIDWAY_AUTO_TO_SWITCH + DistanceMap.SWITCH_TO_MIDWAY_SCALE) );
	    			addSequential( SwitchLeft ? TurnRight : TurnLeft );
	    			addSequential( new DriveInches(DistanceMap.CROSS_COURT) );
	    			addSequential( SwitchLeft ? TurnRight : TurnLeft );
	    			addSequential( new DriveInches(DistanceMap.SWITCH_TO_MIDWAY_SCALE) );
	    			addSequential( SwitchLeft ? TurnRight : TurnLeft );
    			}
    			addSequential( new DriveInches(DistanceMap.APPROACH_SWITCH) );
    		}
    	}
    	// Always place the cube
		addSequential( new PlaceCube());
    }
}
