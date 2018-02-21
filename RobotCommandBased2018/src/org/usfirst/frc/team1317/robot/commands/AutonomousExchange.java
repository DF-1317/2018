package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.DistanceMap;
import org.usfirst.frc.team1317.robot.Robot;

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
    	addSequential(new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, DistanceMap.AUTO_LINE + 10.0, Robot.DEFAULT_MAX_SPEED));
    	//back up
    	addSequential(new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, DistanceMap.AUTO_LINE - 10.0, Robot.DEFAULT_MAX_SPEED, true));
    	//turn right
    	addSequential(new TurnToAngle(90.0));
    	//drive to the exchange
    	addSequential(new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, EXCHANGE_DIST_FROM_LEFT - distanceFromLeftEdge, Robot.DEFAULT_MAX_SPEED));
    	//turn right
    	addSequential(new TurnToAngle(180.0));
    	//lower arm
    	addSequential(new ArmDown());
    	//drive forward to exchange
    	addSequential(new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, 16.0, Robot.DEFAULT_MAX_SPEED));
    	//open claw
    	addSequential(new ClawOpen());
    	//back up
    	addSequential(new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, 16.0, Robot.DEFAULT_MAX_SPEED, true));
    }
}
