package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Logger;
import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveInchesUltrasonic extends Command {

	private double targetPosition;
    private DriveInchesAccelerate command;
    Logger syslog = new Logger("1317", "DriveInchesUltrasonic");
    

	public DriveInchesUltrasonic(double targetPostion) {
        this.targetPosition = targetPostion;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	double startPosition = Robot.getUltrasonicAverageDistance();
		double targetDistance = targetPosition - startPosition;
		syslog.log("start: " + startPosition + ", targetDistace: " + targetDistance);
		if(targetDistance < 0) {
			command = new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, -targetDistance, Robot.DEFAULT_MAX_SPEED, true);
		} else {
			command = new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, targetDistance, Robot.DEFAULT_MAX_SPEED);
		}
		command.initialize();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	command.execute();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return command.isFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    	command.end();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	command.interrupted();
    }
}
