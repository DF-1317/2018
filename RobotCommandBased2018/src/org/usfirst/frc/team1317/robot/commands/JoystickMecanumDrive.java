/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1317.robot.OI;
import org.usfirst.frc.team1317.robot.Robot;

/**
 * An example command.  You can replace me with your own command.
 */

public class JoystickMecanumDrive extends Command {
	
	boolean isTwistEnabled = false;
	double turnModifier = 0;
	double speedModifier = 0;
	
	public JoystickMecanumDrive() {
		//set up the base class
		super("JoystickMecanumDrive");
		//this command requires the mecanum drive train
		requires(Robot.mecanumDriveTrain);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		//this command can be interrupted
		setInterruptible(true);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		
		// Only have extra control options when enabled
		if(isTwistEnabled) {
			turnModifier = OI.MoveJoystick.getTwist() * 0.3;
			speedModifier = (OI.MoveJoystick.getThrottle() - 1) / -2;
		} else {
			turnModifier = 0;
			speedModifier = 1;
		}
		
		//the "MoveJoystick" causes the robot to move forward, backward, right, and left
		//the "TurnJoystick" causes the robot to turn when the joystick is moved left and right
		Robot.mecanumDriveTrain.driveCartesian(OI.MoveJoystick.getX() * speedModifier, (OI.TurnJoystick.getX() + turnModifier) *
				speedModifier, -OI.MoveJoystick.getY() * speedModifier);
		
		// Toggle extra control options
		if(OI.MoveJoystick.getRawButtonPressed(11)) {
			isTwistEnabled = !isTwistEnabled;
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
		//stop the drive train from moving when the command is over
		Robot.mecanumDriveTrain.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		//stops the drive train from moving when the command is interrupted
		Robot.mecanumDriveTrain.stop();
	}
}
