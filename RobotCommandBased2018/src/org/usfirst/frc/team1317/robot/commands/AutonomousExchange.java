package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousExchange extends CommandGroup {
	
	final double EXCHANGE_DIST_FROM_LEFT = 120;

	/**
	 * A command for the robot to deposit a cube in the exchange in autonomous
	 * 
	 * @param distanceFromLeftEdge - the distance the robot is from the left side of the field in inches
	 * @param delay - time to wait in seconds before starting
	 */
    public AutonomousExchange(double distanceFromLeftEdge, double delay) {
    	super("Autonomus Exchange");
    	//wait before starting
    	addSequential(new Wait(delay));
    	//drive forward to cross auto line
    	addSequential(new DriveInches(100, 30));
    	//back up
    	addSequential(new DriveInches(-80, 30));
    	//turn right
    	addSequential(new TurnDegrees(90, 0.3));
    	//drive to the exchange
    	addSequential(new DriveInches(EXCHANGE_DIST_FROM_LEFT - distanceFromLeftEdge, 30));
    	//turn right
    	addSequential(new TurnDegrees(90, 0.3));
    	//lower arm
    	addSequential(new ArmDown());
    	//drive forward to exchange
    	addSequential(new DriveInches(16, 10));
    	//open claw
    	addSequential(new ClawOpen());
    	//back up
    	addSequential(new DriveInches(-16, 10));
    }
}
