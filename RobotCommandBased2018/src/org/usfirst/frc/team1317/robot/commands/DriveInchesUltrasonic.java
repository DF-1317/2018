package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveInchesUltrasonic extends Command {

    private double targetDistance;
    private Command command;

	public DriveInchesUltrasonic(double targetDistance) {
        this.targetDistance = targetDistance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	command = Robot.ultrasonicDriveToDistance(targetDistance);
    	command.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return command.isCompleted();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
