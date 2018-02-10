package org.usfirst.frc.team1317.robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import org.usfirst.frc.team1317.robot.subsystems.*;
import com.kauailabs.navx.frc.AHRS;


public class PIDTurning implements PIDOutput {

	MecanumDriveTrain DriveTrain;
	AHRS gyroSensor;
	PIDController turnController;
	static final double kP = 0.02;
	static final double kI = 0.00;
	static final double kD = 0.00;
	static final double kToleranceDegrees = 5.0;
	
	double TurnRate;
	double OriginalDegrees;
	double endingDegrees;
	
	boolean firstTime =true;
	
	public PIDTurning(MecanumDriveTrain drive, AHRS gyro)
	{
		DriveTrain = drive;
		gyroSensor = gyro;
		turnController = new PIDController(kP,kI,kD,0.0,gyro,this);
		turnController.setInputRange(-180.0F, 180.0F);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(kToleranceDegrees);
		turnController.setContinuous(true);
		TurnRate = 0.0;
		turnController.setName("Drive System", "Rotate Controller");
		LiveWindow.add(turnController);
	}
	
	public Boolean turnToDegrees(double degrees, double speed)
	{
		turnController.setSetpoint(degrees);
		turnController.enable();
		if (turnController.onTarget())
		{
			turnController.disable();
			DriveTrain.driveCartesian(0, 0, 0);
			return true;
		}
		else
		{
			DriveTrain.driveCartesian(0, 0, speed*TurnRate);
			return false;
		}
	}
	
	public Boolean turnDegrees(double degrees, double speed)
	{
		if(firstTime == true)
		{
			OriginalDegrees = gyroSensor.getYaw();
			endingDegrees = OriginalDegrees + degrees;
			firstTime =false;
			
		}
		turnController.setSetpoint(endingDegrees);
		turnController.enable();
		if (turnController.onTarget())
		{
			turnController.disable();
			return true;
		}
		else
		{
			DriveTrain.driveCartesian(0, 0, speed*TurnRate);
			return false;
		}
	}
	
	@Override
	public void pidWrite(double output) {
		TurnRate = output;
	}
	
	public void reset()
	{
		firstTime = true;
	}

	public void stop() {
		DriveTrain.stop();
	}
	

	/**
	 * converts an angle in degrees to its equivalent angle that is between -180 degrees and 180 degrees
	 * @param angle - an angle in degrees
	 * @return an angle between -180 degrees and 180 degrees
	 */
	public static double equivalentAngle(double angle)
	{
		while(angle>180.0||angle<-180.0)
		{
			if(angle>180.0)
				angle -=360.0;
			else if(angle<-180.0)
				angle+=360.0;
		}
		return angle;
	}

}
