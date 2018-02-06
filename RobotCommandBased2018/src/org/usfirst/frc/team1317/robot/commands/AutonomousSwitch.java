package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonomousSwitch extends CommandGroup {

    public AutonomousSwitch(String plateLocations, int startingPosition, boolean crossCourt, double delay) {
        
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
    		addSequential( new DriveInches(50.0, 70.0) );
    		if(SwitchLeft)
    			addSequential(TurnLeft);
    		else
    			addSequential(TurnRight);
    		addSequential( new DriveInches(50.0, 70.0));
    		if(SwitchLeft)
    			addSequential(TurnRight);
    		else
    			addSequential(TurnLeft);
    		addSequential( new DriveInches(50.0, 70.0) );
    		addSequential( new PlaceCube());
    	}
    	else
    	{
    		//if the switch and robot are on the same side
    		if((startingPosition==Robot.Left_Position&&SwitchLeft)||(startingPosition==Robot.Right_Position&&!SwitchLeft)) {
    			
    		}
    		//if the switch and robot are on opposite sides
    		else {
    			
    		}
    	}
    }
}
