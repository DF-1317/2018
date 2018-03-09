package org.usfirst.frc.team1317.robot.navigation;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * <p>This class acts as both a source and an output for a PIDController. </p>
 * <p>As a source, this class returns the average value of two encoders, one on each side of the robot.</p>
 * <p>As an output, this class drives the robot forward according to the PIDController and turns the robot based on an <code>AutonomousTurningController</code></p>
 *
 */
public class AutonomousDrivingController implements PIDSource, PIDOutput {

	Encoder LeftEncoder;
	Encoder RightEncoder;
	AutonomousTurningController turncontroller;
	double turningSpeed;
	double forwardSpeed;
	
	
	PIDSourceType m_pidSource = PIDSourceType.kDisplacement;
	
	/**
	 * constructs the autonomous driving controller with the maximum speeds as options. The <code>initializeEncoders</code> method must be called in order 
	 * 
	 * @param controller - An <code>AutonomousTurningController</code> that is an output for a PIDController that is controlling turning the robot.
	 * @param forwardSpeed - the maximum speed at which the robot should move forward
	 * @param turningSpeed - the maximum speed at which the robot should turn
	 */
	public AutonomousDrivingController(AutonomousTurningController controller, double forwardSpeed, double turningSpeed) {
		turncontroller = controller;
		this.forwardSpeed = forwardSpeed;
		this.turningSpeed = turningSpeed;
	}
	
	/**
	 * constructs this class with default speeds of 1.0
	 * 
	 * @param controller - An <code>AutonomousTurningController</code> that is an output for a PIDController that is controlling turning the robot.
	 */
	public AutonomousDrivingController(AutonomousTurningController controller) {
		this(controller, 1.0, 1.0);
	}
	
	/**
	 * gets the encoders from the mecanumDriveTrain
	 * 
	 * This method must be called before using this as a PID source
	 */
	public void initializeEncoders() {
		LeftEncoder = Robot.mecanumDriveTrain.BLEncoder;
		RightEncoder = Robot.mecanumDriveTrain.BREncoder;
	}
	
	/**
	 * sets the maximum speed at which the robot should turn when using this class
	 * 
	 * @param speed - the maximum speed from 0 to 1.
	 */
	public void setTurningSpeed(double speed) {
		turningSpeed = speed;
	}
	
	/**
	 * 
	 * @return the maximum speed this class is set to turn at.
	 */
	public double getTurningSpeed() {
		return turningSpeed;
	}
	
	/**
	 * 
	 * @return the maximum speed this class is set to drive the robot forward at
	 */
	public double getForwardSpeed() {
		return forwardSpeed;
	}
	
	/**
	 * sets the maximum speed at which the robot should drive forward
	 * 
	 * @param speed - from 0 to 1.
	 */
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
			if(LeftEncoder.getDistance() == 0){
				return RightEncoder.getDistance() * RobotMap.RightEncoderDirection;
			}
			else if(RightEncoder.getDistance() == 0) {
				return LeftEncoder.getDistance() * RobotMap.LeftEncoderDirection;
			}
			else {
				return (LeftEncoder.getDistance()*RobotMap.LeftEncoderDirection + RightEncoder.getDistance()*RobotMap.RightEncoderDirection)/2.0;
			}
		}
		else {
			return (LeftEncoder.getRate() * RobotMap.LeftEncoderDirection + RightEncoder.getRate() * RobotMap.RightEncoderDirection);
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
