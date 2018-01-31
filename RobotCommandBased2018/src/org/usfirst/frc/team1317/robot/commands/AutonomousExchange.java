package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousExchange extends CommandGroup {
	
	final double EXCHANGE_DIST_FROM_LEFT = 120;

    public AutonomousExchange(double distanceFromLeftEdge, double delay) {
    	addSequential(new Wait(delay));
    	addSequential(new DriveInches(100, 30));
    	addSequential(new DriveInches(-80, 30));
    	addSequential(new TurnDegrees(90, 0.3));
    	addSequential(new DriveInches(EXCHANGE_DIST_FROM_LEFT - distanceFromLeftEdge, 30));
    	addSequential(new TurnDegrees(90, 0.3));
    	addSequential(new ArmDown());
    	addSequential(new DriveInches(16, 10));
    	addSequential(new ClawOpen());
    	addSequential(new DriveInches(-16, 10));
    }
}
