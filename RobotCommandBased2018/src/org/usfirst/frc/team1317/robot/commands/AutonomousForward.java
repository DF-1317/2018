package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonomousForward extends CommandGroup {

    public AutonomousForward(int Position, double delay) {
    	addSequential(new Wait(delay));
    	addSequential(new DriveInches(100, 30));
        
    	
    }
}
