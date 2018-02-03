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
	public final static int FLMotorPort = 3;
	public final static int FRMotorPort = 1;
	public final static int BLMotorPort = 4;
	public final static int BRMotorPort = 2;
	
	public final static int ElevatorPort = 5;
	public final static int ArmMotor = 6;
	
	//joystick ports
	public final static int MoveJoystickPort = 2;
	public final static int TurnJoystickPort = 1;
	public final static int OtherJoystickPort = 0;
	
	//DIO ports
	public final static int FLMotorEncoderPort1 = 4;
	public final static int FLMotorEncoderPort2 = 5;
	public final static int FRMotorEncoderPort1 = 2;
	public final static int FRMotorEncoderPort2 = 3;
	public final static int BLMotorEncoderPort1 = 0;
	public final static int BLMotorEncoderPort2 = 1;
	public final static int BRMotorEncoderPort1 = 6;
	public final static int BRMotorEncoderPort2 = 7;
	public final static int ElevatorMotorEncoderPort1 = 8;
	public final static int ElevatorMotorEncoderPort2 = 9;
	
	//Solenoid ports
	public final static int DriveTrainPistonPort = 0;
	public final static int ClawPistonPort1 = 1;
	public final static int ClawPistonPort2 = 2;
	
	//Analog Ports
	public final static int UltrasonicPort = 0;
	public final static int AnalogGyroPort = 1;
	
	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
}
