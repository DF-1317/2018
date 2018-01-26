/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1317.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	

	//The ports for the drive train motors
	public static int FLMotorPort = 2;
	public static int FRMotorPort = 1;
	public static int BLMotorPort = 4;
	public static int BRMotorPort = 3;
	
	public static int ElevatorPort = 5;
	
	//joystick ports
	public static int MoveJoystickPort = 2;
	public static int TurnJoystickPort = 1;
	public static int OtherJoystickPort = 0;
	
	public static int FLMotorEncoderPort1 = 0;
	public static int FLMotorEncoderPort2 = 1;
	public static int FRMotorEncoderPort1 = 2;
	public static int FRMotorEncoderPort2 = 3;
	public static int BLMotorEncoderPort1 = 4;
	public static int BLMotorEncoderPort2 = 5;
	public static int BRMotorEncoderPort1 = 6;
	public static int BRMotorEncoderPort2 = 7;
	public static int ElevatorMotorEncoderPort1 = 8;
	public static int ElevatorMotorEncoderPort2 = 9;
	
	public static int DriveTrainPistonPort = 0;
	public static int ClawPistonPort1 = 1;
	public static int ClawPistonPort2 = 2;
	
	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
}
