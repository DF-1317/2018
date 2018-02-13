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
		//I may have to change around + and - signs 
		double left_Speed = forwardSpeed*distanceOutput + turningSpeed*steeringOutput;
		double right_Speed = forwardSpeed*distanceOutput - turningSpeed*steeringOutput;
		Robot.mecanumDriveTrain.FLMotor.set(left_Speed);
		Robot.mecanumDriveTrain.BLMotor.set(left_Speed);
		Robot.mecanumDriveTrain.FRMotor.set(right_Speed);
		Robot.mecanumDriveTrain.BRMotor.set(right_Speed);
	}

}
