package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.usfirst.frc.team1317.robot.*;
import org.usfirst.frc.team1317.robot.subsystems.MecanumDriveTrainCAN;

import com.kauailabs.navx.frc.AHRS;

/**
 * This command turns the robot a certain number of degrees and specific speed
 */
public class TurnDegrees extends Command implements PIDOutput {
	
	
	// Objects representing the drive train, gyroscope, and a PID controller
	MecanumDriveTrainCAN DriveTrain;
	AHRS gyroSensor;
	PIDController turnController;
	
	// Some things that do things
	static final double kP = 0.02;
	static final double kI = 0.00;
	static final double kD = 0.00;
	static final double kToleranceDegrees = 1.0;
	
	// Variables representing speed and angle to turn
	double degrees = 0.0;
	double speed = 1.0;
	
	// A variable representing the angle the robot starts at
	double OriginalAngle;
	//a variable representing the angle the robot should come to
	double TargetAngle;
	
	/**
	 * The constructor for the command
	 * @param degrees - number of degrees to turn. Positive is clockwise.
	 * @param speed - speed from 0 to 1 to turn at.
	 */
	public TurnDegrees(double degrees, double speed)
	{
		super("TurnDegrees");
		requires(Robot.mecanumDriveTrain);
		DriveTrain = Robot.mecanumDriveTrain;
		gyroSensor = Robot.mecanumDriveTrain.navX;
		turnController = new PIDController(kP,kI,kD,0.0,gyroSensor,this);
		turnController.setInputRange(-180.0F, 180.0F);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(kToleranceDegrees);
		turnController.setContinuous(true);
		turnController.setName("Drive System", "Rotate Controller");
		LiveWindow.add(turnController);
		this.degrees = degrees;
		this.speed = speed;
		setInterruptible(true);
	}
	
	/**
	 * Overloaded constructor with a default speed of 0.7
	 * @param degrees number of degrees to turn. Positive is clockwise
	 */
	public TurnDegrees(double degrees) {
		this(degrees, 0.7);
	}
	
	@Override
	protected void initialize()
	{
		OriginalAngle = gyroSensor.pidGet();
		Robot.log("Original Angle: " + OriginalAngle);
		TargetAngle = OriginalAngle + degrees;
		TargetAngle = PIDTurning.equivalentAngle(TargetAngle);
		Robot.log("Target Angle: " + TargetAngle);
	}
	
	@Override
	protected void execute()
	{
		turnController.setSetpoint(TargetAngle);
		turnController.enable();
	}
	
	@Override
	protected boolean isFinished() {
		return turnController.onTarget();
	}
	
	@Override
	protected void end()
	{
		turnController.disable();
		DriveTrain.stop();
		Robot.log("Done Turning");
		Robot.log("Current Angle: " + gyroSensor.pidGet());
	}
	
	@Override
	public void interrupted()
	{
		turnController.disable();
		DriveTrain.stop();
	}
	
	@Override
	public void pidWrite(double output) {
		DriveTrain.driveCartesian(0, speed*output, 0.0);
	}

}
