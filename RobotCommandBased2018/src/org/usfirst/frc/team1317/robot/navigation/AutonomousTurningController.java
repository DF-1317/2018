package org.usfirst.frc.team1317.robot.navigation;

import edu.wpi.first.wpilibj.PIDOutput;

public class AutonomousTurningController implements PIDOutput {

	public static final double kP = 0.2;
	public static final double kI = 0.0;
	public static final double kD = 0.0;
	public static final double kF = 0.0;
	
	
	public double steeringError;
	
	public double getSteeringError() {
		return steeringError;
	}
	
	@Override
	public void pidWrite(double output) {
		steeringError = output;
	}

	
}
