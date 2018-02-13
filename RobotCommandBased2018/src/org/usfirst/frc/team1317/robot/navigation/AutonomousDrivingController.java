package org.usfirst.frc.team1317.robot.navigation;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.PIDOutput;

public class AutonomousDrivingController implements PIDOutput {

	AutonomousTurningController turncontroller;
	double turningSpeed;
	double forwardSpeed;
	
	public AutonomousDrivingController(AutonomousTurningController controller, double forwardSpeed, double turningSpeed) {
		turncontroller = controller;
		this.forwardSpeed = forwardSpeed;
		this.turningSpeed = turningSpeed;
	}
	
	
	
	@Override
	public void pidWrite(double output) {
		double distanceOutput = output;
		double steeringOutput =turncontroller.getSteeringError();
		Robot.mecanumDriveTrain.driveCartesian(0.0, forwardSpeed*distanceOutput, turningSpeed*steeringOutput);
	}

}
