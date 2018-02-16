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
    	addSequential(new DriveInchesPID(100.0, 0.0));
    	//back up
    	addSequential(new DriveInchesPID(-100.0, 0.0));
    	//turn right
    	addSequential(new TurnToAngle(90.0));
    	//drive to the exchange
    	addSequential(new DriveInchesPID(EXCHANGE_DIST_FROM_LEFT - distanceFromLeftEdge, 90.0));
    	//turn right
    	addSequential(new TurnToAngle(180.0));
    	//lower arm
    	addSequential(new ArmDown());
    	//drive forward to exchange
    	addSequential(new DriveInchesPID(16.0, 180.0));
    	//open claw
    	addSequential(new ClawOpen());
    	//back up
    	addSequential(new DriveInchesPID(-16.0, 180));
    }
}
