package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.RoboMapTest;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * This class represents the Climber motor for the robot
 */
public class Climber extends Subsystem {
	// Object representing motor that climbs
	WPI_TalonSRX ClimberMotor;
	
	public Climber() {
		super();
		// Declare climber motor
		ClimberMotor = new WPI_TalonSRX(RoboMapTest.ClimberPort);
		// Just in case
		ClimberMotor.setInverted(false);
	}
	
	/**
	 * Sets the climber motor's speed.
	 * @param speed from 0 to 1.
	 */
	public void move(double speed) {
		ClimberMotor.set(speed);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

