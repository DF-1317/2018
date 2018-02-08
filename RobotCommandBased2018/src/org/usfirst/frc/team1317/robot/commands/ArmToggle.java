package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.command.ConditionalCommand;

/**
 *This command moves the arm up if it is down and down if it is up.
 *
 *@see ArmUp
 *@see ArmDown
 */
public class ArmToggle extends ConditionalCommand {

	Arm arm;
	
    public ArmToggle() {
    	super(new ArmUp(), new ArmDown());
    	arm = Robot.arm;
    }

	@Override
	protected boolean condition() {
		//assume that the arm is up
		boolean returnValue = false;
		
		//Question our assumption
		//We will prove our assumption wrong if the top switch is pressed or if the arm is moving forward
		if (arm.topSwitchPressed() || arm.getArmSpeed() > 0) {
			returnValue = true;
		}
		return returnValue;
	}
}