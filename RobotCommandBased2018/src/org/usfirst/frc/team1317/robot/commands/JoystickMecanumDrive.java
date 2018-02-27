/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team1317.robot.OI;
import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.Logger;

/**
 * This command attaches joystick input to the drive train
 */

public class JoystickMecanumDrive extends Command {

	static final Logger syslog = new Logger("1317", JoystickMecanumDrive.class.getSimpleName());

	// variables storing values for button toggle
	boolean isAdvancedEnabled = false;
	double turnModifier = 0;
	double speedModifier = 0;
	
	boolean isAlignedEnabled;
	double gyroPosition;
	
	public JoystickMecanumDrive() {
		//set up the base class
		super("JoystickMecanumDrive");
		//this command requires the mecanum drive train
		requires(Robot.mecanumDriveTrain);
		isAlignedEnabled = false;
		syslog.log("JoystickMecanumDrive constructed");
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
		if(isAdvancedEnabled) {
			// Enables joystick twist turning and throttle adjustment
			turnModifier = OI.MoveJoystick.getTwist() * 0.3;
			speedModifier = (OI.MoveJoystick.getThrottle() - 1) / -2;
		} else {
			// Disables joystick twist turning and throttle adjustment
			turnModifier = 0.0;
			speedModifier = 1.0;
		}
		
		if(isAlignedEnabled) {
			gyroPosition = Robot.mecanumDriveTrain.navX.getAngle();
		} else {
			gyroPosition = 0.0;
		}
		
		//the "MoveJoystick" causes the robot to move forward, backward, right, and left
		//the "TurnJoystick" causes the robot to turn when the joystick is moved left and right
		Robot.mecanumDriveTrain.driveCartesian(OI.MoveJoystick.getX() * speedModifier, -(OI.MoveJoystick.getY()*speedModifier), (OI.TurnJoystick.getX()+ turnModifier)*speedModifier,gyroPosition);
		
		// Toggle extra control options
		if(OI.MoveJoystick.getRawButtonPressed(9)) {
			isAdvancedEnabled = !isAdvancedEnabled;
			syslog.log("Control mode toggled. Advanced controls: " + isAdvancedEnabled);
		}
		
		if(OI.MoveJoystick.getRawButtonPressed(8)) {
			isAlignedEnabled = !isAlignedEnabled;
			Robot.log("Control mode toggled. Movement aligned to field: " + isAlignedEnabled);
		}
		
		SmartDashboard.putBoolean("Advanced Controls", isAdvancedEnabled);
		SmartDashboard.putBoolean("Field Oriented Drive", isAlignedEnabled);
		
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
