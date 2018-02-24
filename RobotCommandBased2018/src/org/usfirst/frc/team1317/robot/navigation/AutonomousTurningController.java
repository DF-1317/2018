package org.usfirst.frc.team1317.robot.navigation;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.PIDOutput;

/**
 * 
 * This class functions as a PIDOutput that either writes the value the a variable to be used by another controller or turns the robot.
 * These modes can be switched using  <code>setMode</code>.
 *
 */
public class AutonomousTurningController implements PIDOutput {

	public static final double kP = 0.015;
	public static final double kI = 0.0;
	public static final double kD = 0.0;
	public static final double kF = 0.0;

	/**
	 * The modes the AutonomousTurningController can be set to.
	 *
	 */
	public enum TurnMode {
		withDriving,
		withoutDriving;
	}
	
	TurnMode mode;
	
	double steeringError = 0;

	/**
	 * 
	 * @return the latest output from the PIDController
	 */
	public double getSteeringError() {
		return steeringError;
	}
	
	/**
	 * sets the mode that controls what the class does with the output from the PIDController
	 * 
	 * @param mode - <code>TurnMode.withDriving</code> means that the output will not directly turn the robot. <code>TurnMode.withoutDriving</code> means that as a PIDOutput, the class will turn the robot.
	 */
	public void setMode(TurnMode mode) {
		this.mode = mode;
	}
	
	@Override
	public void pidWrite(double output) {
		steeringError = output;
		if(mode == TurnMode.withoutDriving) {
			Robot.mecanumDriveTrain.driveCartesian(0.0, 0.0, output);
		}
	}

	
}
