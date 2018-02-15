package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.OI;
import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class JoystickArmCommand extends Command {

	Arm arm;
	
    public JoystickArmCommand() {
        requires(Robot.arm);
        arm = Robot.arm;
        setInterruptible(true);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	if(OI.OtherJoystick.getRawButton(2)) {
    		arm.move(-0.5*OI.OtherJoystick.getY());
    	}
    	else if(OI.OtherJoystick.getRawButton(5)) {
    		arm.move(0.3);
    	}
    	else if(OI.OtherJoystick.getRawButton(3)) {
    		arm.move(-0.3);
    	}
    	else {
    		arm.move(0.0);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
}
