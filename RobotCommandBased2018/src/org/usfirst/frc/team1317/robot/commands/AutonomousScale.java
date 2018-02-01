package org.usfirst.frc.team1317.robot.commands;

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
    				
    }
}
