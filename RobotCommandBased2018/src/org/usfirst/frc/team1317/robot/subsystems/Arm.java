package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.Logger;
import org.usfirst.frc.team1317.robot.RobotMap;
import org.usfirst.frc.team1317.robot.commands.JoystickArmCommand;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A class representing the robot
 */
public class Arm extends Subsystem {

	WPI_TalonSRX ArmMotor;
	DigitalInput ArmSwitch;
	byte direction = 0;
	boolean upSwitch = false;
	boolean downSwitch = false;
	boolean switchValue;
	Logger syslog = new Logger("1317", "Arm");
	
	
	
	public Arm() {
		super();
		// set up motor
		ArmMotor = new WPI_TalonSRX(RobotMap.ArmMotor);
		ArmMotor.setInverted(false);
		ArmSwitch = new DigitalInput(RobotMap.ArmLimitSwitchPort);
		switchValue = ArmSwitch.get();
	}
	
	public void move(double speed) {
		
		
		if(speed > 0.0) {
			if(upSwitch) {
				ArmMotor.set(0.0);
			}
			else {
				ArmMotor.set(speed);
			}
		}
		else if(speed < 0.0) {
			if(downSwitch) {
				ArmMotor.set(0.0);
			}
		}
		ArmMotor.set(speed);
	}
	
	public boolean topSwitchPressed() {
		return upSwitch;
	}
	
	public boolean bottomSwitchPressed() {
		return downSwitch;
	}
	
	public double getArmSpeed() {
		return ArmMotor.get();
	}

    public void initDefaultCommand() {
        setDefaultCommand(new JoystickArmCommand());
    }
    
    public void logSwitch() {
    	updateSwitchValues();
    	SmartDashboard.putBoolean("Arm Switch", switchValue);
    	SmartDashboard.putBoolean("Top Arm Switch", upSwitch);
    	SmartDashboard.putBoolean("Bottom Arm Switch", downSwitch);
    }
    private void updateSwitchValues() {
    	boolean oldSwitchValue = switchValue;
		switchValue = ArmSwitch.get();
		if(switchValue != oldSwitchValue) {
			syslog.log("Switch: " + switchValue);
			if(switchValue == true){
				upSwitch = false;
				downSwitch = false;
			}
			else {
				if(ArmMotor.get()>0.0&&downSwitch == false) {
					upSwitch = true;
				}
				else if(ArmMotor.get()<0.0&&upSwitch == false) {
					downSwitch = true;
				}
			}
		}
    }
}

