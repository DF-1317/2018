package org.usfirst.frc.team1317.robot;

import org.usfirst.frc.team1317.robot.subsystems.*;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.*;

public class PIDDriveDistance implements PIDOutput, PIDSource {

	double moveSpeed = 0;
	MecanumDriveTrain driveTrain;
	static final double kP = 0.01;
	static final double kI = 0.00;
	static final double kD = 0.00;
	static final double kToleranceInches = 1.0;
	PIDController moveController;
	AHRS NavXMXP;
	double distancetravelled;
	double velocity;
	double oldVelocity;
	double oldAcceleration;
	double lastTime;
	
	public PIDDriveDistance(MecanumDriveTrain drive, AHRS navx)
	{
		NavXMXP = navx;
		driveTrain = drive;
		moveController = new PIDController(kP,kI,kD,0.00,this,this);
		moveController.setAbsoluteTolerance(kToleranceInches);
		moveController.setInputRange(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		moveController.setOutputRange(-1.0, 1.0);
		moveController.setContinuous(false);
		moveController.setName("Drive System", "Distance Controller");
		LiveWindow.add(moveController);
	}
	
	public Boolean driveForward(double distance, double speed, double heading)
	{
		calculateDistance();
		moveController.setSetpoint(distance);
		moveController.enable();
		if(moveController.onTarget())
		{
			moveController.disable();
			return true;
		}
		else
		{
			driveTrain.driveCartesian(0, speed*moveSpeed, 0.0);
			return false;
		}
	}
	
	public void resetDistance()
	{
		distancetravelled = 0;
		driveTrain.driveCartesian(0, 0, 0);
		oldVelocity = 0;
		oldAcceleration = 0;
		velocity = 0;
		lastTime=Timer.getFPGATimestamp();
	}
	
	void calculateDistance()
	{
		//gets the current time
		double currentTime = Timer.getFPGATimestamp();
		//calculate the amount of time that has passed since this function was last called.
		double changeInTime = currentTime - lastTime;
		//gets the current acceleration
		double currentAcceleration = NavXMXP.getWorldLinearAccelX();
		//calculates the change in velocity by averaging the current acceleration and the old acceleration, converting the acceleration to in/s^2, and multiplying by change in time
		double changeInVelocity = (oldAcceleration+currentAcceleration)*32.174*12/2 *changeInTime;
		//adds the change in velocity to the velocity
		velocity+=changeInVelocity;
		//calculates the change in position from the velocity by averaging the current and old velocity and multiplying by the change in time.
		double changeInPosition = (oldVelocity + velocity)/2 * changeInTime;
		//adds the change in position to the distance traveled
		distancetravelled += changeInPosition;
		//sets the old times to what the times are now
		oldVelocity = velocity;
		oldAcceleration = currentAcceleration;
		lastTime = currentTime;

	}
	
	@Override
	public void pidWrite(double output) {
		moveSpeed = output;
	}
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public double pidGet() {
		return distancetravelled;
	}

}
