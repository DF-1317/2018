package org.usfirst.frc.team1317.robot.navigation;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class AutonomousDrivingController implements PIDSource, PIDOutput {

	Encoder LeftEncoder;
	Encoder RightEncoder;
	AutonomousTurningController turncontroller;
	double turningSpeed;
	double forwardSpeed;
	
	
	PIDSourceType m_pidSource = PIDSourceType.kDisplacement;
	
	public AutonomousDrivingController(AutonomousTurningController controller, double forwardSpeed, double turningSpeed) {
		turncontroller = controller;
		this.forwardSpeed = forwardSpeed;
		this.turningSpeed = turningSpeed;
	}
	
	public void initializeEncoders() {
		LeftEncoder = Robot.mecanumDriveTrain.BLEncoder;
		RightEncoder = Robot.mecanumDriveTrain.BREncoder;
	}
	
	public AutonomousDrivingController(AutonomousTurningController controller) {
		this(controller, 1.0, 1.0);
	}
	
	public void setTurningSpeed(double speed) {
		turningSpeed = speed;
	}
	
	public double getTurningSpeed() {
		return turningSpeed;
	}
	
	public double getForwardSpeed() {
		return forwardSpeed;
	}
	
	public void setForwardSpeed(double speed) {
		forwardSpeed = speed;
	}
	
	
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		m_pidSource = pidSource;
	}
	
	@Override
	public double pidGet() {
		if(m_pidSource == PIDSourceType.kDisplacement) {
			return (LeftEncoder.getDistance() + RightEncoder.getDistance())/2.0;
		}
		else {
			return (LeftEncoder.getRate() + RightEncoder.getRate())/2.0;
		}
	}


	@Override
	public PIDSourceType getPIDSourceType() {
		return m_pidSource;
	}
	
	@Override
	public void pidWrite(double output) {
		double distanceOutput = output;
		double steeringOutput =turncontroller.getSteeringError();
		Robot.mecanumDriveTrain.driveCartesian(0.0, forwardSpeed*distanceOutput, turningSpeed*steeringOutput);
	}







	

}
