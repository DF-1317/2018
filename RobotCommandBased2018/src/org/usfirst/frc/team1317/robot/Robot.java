/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1317.robot;

import com.cloudbees.syslog.sender.UdpSyslogMessageSender;
import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.SyslogMessage;
import com.cloudbees.syslog.MessageFormat;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1317.robot.commands.*;
import org.usfirst.frc.team1317.robot.subsystems.*;
import org.usfirst.frc.team1317.sensors.UltrasonicHRLVMaxSonar;

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
	public static final int Far_Left_Position = 0;
	public static final int Left_Position = 1;
	public static final int Center_Position = 2;
	public static final int Right_Position = 3;
	public static final int Far_Right_Position = 4;
	
	// Syslog sender
	static UdpSyslogMessageSender syslog = new UdpSyslogMessageSender();
    static final String         ServerHost      = "10.40.0.164";                 // address of the central log server
    static final Facility            Fac             = Facility.LOCAL0;              // what facility is labeled on the msg
    static final Severity            Sev             = Severity.INFORMATIONAL;
	
	String GameData = "";
	
	public UltrasonicHRLVMaxSonar Ultrasonic = new UltrasonicHRLVMaxSonar(RobotMap.UltrasonicPort);
	
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
		
		positionChooser.addObject("Far Left", Far_Left_Position);
		positionChooser.addObject("Left", Left_Position);
		positionChooser.addObject("Center", Center_Position);
		positionChooser.addObject("Right", Right_Position);
		positionChooser.addObject("Far Right", Far_Right_Position);
		SmartDashboard.putData("Position", positionChooser);
		
		log("Position: " + positionChooser);
		
		crossChooser.addObject("Front", true);
		crossChooser.addObject("Behind", false);
		SmartDashboard.putData("Cross", crossChooser);
		
		log("Cross: " + crossChooser);
		
		SmartDashboard.getNumber("Delay", 0);
		
		log("Init Complete");
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
			m_autonomousCommand = new AutonomousForward(position, delay);
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
		
		log("Autonomous Init Complete");
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
		
		log("Teleop Init Complete");
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

		if(OI.OtherJoystick.getRawButtonPressed(1))
		{
			claw.toggleClaw();
		}
		OI.toggleArmButton.whenPressed(new ArmToggle());
		
		
		mecanumDriveTrain.printNavXYawOutput();
		el.PrintEncoderPulses();
		mecanumDriveTrain.printEncoderPulses();
		SmartDashboard.putNumber("Ultrasonic (mm)", Ultrasonic.getRangeMM());
		SmartDashboard.putNumber("Move Joystick Y", OI.MoveJoystick.getY());
		SmartDashboard.putNumber("Move Joystick X", OI.MoveJoystick.getX());
		SmartDashboard.putNumber("Turn Joystick X", OI.TurnJoystick.getX());
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		double speed = 0.3;
		
		if(OI.MoveJoystick.getRawButton(1)) {
			speed = -0.3;
		} else {
			speed = 0.3;
		}
		
		if(OI.MoveJoystick.getRawButton(5)) {
			mecanumDriveTrain.FLMotor.set(speed);
		} else if(OI.MoveJoystick.getRawButton(6)) {
			mecanumDriveTrain.FRMotor.set(speed);
		} else if(OI.MoveJoystick.getRawButton(3)) {
			mecanumDriveTrain.BLMotor.set(speed);
		} else if(OI.MoveJoystick.getRawButton(4)) {
			mecanumDriveTrain.BRMotor.set(speed);
		} else {
			mecanumDriveTrain.stop();
		}
	}
	
	public static void log(String msg) {
        try {
            SyslogMessage m = new SyslogMessage()
                .withFacility(Fac)
                .withSeverity(Sev)
                .withHostname(ServerHost)
                .withAppName("Robot")
                .withProcId("Logger")
                .withMsg(msg);
            syslog.sendMessage(m);
        } catch (Exception e) {
            System.err.println("Ouch: " + e.getMessage());
            e.printStackTrace();
        }
	}
}
