package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.RobotMap;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * A class representing the claw on the robot.
 */
public class Claw extends Subsystem {

	DoubleSolenoid ClawPiston;
	Value LastValue;
	
	public Claw()
	{
		ClawPiston = new DoubleSolenoid(RobotMap.ClawPistonPort1,RobotMap.ClawPistonPort2);
	}
	
    /**
     * opens the robot's claw by extending the pneumatic piston
     */
	public void openClaw()
	{
		ClawPiston.set(Value.kForward);
	}
	
	/**
	 * closes the robot's claw by retracting the pneumatic piston
	 */
	public void closeClaw()
	{
		ClawPiston.set(Value.kReverse);
	}
	
	/**
	 * Turns the pneumatic piston controlling the claw
	 */
	public void relaxClaw()
	{
		LastValue = ClawPiston.get();
		ClawPiston.set(Value.kOff);
	}
	
	/**
	 * Closes the claw if it is open and opens the claw if it is closed.
	 * If the claw is neither open nor closed, sets it to its last value.
	 */
	public void toggleClaw()
	{
		if(ClawPiston.get() == Value.kForward)
		{
			closeClaw();
		}
		else if(ClawPiston.get() == Value.kReverse)
		{
			openClaw();
		}
		else
		{
			ClawPiston.set(LastValue);
		}
	}
	
	@Override
    public void initDefaultCommand() {
       
    }
}

