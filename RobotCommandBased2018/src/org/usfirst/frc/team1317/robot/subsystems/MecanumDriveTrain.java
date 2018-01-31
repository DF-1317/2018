/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1317.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.*;
import org.usfirst.frc.team1317.robot.commands.JoystickMecanumDrive;

import edu.wpi.first.wpilibj.*;

/**
 * 
 */
public class MecanumDriveTrain extends Subsystem {

	//object representing drivetrain
	MecanumDrive DriveTrain;

	public MecanumDriveTrain()
	{
		
	}
	
	//set up the DriveTrain class with the motor controllers.
	//The inheriting classes choose a specific type of SpeedController
	public void Initialize(SpeedController FLMotor, SpeedController FRMotor, SpeedController BLMotor, SpeedController BRMotor) {
		//set up the drive train
		DriveTrain = new MecanumDrive(FLMotor, BLMotor, FRMotor, BRMotor);
	}
	
	//basically these methods just let the methods from the MecanumDrive to be used from this class.
	public void driveCartesian(double ySpeed, double xSpeed, double zRotation)
	{
		DriveTrain.driveCartesian(ySpeed, xSpeed, zRotation);
	}
	public void drivePolar(double magnitude, double angle, double zRotation)
	{
		DriveTrain.drivePolar(magnitude, angle, zRotation);
	}
	public void stop()
	{
		DriveTrain.stopMotor();
	}
	public void initDefaultCommand()
	{
		setDefaultCommand(new JoystickMecanumDrive());
	}
}
