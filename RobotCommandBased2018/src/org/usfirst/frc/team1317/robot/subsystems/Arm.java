package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * A class representing the robot
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
	
	public boolean topSwitchPressed() {
		return ArmMotor.getSensorCollection().isFwdLimitSwitchClosed();
	}
	
	public boolean bottomSwitchPressed() {
		return ArmMotor.getSensorCollection().isRevLimitSwitchClosed();
	}
	
	public double getArmSpeed() {
		return ArmMotor.get();
	}

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

