package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command that moves the elevator to a specific position
 */
public class PositionElevatorPID extends Command {

	Elevator el;
	
	// Variable representing the target position of the elevator
	double position;
	
	/**
	 * The constructor for the command
	 * @param position - target position for the elevator.
	 */
    public PositionElevatorPID(double position) {
    	
    	super("PositionElevatorPID");
    	requires(Robot.el);
    	el = Robot.el;
    	this.position = position;
    	setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	el.setSetpoint(position);
    	el.enable();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return el.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	el.disable();
    	el.ElevatorMotor.stopMotor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	el.disable();
    	el.ElevatorMotor.stopMotor();
    }
}
