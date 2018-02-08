package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This moves the elevator to a specific position. Handy constants included.
 */
public class PositionElevator extends Command {
	
	// I AM ASSUMING ALL OF THESE VALUES. DO NOT USE UNTIL TESTING ACTUAL VALUES.
	
	double currentPosition;
	final double SPEED = 1;
	
	// Constants for different encoder positions
	public static final int BOTTOM_POS = 0;
	public static final int TOP_POS = 1000;
	public static final int SWITCH_POS = 300;
	
	// Number of pulses error
	final int ERROR = 10;
	
	// Target position
	int target;
	
	// Boolean to determine of command is finished
	boolean finished = false;
	
    public PositionElevator(int target) {
    	super("Position Elevator");
        requires(Robot.el);
        setInterruptible(true);
        this.target = target;
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	currentPosition = Robot.el.getPosition();
    	if(target < currentPosition - ERROR) {
    		Robot.el.move(SPEED);
    	} else if(target > currentPosition + ERROR) {
    		Robot.el.move(-SPEED);
    	} else {
    		finished = true;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.el.move(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.el.move(0);
    }
}
