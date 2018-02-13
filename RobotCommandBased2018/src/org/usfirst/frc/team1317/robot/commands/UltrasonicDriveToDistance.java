package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * A command to drive the robot until the ultrasonic sensor is within a certain distance 
 */
public class UltrasonicDriveToDistance extends Command {

	static final double kP = 0.01;
	static final double kI = 0.00;
	static final double kD = 0.00;
	static final double kF = 0.00;
	static final double kTolerance = 10.0;
	
	PIDController moveController;
	
	double target;
	
	/**
	 * constructs the command 
	 * @param millimetersFromObject - the target millimeters from an object that the robot should be at
	 */
    public UltrasonicDriveToDistance(double millimetersFromObject) {
    	super("UltrasonicDriveToDistance");
        requires(Robot.mecanumDriveTrain);
        moveController = new PIDController(kP, kI, kD, kF, Robot.Ultrasonic, Robot.mecanumDriveTrain);
    	moveController.setOutputRange(-1.0, 1.0);
    	moveController.setAbsoluteTolerance(kTolerance);
    	moveController.setName("Drive System", "Ultrasonic Controller");
    	LiveWindow.add(moveController);
    	target = millimetersFromObject;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	moveController.setSetpoint(target);
    	moveController.enable();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return moveController.onTarget();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	moveController.disable();
    	Robot.mecanumDriveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	moveController.disable();
    	Robot.mecanumDriveTrain.stop();
    }
}
