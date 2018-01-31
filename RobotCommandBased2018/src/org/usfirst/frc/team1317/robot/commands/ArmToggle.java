package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.command.ConditionalCommand;

/**
 *
 */
public class ArmToggle extends ConditionalCommand {

	Arm arm;
	
    public ArmToggle() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	super(new ArmUp(), new ArmDown());
    	arm = Robot.arm;
    }

	@Override
	protected boolean condition() {
		
		boolean returnValue = false;
		
		if (arm.topSwitchPressed() || arm.getArmSpeed() > 0) {
			returnValue = true;
		}
		return returnValue;
	}
}