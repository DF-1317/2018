package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Arm extends Subsystem {

	WPI_TalonSRX ArmMotor;
	
	public Arm() {
		super();
		// set up motor
		ArmMotor = new WPI_TalonSRX(RobotMap.ArmMotor);
		
		ArmMotor.setInverted(false);
	}
	
	public void move(double speed) {
		ArmMotor.set(speed);
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

