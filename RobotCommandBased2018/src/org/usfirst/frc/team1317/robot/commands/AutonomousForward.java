package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.DistanceMap;
import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * A command that causes the robot to drive forward to cross the auto line in autonomous
 */
public class AutonomousForward extends CommandGroup {

	/**
	 * creates command to drive forward in autonomous
	 * 
	 * @param position - a number representing the position the robot starts in
	 * @param delay - the time in seconds to wait before driving forward
	 */
    public AutonomousForward(int position, double delay) {
    	super("AutonomousForward");
    	//wait the determined number of seconds
    	addSequential(new ClawClose());
    	addSequential(new Wait(delay));
    	//drive forward to 
    	addSequential(new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, DistanceMap.AUTO_LINE, Robot.DEFAULT_MAX_SPEED));
    }
}
