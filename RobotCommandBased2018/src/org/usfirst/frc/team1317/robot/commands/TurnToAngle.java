package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.navigation.AutonomousTurningController.TurnMode;
import org.usfirst.frc.team1317.robot.subsystems.MecanumDriveTrainCAN;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This command turns the robot a certain number of degrees and specific speed
 */
public class TurnToAngle extends Command implements PIDOutput {
	
	
	// Objects representing the drive train, gyroscope, and a PID controller
	MecanumDriveTrainCAN DriveTrain;
	
	// Some things that do things
	static final double kP = 0.0;
	static final double kI = 0.00;
	static final double kD = 0.00;
	static final double kToleranceDegrees = 1.0;
	
	// Variables representing speed and angle to turn
	double TargetAngle = 0.0;
	double speed = 1.0;
	
	Timer AutoTimer = new Timer();
	static final double MAX_TIME = 2.0;
	
	/**
	 * The constructor for the command
	 * @param degrees - The angle to turn to in degrees
	 * @param speed - speed from 0 to 1 to turn at.
	 */
	public TurnToAngle(double degrees, double speed)
	{
		super("TurnToAngle");
		requires(Robot.mecanumDriveTrain);
		DriveTrain = Robot.mecanumDriveTrain;
		this.TargetAngle = degrees;
		this.speed = speed;
		setInterruptible(true);
	}
	
	/**
	 * Overloaded constructor with a default speed of 1.0
	 * @param degrees number of degrees to turn. Positive is clockwise
	 */
	public TurnToAngle(double degrees) {
		this(degrees, 1.0);
	}
	
	@Override
	protected void initialize()
	{
		DriveTrain.setTurnControllerMode(TurnMode.withoutDriving);
		Robot.log("Starting to turn");
	}
	
	@Override
	protected void execute()
	{
		DriveTrain.enableTurnController(TargetAngle);
	}
	
	@Override
	protected boolean isFinished() {
		return DriveTrain.turnControllerOnTarget() || AutoTimer.get() >= MAX_TIME;
	}
	
	@Override
	protected void end()
	{
		DriveTrain.disableTurnController();
		DriveTrain.stop();
		Robot.log("Done Turning");
		Robot.log("Current Angle: " + Robot.mecanumDriveTrain.navX.pidGet());
	}
	
	@Override
	public void interrupted()
	{
		DriveTrain.disableTurnController();
		DriveTrain.stop();
	}
	
	@Override
	public void pidWrite(double output) {
		DriveTrain.driveCartesian(0, 0.0, speed*output);
	}

}
