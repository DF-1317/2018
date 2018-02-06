package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.usfirst.frc.team1317.robot.*;
import org.usfirst.frc.team1317.robot.subsystems.MecanumDriveTrainCAN;

import com.kauailabs.navx.frc.AHRS;

public class TurnDegrees extends Command implements PIDOutput {

	MecanumDriveTrainCAN DriveTrain;
	AHRS gyroSensor;
	PIDController turnController;
	static final double kP = 0.01;
	static final double kI = 0.00;
	static final double kD = 0.00;
	static final double kToleranceDegrees = 5.0;
	double degrees = 0;
	double speed = 1.0;
	boolean done = false;
	double TurnRate;
	double OriginalAngle;
	double TargetAngle;
	
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
		TurnRate = 0.0;
		turnController.setName("Drive System", "Rotate Controller");
		LiveWindow.add(turnController);
		this.degrees = degrees;
		this.speed = speed;
		setInterruptible(true);
	}
	@Override
	protected void initialize()
	{
		OriginalAngle = gyroSensor.getAngle();
		TargetAngle = OriginalAngle + degrees;
	}
	
	@Override
	protected void execute()
	{
		turnController.setSetpoint(TargetAngle);
		turnController.enable();
		DriveTrain.driveCartesian(0, 0, speed*TurnRate);
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
	}
	
	@Override
	public void interrupted()
	{
		turnController.disable();
		DriveTrain.stop();
	}
	
	@Override
	public void pidWrite(double output) {
		TurnRate = output;
	}

}
