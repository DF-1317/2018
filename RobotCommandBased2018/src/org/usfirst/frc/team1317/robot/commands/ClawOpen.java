package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.subsystems.Claw;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Timer;

/**
 * A command for opening the robots claw
 * 
 * @see ClawClose
 */
public class ClawOpen extends Command {
	
	Claw claw;
	Timer timer;
	//the time it should take for the claw to open
	final double COMPLETION_TIME = 0.5;
	
	/**
	 * creates command for opening claw
	 */
    public ClawOpen() {
    	super("ClawOpen");
    	//this requires the claw
    	requires(Robot.claw);
    	//set up necessary variables
    	claw = Robot.claw;
    	timer = new Timer();
    	//we can interrupt this
    	setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//reset and start the timer
    	timer.reset();
    	timer.start();
    	//start opening the claw
    	claw.openClaw();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	//this is fininished when the timer has run for the specified amount of time
        return COMPLETION_TIME < timer.get();
    }

    // Called once after isFinished returns true
    protected void end() {
    	//stop the timer when we're done
    	timer.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	//stop the timer when interrupted
    	timer.stop();
    }
}
