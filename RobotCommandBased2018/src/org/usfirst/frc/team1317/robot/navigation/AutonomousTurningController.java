package org.usfirst.frc.team1317.robot.navigation;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class AutonomousTurningController implements PIDOutput {

	public static final double kP = 0.2;
	public static final double kI = 0.0;
	public static final double kD = 0.0;
	public static final double kF = 0.0;
	
	public enum TurnMode {
		withDriving,
		withoutDriving;
	}
	
	TurnMode mode;
	
	double steeringError;
	
	public double getSteeringError() {
		return steeringError;
	}
	
	public void setMode(TurnMode mode) {
		this.mode = mode;
	}
	
	@Override
	public void pidWrite(double output) {
		steeringError = output;
		if(mode == TurnMode.withoutDriving) {
			Robot.mecanumDriveTrain.driveCartesian(0.0, 0.0, 0.0);
		}
	}

	
}
