/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1317.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1317.robot.commands.*;
import org.usfirst.frc.team1317.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	//a class that represents the robot's drivetrain
	public static final MecanumDriveTrainCAN mecanumDriveTrain
			= new MecanumDriveTrainCAN();
	public static final Claw claw = new Claw();
	public static OI m_oi;
	public static final Elevator el = new Elevator();
	public static final Arm arm = new Arm();
	
	String GameData = "";
	
	public AnalogInput Ultrasonic = new AnalogInput(0);
	
	//a command that tells the robot what to do in autonomous
	Command m_autonomousCommand;
	SendableChooser<String> m_chooser = new SendableChooser<>();
	SendableChooser<Integer> positionChooser = new SendableChooser<>();
	SendableChooser<Boolean> crossChooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_oi = new OI();
		m_chooser.addDefault("Switch Auto", "Switch");
		m_chooser.addObject("Scale Auto", "Scale");
		m_chooser.addObject("DriveForward Auto", "Forward");
		m_chooser.addObject("Exchange Auto", "Exchange");
		SmartDashboard.putData("Auto mode", m_chooser);
		
		positionChooser.addObject("Far Left", 0);
		positionChooser.addObject("Left", 1);
		positionChooser.addObject("Center", 2);
		positionChooser.addObject("Right", 3);
		positionChooser.addObject("Far Right", 4);
		SmartDashboard.putData("Position", positionChooser);
		
		crossChooser.addObject("Front", true);
		crossChooser.addObject("Behind", false);
		SmartDashboard.putData("Cross", crossChooser);
		
		SmartDashboard.getNumber("Delay", 0);
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		GameData = "";
	}

	@Override
	public void disabledPeriodic() {
		//Automatically runs the scheduler (which runs the commands)
		Scheduler.getInstance().run();
		GameData = DriverStation.getInstance().getGameSpecificMessage();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		if(GameData == ""||GameData == null)
			GameData = DriverStation.getInstance().getGameSpecificMessage();
		String mode = m_chooser.getSelected();
		int position = positionChooser.getSelected();
		boolean crossCourt = crossChooser.getSelected();
		double delay = SmartDashboard.getNumber("Delay", 0);
		
		if(mode == "Forward"||GameData =="") {
			m_autonomousCommand = new AutonomousForward(delay);
		}
		else if(mode == "Switch") {
			m_autonomousCommand = new AutonomousSwitch(GameData, position, crossCourt, delay);
		}
		else if(mode == "Scale") {
			m_autonomousCommand = new AutonomousScale(GameData, position, crossCourt, delay);
		}
		else if(mode == "Exchange") {
			m_autonomousCommand = new AutonomousExchange(position, delay);
		}

		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		//runs commands during autonomous
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		//runs commands during teleop
		Scheduler.getInstance().run();
		if(OI.MoveJoystick.getRawButtonPressed(1))
		{
			mecanumDriveTrain.toggleTractionWheels();
		}
		if(OI.TurnJoystick.getRawButtonPressed(12)) {
			mecanumDriveTrain.zeroGyro();
		}
		
		mecanumDriveTrain.printNavXYawOutput();
		el.PrintEncoderPulses();
		SmartDashboard.putNumber("Ultrasonic (mm)", Ultrasonic.getVoltage()*5/(4.88/1000));

		if(OI.OtherJoystick.getRawButtonPressed(1))
		{
			claw.toggleClaw();
		}
		OI.toggleArmButton.whenPressed(new ArmToggle());
		
		SmartDashboard.putNumber("Move Joystick Y", OI.MoveJoystick.getY());
		SmartDashboard.putNumber("Move Joystick X", OI.MoveJoystick.getX());
		SmartDashboard.putNumber("Turn Joystick X", OI.TurnJoystick.getX());
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		if(OI.MoveJoystick.getRawButton(5)) {
			mecanumDriveTrain.FLMotor.set(0.3);
		} else if(OI.MoveJoystick.getRawButton(6)) {
			mecanumDriveTrain.FRMotor.set(0.3);
		} else if(OI.MoveJoystick.getRawButton(3)) {
			mecanumDriveTrain.BLMotor.set(0.3);
		} else if(OI.MoveJoystick.getRawButton(4)) {
			mecanumDriveTrain.BRMotor.set(0.3);
		} else {
			mecanumDriveTrain.stop();
		}
	}
}
