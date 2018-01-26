package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 */
public class Elevator extends Subsystem {
	// create objects representing the motor controlling the elevator
	WPI_TalonSRX ElevatorMotor;
	
	// encoders
	Encoder ElevatorEncoder;
	
	public Elevator() {
		super();
		// set up elevator motor
		ElevatorMotor = new WPI_TalonSRX(RobotMap.ElevatorPort);
		ElevatorEncoder = new Encoder(RobotMap.ElevatorMotorEncoderPort1, RobotMap.ElevatorMotorEncoderPort2);
		// Just in case
		ElevatorMotor.setInverted(false);
		
	}
	
	public void PrintEncoderPulses() {
		
		SmartDashboard.putNumber("Elevator Position", ElevatorEncoder.get());
	}
	
	public void move(double speed) {
		ElevatorMotor.set(speed);
	}
	
	public double getPosition() {
		return ElevatorEncoder.get();
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

