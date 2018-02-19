package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * A command to drive the robot until the ultrasonic sensor is within a certain distance 
 */
public class UltrasonicDriveToDistance extends Command {
	
	double target;
	
	/**
	 * constructs the command 
	 * @param inchesFromObject - the target inches from an object that the robot should be at
	 */
    public UltrasonicDriveToDistance(double inchesFromObject) {
    	super("UltrasonicDriveToDistance");
        requires(Robot.mecanumDriveTrain);
    	target = inchesFromObject;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	Robot.mecanumDriveTrain.enableUltrasonicController(target);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.mecanumDriveTrain.ultrasonicControllerOnTarget();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	Robot.mecanumDriveTrain.disableUltrasonicController();
    	Robot.mecanumDriveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	Robot.mecanumDriveTrain.disableUltrasonicController();
    	Robot.mecanumDriveTrain.stop();
    }
}
