package org.usfirst.frc.team1317.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc.team1317.robot.RobotMap;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 *
 */
public class Claw extends Subsystem {

	DoubleSolenoid ClawPiston;
	Value LastValue;
	
	public Claw()
	{
		ClawPiston = new DoubleSolenoid(RobotMap.ClawPistonPort1,RobotMap.ClawPistonPort2);
	}
	
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	public void OpenClaw()
	{
		ClawPiston.set(Value.kForward);
	}
	
	public void CloseClaw()
	{
		ClawPiston.set(Value.kReverse);
	}
	
	public void RelaxClaw()
	{
		LastValue = ClawPiston.get();
		ClawPiston.set(Value.kOff);
	}
	
	public void ToggleClaw()
	{
		if(ClawPiston.get() == Value.kForward)
		{
			ClawPiston.set(Value.kReverse);
		}
		else if(ClawPiston.get() == Value.kReverse)
		{
			ClawPiston.set(Value.kForward);
		}
		else
		{
			ClawPiston.set(LastValue);
		}
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

