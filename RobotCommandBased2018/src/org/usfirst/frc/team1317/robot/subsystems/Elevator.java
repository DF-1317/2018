package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.RobotMap;
import org.usfirst.frc.team1317.robot.commands.JoystickElevatorCommand;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 */
public class Elevator extends Subsystem {
	// create objects representing the motor controlling the elevator
	public WPI_TalonSRX ElevatorMotor;
	
	// encoders
	public Encoder ElevatorEncoder;
	
	public Elevator() {
		super();
		// set up elevator motor
		ElevatorMotor = new WPI_TalonSRX(RobotMap.ElevatorPort);
		ElevatorEncoder = new Encoder(RobotMap.ElevatorMotorEncoderPort1, RobotMap.ElevatorMotorEncoderPort2);
		// Just in case
		ElevatorMotor.setInverted(false);
		
	}
	
	/**
	 * prints the pulses from the encoder to the SmartDashoard
	 */
	public void PrintEncoderPulses() {
		SmartDashboard.putNumber("Elevator Position", ElevatorEncoder.get());
	}
	
	/**
	 * moves the elevator motor
	 * 
	 * @param speed - the speed the motor on the elevator moves as a ratio of maximum motor power. Range from -1.0 to 1.0
	 */
	public void move(double speed) {
		ElevatorMotor.set(speed);
	}
	
	/**
	 * gets the position of the elevator motor
	 * @return the pulses from the encoder on the motor
	 */
	public double getPosition() {
		return ElevatorEncoder.get();
	}

	@Override
    public void initDefaultCommand() {
    	setDefaultCommand(new JoystickElevatorCommand());
    }
}

