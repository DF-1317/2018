package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonomousSwitch extends CommandGroup {

    public AutonomousSwitch(String plateLocations, int startingPosition, boolean crossCourt, double delay) {
        
    	Boolean SwitchLeft; 
    	if (plateLocations.charAt(0) == 'L')
    		SwitchLeft = true;
    	else if (plateLocations.charAt(0) == 'R')
    		SwitchLeft = false;
    	addSequential( new Wait(delay) );
    	if(startingPosition == Robot.Center_Position)
    	{
    		
    	}
    	else
    	{
    		
    	}
    }
}
