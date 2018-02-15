package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class AutonomousExchangeTimer extends CommandGroup {
	
	final double EXCHANGE_DIST_FROM_LEFT = 120;

	/**
	 * A command for the robot to deposit a cube in the exchange in autonomous
	 * 
	 * @param distanceFromLeftEdge - the distance the robot is from the left side of the field in inches
	 * @param delay - time to wait in seconds before starting
	 */
    public AutonomousExchangeTimer(double distanceFromLeftEdge, double delay) {
    	super("Autonomus Exchange Timer");
    	//wait before starting
    	addSequential(new Wait(delay));
    	//drive forward to cross auto line
    	addSequential(new DriveForward(1.0, 0.7));
    	//back up
    	addSequential(new DriveForward(-0.8, 0.7));
    	//turn right
    	addSequential(new TurnDegrees(90, 0.3));
    	//drive to the exchange
    	addSequential(new DriveForward(EXCHANGE_DIST_FROM_LEFT - distanceFromLeftEdge, 30));
    	//turn right
    	addSequential(new TurnDegrees(90, 0.3));
    	//lower arm
    	addSequential(new ArmDown());
    	//drive forward to exchange
    	addSequential(new DriveForward(0.16, 0.5));
    	//open claw
    	addSequential(new ClawOpen());
    	//back up
    	addSequential(new DriveForward(-0.16, 0.5));
    }
}
