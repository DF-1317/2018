package org.usfirst.frc.team1317.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team1317.robot.RobotMap;

import edu.wpi.first.wpilibj.*;

/**
 *
 */
public class Claw extends Subsystem {

	DoubleSolenoid ClawPiston;
	
	public Claw()
	{
		ClawPiston = new DoubleSolenoid(RobotMap.ClawPistonPort1,RobotMap.ClawPistonPort2);
	}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

