/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1317.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
	//the first joystick from the left is the joystick used for turning the robot
	public static final Joystick TurnJoystick = new Joystick(RobotMap.TurnJoystickPort);
	//the second joystick from the left is the joystick used for moving the robot
	public static final Joystick MoveJoystick = new Joystick(RobotMap.MoveJoystickPort);
	//the third joystick from the left is the other joystick
	public static final Joystick OtherJoystick = new Joystick(RobotMap.OtherJoystickPort);
	// The button that toggles the arm
	public static final Button toggleArmButton = new JoystickButton(OtherJoystick, 2);
	public static final Button testTurnButton = new JoystickButton(MoveJoystick, 10);
	
	/* ######################################################################
	 * #                                                                    #
	 * #   						    Buttons						    		#
	 * #                                                                    #
	 * ######################################################################
	 *
	 * ### Other Joystick ###
	 * Button 1: Toggles the claw (Robot.teleopPeriodic)
	 * Button 2: Toggles the arm (this)
	 * POV Up: Move arm up (Robot.teleopPeriodic)
	 * POV Down: Move arm down (Robot.teleopPeriodic)
	 *
	 * ### Turn Joystick ###
	 * Button 9: Zeroes the gyro rotation (Robot.teleopPeriodic)
	 * POV: Turn in specified direction (Robot.teleopPeriodic)
	 *
	 * ### Move Joystick ###
	 * Button 1: Toggles the traction wheels (Robot.teleopPeriodic)
	 * Button 11: Toggles variable speed and joystick twist turning (JoystickMecanumDrive.execute)
	 * Button 12: Resets the NavX distance (Robot.teleopPeridodic)
	 * POV Up: Climb (Robot.teleopPeriodic)
	 * POV Down: Un-climb (Robot.teleopPeriodic)
	 */
	

	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.

	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:

	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());

	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());

	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
}
