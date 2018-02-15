package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.DistanceMap;
import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * This is the <code>CommandGroup</code> used in the autonomous phase of the match if the robot places the cube on the switch.
 */
public class AutonomousSwitchTimer extends CommandGroup {
	
	final double SPEED_MOD = 0.01;
	final double SPEED = 0.7;
	
	/** 
	 * 
	 * @param plateLocations String containing the game data for the match
	 * @param startingPosition Integer representing the starting position of the robot
	 * @param crossFront Boolean representing whether the robot crosses the court in front of or behind the switch
	 * @param delay Delay in seconds the robot waits after the match starts before running this command (The elevator will still go up immediately.)
	 */
    public AutonomousSwitchTimer(String plateLocations, int startingPosition, boolean crossFront, double delay) {
    	super("AutonomousSwitchTimer");
    	// Commands for turning
    	Command TurnLeft = new TurnDegrees(-90.0, 1.0);
        Command TurnRight = new TurnDegrees(90.0, 1.0);
        // Variable to determine if switch is on the left
    	Boolean SwitchLeft = null; 
    	
    	// Assign a value to SwitchLeft variable based on the game data
    	if (plateLocations.charAt(0) == 'L')
    		SwitchLeft = true;
    	else if (plateLocations.charAt(0) == 'R')
    		SwitchLeft = false;
    	
    	// This command will always raise the elevator and wait (possibly 0 seconds) to start with
    	addParallel( new PositionElevator(PositionElevator.SWITCH_POS));
    	addSequential( new Wait(delay) );
    	
    	//if the robot is in the center position
    	if(startingPosition == Robot.Center_Position)
    	{	
    		// Drive midway to the auto line, turn based on the side of the switch is ours, 
    		// drive level to the side of the switch, turn towards the switch, and approach it.
    		addSequential( new DriveForward(DistanceMap.MIDWAY_AUTO_LINE * SPEED_MOD, SPEED));
    		addSequential( SwitchLeft ? TurnLeft : TurnRight);
    		addSequential( new DriveForward(DistanceMap.HORIZONTAL_PAST_SWITCH * SPEED_MOD, SPEED));
    		addSequential( SwitchLeft ? TurnRight : TurnLeft );
    		addSequential( new DriveForward(DistanceMap.APPROACH_SWITCH_SIDE * SPEED_MOD, SPEED) );
    	}
    	else
    	{
    		//if the switch and robot are on the same side
    		if((startingPosition==Robot.Left_Position&&SwitchLeft)||(startingPosition==Robot.Right_Position&&!SwitchLeft)) {
    			addSequential( new DriveForward((DistanceMap.MIDWAY_AUTO_TO_SWITCH + DistanceMap.MIDWAY_AUTO_LINE) * SPEED_MOD, SPEED) );
    			if(SwitchLeft) {
    				addSequential(TurnRight);
    			} else {
    				addSequential(TurnLeft);
    			}
    			addSequential( new DriveForward(DistanceMap.APPROACH_SWITCH * SPEED_MOD, SPEED) );
    		}
    		//if the switch and robot are on opposite sides
    		else {
				// Instructions to reaching the switch based on whether the robot is crossing the court in front of or behind the switch
    			if(crossFront) {
	    			addSequential( new DriveForward(DistanceMap.MIDWAY_AUTO_LINE * SPEED_MOD, SPEED) );
	    			addSequential( SwitchLeft ? TurnRight : TurnLeft );
	    			addSequential( new DriveForward(DistanceMap.CROSS_COURT * SPEED_MOD, SPEED) );
	    			addSequential( SwitchLeft ? TurnLeft : TurnRight );
	    			addSequential( new DriveForward(DistanceMap.MIDWAY_AUTO_TO_SWITCH * SPEED_MOD, SPEED) );
	    			addSequential( SwitchLeft ? TurnLeft : TurnRight );
    			} else {
	    			addSequential( new DriveForward((DistanceMap.MIDWAY_AUTO_LINE + DistanceMap.MIDWAY_AUTO_TO_SWITCH + DistanceMap.SWITCH_TO_MIDWAY_SCALE) * SPEED_MOD, SPEED) );
	    			addSequential( SwitchLeft ? TurnRight : TurnLeft );
	    			addSequential( new DriveForward(DistanceMap.CROSS_COURT * SPEED_MOD, SPEED) );
	    			addSequential( SwitchLeft ? TurnRight : TurnLeft );
	    			addSequential( new DriveForward(DistanceMap.SWITCH_TO_MIDWAY_SCALE * SPEED_MOD, SPEED) );
	    			addSequential( SwitchLeft ? TurnRight : TurnLeft );
    			}
    			// The robot will always approach the switch
    			addSequential( new DriveForward(DistanceMap.APPROACH_SWITCH * SPEED_MOD, SPEED) );
    		}
    	}
    	// Always place the cube
		addSequential( new PlaceCube());
    }
}
