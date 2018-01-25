package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;

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
		// Just in case
		ElevatorMotor.setInverted(false);
		
	}
	
	public void move(double speed) {
		ElevatorMotor.set(speed);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

