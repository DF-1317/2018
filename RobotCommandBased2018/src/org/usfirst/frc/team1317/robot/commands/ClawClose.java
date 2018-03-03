package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.subsystems.Claw;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * A command to close the claw
 */
public class ClawClose extends Command {
	
	//see comments for ClawOpen
	Claw claw;
	Timer timer;
	final double COMPLETION_TIME = 0.2;

	/**
	 * creates a command to close the claw
	 */
    public ClawClose() {
    	super("ClawClose");
    	requires(Robot.claw);
    	claw = Robot.claw;
    	timer = new Timer();
    	setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.reset();
    	timer.start();
    	claw.closeClaw();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	return COMPLETION_TIME < timer.get();
    }

    // Called once after isFinished returns true
    protected void end() {
    	timer.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	timer.stop();
    }
}
