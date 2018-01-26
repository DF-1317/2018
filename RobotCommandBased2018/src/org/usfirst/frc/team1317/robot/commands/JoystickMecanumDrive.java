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
		//the "MoveJoystick" causes the robot to move forward, backward, right, and left
		//the "TurnJoystick" causes the robot to turn when the joystick is moved left and right
		Robot.mecanumDriveTrain.driveCartesian(OI.MoveJoystick.getX(), OI.TurnJoystick.getX(), -OI.MoveJoystick.getY());
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
