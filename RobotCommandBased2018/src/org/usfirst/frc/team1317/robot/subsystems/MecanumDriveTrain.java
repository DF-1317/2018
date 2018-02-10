/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1317.robot.subsystems;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.*;
import org.usfirst.frc.team1317.robot.commands.JoystickMecanumDrive;

import edu.wpi.first.wpilibj.*;

/**
 * represents a mecanum drivetrain with general motor controllers. <code>initialize</code> must be called to allow the other methods to work.
 */
public class MecanumDriveTrain extends Subsystem {

	//object representing drivetrain
	MecanumDrive DriveTrain;
	

	public MecanumDriveTrain()
	{
		
	}
	
	//set up the DriveTrain class with the motor controllers.
	//The inheriting classes choose a specific type of SpeedController
	/**
	 * sets up the motor controllers to use. This method must be called before using any other methods.
	 * 
	 * @param FLMotor - the motor controller that is controlling the front left motor
	 * @param FRMotor - the motor controller that is controlling the front right motor
	 * @param BLMotor - the motor controller that is controlling the back left motor
	 * @param BRMotor - the motor controller that is controlling the back right motor
	 */
	public void initialize(SpeedController FLMotor, SpeedController FRMotor, SpeedController BLMotor, SpeedController BRMotor) {
		//set up the drive train
		DriveTrain = new MecanumDrive(FLMotor, BLMotor, FRMotor, BRMotor);
	}
	
	//basically these methods just let the methods from the MecanumDrive to be used from this class.
	/**
	 * drives the robot using cartesian coordinates
	 * 
	 * @param ySpeed - the speed as percentage of maximum forward or backward from -1.0 to 1.0
	 * @param xSpeed - the speed to drive sideways at
	 * @param zRotation - the speed to rotate at
	 */
	public void driveCartesian(double ySpeed, double xSpeed, double zRotation)
	{
		DriveTrain.driveCartesian(ySpeed, xSpeed, zRotation);
	}
	
	/**
	 * drives the robot using polar coordinates
	 * 
	 * @param magnitude - the magnitude of the polar coordinate to drive from -1.0 to 1.0
	 * @param angle - the direction the robot should drive in in degrees from -180.0 to 180.0. Measured counter-clockwise from straight ahead.
	 * @param zRotation - the rate at which the robot turns from -1.0 to 1.0
	 */
	public void drivePolar(double magnitude, double angle, double zRotation)
	{
		DriveTrain.drivePolar(magnitude, angle, zRotation);
	}
	
	/**
	 * stops all the motors in the drive train.
	 */
	public void stop()
	{
		DriveTrain.stopMotor();
	}
	
	
	@Override
	public void initDefaultCommand()
	{
		setDefaultCommand(new JoystickMecanumDrive());
	}
}
