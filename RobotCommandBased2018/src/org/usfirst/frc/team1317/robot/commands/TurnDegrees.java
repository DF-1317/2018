package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.PIDTurning;
import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.navigation.AutonomousTurningController.TurnMode;
import org.usfirst.frc.team1317.robot.subsystems.MecanumDriveTrainCAN;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This command turns the robot a certain number of degrees and specific speed
 */
public class TurnDegrees extends Command implements PIDOutput {
	
	
	// Objects representing the drive train, gyroscope, and a PID controller
	MecanumDriveTrainCAN DriveTrain;
	AHRS gyroSensor;
	// Variables representing speed and angle to turn
	double degrees = 0.0;
	double speed = 1.0;
	
	// A variable representing the angle the robot starts at
	double OriginalAngle;
	//a variable representing the angle the robot should come to
	double TargetAngle;
	
	Timer AutoTimer = new Timer();
	
	public static final double WAIT_TIME = 2.0;
	
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
		DriveTrain.setTurnControllerMode(TurnMode.withoutDriving);
		AutoTimer.reset();
		AutoTimer.start();
	}
	
	@Override
	protected void execute()
	{
		DriveTrain.enableTurnController(TargetAngle);
	}
	
	@Override
	protected boolean isFinished() {
		return DriveTrain.turnControllerOnTarget() || AutoTimer.get() > WAIT_TIME;
	}
	
	@Override
	protected void end()
	{
		DriveTrain.disableTurnController();
		DriveTrain.stop();	
		Robot.log("Done Turning");
		Robot.log("Current Angle: " + gyroSensor.pidGet());
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
