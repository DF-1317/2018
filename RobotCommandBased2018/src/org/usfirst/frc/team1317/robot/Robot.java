/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1317.robot;

import org.usfirst.frc.team1317.robot.commands.ArmDown;
import org.usfirst.frc.team1317.robot.commands.ArmToggle;
import org.usfirst.frc.team1317.robot.commands.ArmUp;
import org.usfirst.frc.team1317.robot.commands.AutonomousExchange;
import org.usfirst.frc.team1317.robot.commands.AutonomousForward;
import org.usfirst.frc.team1317.robot.commands.AutonomousScale;
import org.usfirst.frc.team1317.robot.commands.AutonomousSwitch;
import org.usfirst.frc.team1317.robot.commands.ClawClose;
import org.usfirst.frc.team1317.robot.commands.ClimbAlertWait;
import org.usfirst.frc.team1317.robot.commands.Dance;
import org.usfirst.frc.team1317.robot.commands.DanceSine;
import org.usfirst.frc.team1317.robot.commands.DriveInchesAccelerate;
import org.usfirst.frc.team1317.robot.commands.DriveInchesPID;
import org.usfirst.frc.team1317.robot.commands.PositionElevatorTime;
import org.usfirst.frc.team1317.robot.commands.TurnToAngle;
import org.usfirst.frc.team1317.robot.subsystems.Arm;
import org.usfirst.frc.team1317.robot.subsystems.Claw;
import org.usfirst.frc.team1317.robot.subsystems.Climber;
import org.usfirst.frc.team1317.robot.subsystems.Elevator;
import org.usfirst.frc.team1317.robot.subsystems.MecanumDriveTrainCAN;
import org.usfirst.frc.team1317.sensors.AnalogUltrasonic;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GearTooth;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Ultrasonic.Unit;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {

	public static final Logger syslog = new Logger("1317", Robot.class.getSimpleName());
	public static final Logger periodicLog = new Logger("1317", Robot.class.getSimpleName(), 250);

	//classes representing Robot subsystems
	public static final MecanumDriveTrainCAN mecanumDriveTrain
			= new MecanumDriveTrainCAN();
	public static final Claw claw = new Claw();
	
	public static final Elevator el = new Elevator();
	public static final Arm arm = new Arm();
	public static final Climber climb = new Climber();
	
	public static final Compressor compressor = new Compressor();
	public static final AnalogUltrasonic Ultrasonic = new AnalogUltrasonic(RobotMap.UltrasonicPort);
	public static final GearTooth gt = new GearTooth(RobotMap.GearCounterPort);
	
	//Class representing joysticks and driver station controls
	public static OI m_oi;
	
	
	//positions that the robot can start in
	public static final int Far_Left_Position = 0;
	public static final int Left_Position = 1;
	public static final int Center_Position = 2;
	public static final int Right_Position = 3;
	public static final int Far_Right_Position = 4;
	
	//default values for autonomous commands
	public static final double DEFAULT_ACCELERATION = 0.5;
	public static final double DEFAULT_MAX_SPEED = 0.7;
    
    //Data telling where our plates for the switches and scale
	String GameData = "";
	
	//a command that tells the robot what to do in autonomous
	Command m_autonomousCommand;
	
	//Choosers for autonomous mode
	SendableChooser<String> m_chooser = new SendableChooser<>();
	SendableChooser<Integer> positionChooser = new SendableChooser<>();
	SendableChooser<Boolean> crossChooser = new SendableChooser<>();
	
	Command TurnCommand = new TurnToAngle(90.0);
	Command endgameAlert = new ClimbAlertWait();
	Command elevatorUp = new PositionElevatorTime(0.5, 0.7);
	Command elevatorDown = new PositionElevatorTime(0.5, -0.7);
	Command armUp = new ArmUp();
	Command armDown = new ArmDown();
	Command driveTwoFeet;
	
	// Booleans for controlling robot with POVs
	boolean armMoving = false;
	boolean climberMoving = false;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
	    log("Entering robotInit");
	    
		m_oi = new OI();
		
		mecanumDriveTrain.initPIDControllers();
		Ultrasonic.setUnit(Unit.kInches);
		
		//adds options to autonomous mode choosers
		m_chooser.addDefault("Switch Auto", "Switch");
		m_chooser.addObject("Scale Auto", "Scale");
		m_chooser.addObject("DriveForward Auto", "Forward");
		m_chooser.addObject("Exchange Auto", "Exchange");
		m_chooser.addObject("Turn Testing", "TestTurn");
		m_chooser.addObject("Dance Mode", "Dance");
		m_chooser.addObject("Dance Sine", "DanceSine");
		//puts the chooser on the dashboard
		SmartDashboard.putData("Auto mode", m_chooser);
		
		//adds options to the starting position chooser
		positionChooser.addObject("Far Left", Far_Left_Position);
		positionChooser.addObject("Left", Left_Position);
		positionChooser.addDefault("Center", Center_Position);
		positionChooser.addObject("Right", Right_Position);
		positionChooser.addObject("Far Right", Far_Right_Position);
		//puts chooser on dashboard
		SmartDashboard.putData("Position", positionChooser);
		
		log("Position: " + positionChooser);
		
		//adds options to choosing which way to cross court
		crossChooser.addObject("Front", true);
		crossChooser.addDefault("Behind", false);
		SmartDashboard.putData("Cross", crossChooser);
		
		log("Cross: " + crossChooser);
		
		//puts a box to input the delay before starting autonomous
		SmartDashboard.putNumber("Delay", 0.0);
		
		LiveWindow.add(armDown);
		LiveWindow.add(armUp);
		LiveWindow.add(elevatorDown);
		LiveWindow.add(elevatorUp);
		//compressor.setClosedLoopControl(false);
		
		driveTwoFeet = new DriveInchesPID(24.0, 0.0);
		log("Init Complete");
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		//reset the game data when the robot is disabled
		GameData = "";
		SmartDashboard.putBoolean("Endgame", false);
		endgameAlert.cancel();
	}

	@Override
	public void disabledPeriodic() {
		//Automatically runs the scheduler (which runs the commands)
		Scheduler.getInstance().run();
		//periodically tries to get the GameData
		GameData = DriverStation.getInstance().getGameSpecificMessage();
		arm.logSwitch();
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
		log("Entering autonomousInit");
		//if the game data has not been found, try to get it again
		if(GameData == null||GameData.length()<3)
			GameData = DriverStation.getInstance().getGameSpecificMessage();
		
		//get options from dashboard
		String mode = m_chooser.getSelected();
		int position = positionChooser.getSelected();
		boolean crossFront = crossChooser.getSelected();
		double delay = SmartDashboard.getNumber("Delay", 0);
		
		log("Game Data: " + GameData);
		log("Auto Mode: " + mode);
		log("Position: " + position);
		log("Cross Front?" + crossFront);
		log("Delay: " + delay);

		//The autonomous command variable should hold the command for the sellected mode. If the game data has not been found, drive forward
		if(mode == "Forward"||GameData =="") {
			m_autonomousCommand = new AutonomousForward(position, delay);
		}
		else if(mode == "Switch") {
			m_autonomousCommand = new AutonomousSwitch(GameData, position, crossFront, delay);
		}
		else if(mode == "Scale") {
			m_autonomousCommand = new AutonomousScale(GameData, position, crossFront, delay);
		}
		else if(mode == "Exchange") {
			m_autonomousCommand = new AutonomousExchange(position, delay);
		}
		else if(mode == "TestTurn") {
			m_autonomousCommand = TurnCommand;
		}
		else if(mode == "Dance") {
			m_autonomousCommand = new Dance();
		}
		else if(mode == "DanceSine") {
			m_autonomousCommand = new DanceSine();
		}

		//start the autonomous command if it exists
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
			log("Starting Auto Command");
		}
		
		(new ClawClose()).start();
		
		log("Autonomous Init Complete");
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		//runs commands during autonomous
		Scheduler.getInstance().run();
		mecanumDriveTrain.printNavXYawOutput();
		arm.logSwitch();
	}

	@Override
	public void teleopInit() {
		log("Entering teleopInit");
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
			log("Canceled Autonomous command");
			m_autonomousCommand.free();
		}
		m_autonomousCommand = null;
		
		endgameAlert.start();
		
		log("Teleop Init Complete");
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		//runs commands during teleop
		Scheduler.getInstance().run();
		//toggle the traction wheels if the trigger is pressed on the move joystick
		if(OI.MoveJoystick.getRawButtonPressed(1))
		{
			mecanumDriveTrain.toggleTractionWheels();
		}
		
		//zero the gyro sensor when button 12 is pressed on the turn joystick
		if(OI.TurnJoystick.getRawButtonPressed(9)) {
			mecanumDriveTrain.zeroGyro();
			log("Gyro zeroed");
		}

		//toggle the claw when the trigger is pressed on the other joystick
		if(OI.OtherJoystick.getRawButtonPressed(1))
		{
			claw.toggleClaw();
		}
		//toggle the arm when the arm button is pressed
		OI.toggleArmButton.whenPressed(new ArmToggle());
		
		if(OI.MoveJoystick.getRawButtonPressed(12)) {
			mecanumDriveTrain.resetNavXDistance();
			mecanumDriveTrain.resetEncoderDistance();
		}
		
		// Climb up and down
		if(OI.OtherJoystick.getPOV() == 0) {
			climb.move(-1);
			climberMoving = true;
		} else if(OI.OtherJoystick.getPOV() == 180) {
			climb.move(1);
			climberMoving = true;
		} else if(climberMoving) {
			climb.move(0);
			climberMoving = false;
		}
		
		// Move arm up and down
		/*if(OI.OtherJoystick.getPOV() == 0) {
			arm.move(1);
			armMoving = true;
		} else if(OI.MoveJoystick.getPOV() == 180) {
			arm.move(-1);
			armMoving = true;
		} else if(armMoving) {
			arm.move(0);
			armMoving = false;
		}*/
		
		if(OI.TurnJoystick.getPOV() != -1&&mecanumDriveTrain.navX.isConnected()) {
			new TurnToAngle(PIDTurning.equivalentAngle(OI.TurnJoystick.getPOV()), 0.5).start();
		}
		if(OI.TurnJoystick.getRawButtonPressed(3)) {
			(new DriveInchesAccelerate(DEFAULT_ACCELERATION, 240.0, DEFAULT_MAX_SPEED)).start();
		} else if(OI.TurnJoystick.getRawButtonPressed(4)) {
			(new DriveInchesAccelerate(DEFAULT_ACCELERATION, 240.0, DEFAULT_MAX_SPEED, true)).start();
		}
		if(OI.OtherJoystick.getRawButtonPressed(9)) {
			elevatorUp.start();
		} else if (OI.OtherJoystick.getRawButtonPressed(10) ){
			elevatorDown.start();
		}
		
		//print stuff to smart dashboard or console
		mecanumDriveTrain.printNavXYawOutput();
		mecanumDriveTrain.printNavXDistance();
		el.PrintEncoderPulses();
		mecanumDriveTrain.printEncoderPulses();
		mecanumDriveTrain.resetNavXDistance();
		SmartDashboard.putNumber("Ultrasonic (mm)", Ultrasonic.getRangeMM());
		SmartDashboard.putNumber("Move Joystick Y", OI.MoveJoystick.getY());
		SmartDashboard.putNumber("Move Joystick X", OI.MoveJoystick.getX());
		SmartDashboard.putNumber("Turn Joystick X", OI.TurnJoystick.getX());
		SmartDashboard.putNumber("TurnJoystick POV", OI.TurnJoystick.getPOV());
		SmartDashboard.putNumber("Gear Tooth Counter", gt.getDistance());
		SmartDashboard.putBoolean("Claw Open", claw.isClawOpen());
		arm.logSwitch();
		String joyMsg = "Ultrasonic (mm) " + Ultrasonic.getRangeMM();
		joyMsg += ", Move Joystick Y " + OI.MoveJoystick.getY();
		joyMsg += ", Move Joystick X " + OI.MoveJoystick.getX();
		joyMsg += ", Turn Joystick X " + OI.TurnJoystick.getX();
		joyMsg += ", Turn Joytcick POV " + OI.TurnJoystick.getPOV();
		//periodicLog.log(joyMsg);
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
		log("Entering testPeriodic");
		double speed = 0.3;
		
		if(OI.MoveJoystick.getRawButton(1) || OI.OtherJoystick.getRawButton(1)) {
			speed = -0.3;
		} else {
			speed = 0.3;
		}
		
		if(OI.MoveJoystick.getRawButton(5)) {
			mecanumDriveTrain.FLMotor.set(speed);
		}
		if(OI.MoveJoystick.getRawButton(6)) {
			mecanumDriveTrain.FRMotor.set(speed);
		}
		if(OI.MoveJoystick.getRawButton(3)) {
			mecanumDriveTrain.BLMotor.set(speed);
		}
		if(OI.MoveJoystick.getRawButton(4)) {
			mecanumDriveTrain.BRMotor.set(speed);
		}
		if(OI.MoveJoystick.getRawButton(2)){
			mecanumDriveTrain.stop();
		} 
		
		// Test arm motor
		if(OI.OtherJoystick.getRawButton(3)) {
			arm.move(speed);
		} else {
			arm.move(0);
		}
		// Test elevator motor
		if(OI.OtherJoystick.getRawButton(4)) {
			el.move(speed);
		} else {
			el.move(0);
		}
		// Test climb motor (when added to robot and class made)
		if(OI.OtherJoystick.getRawButton(5)) {
			climb.move(speed);
		} else {
			climb.move(0);
		}
		// Test claw (toggle)
		if(OI.OtherJoystick.getRawButtonPressed(6)) {
			claw.toggleClaw();
		}
		if(OI.MoveJoystick.getRawButtonPressed(12)) {
			mecanumDriveTrain.resetEncoderDistance();
		}
		if(OI.OtherJoystick.getRawButtonPressed(11))
			
		OI.testTurnButton.whenPressed(TurnCommand);
	}
	
	/**
	 * Sends message to server host
	 * @param msg message to be sent to server host
	 */
	public static void log(String msg) {
		syslog.log(msg);
	}
	
	public static Command ultrasonicDriveToDistance(double targetPosition) {
		double startPosition = Ultrasonic.getRangeInches();
		double targetDistance = targetPosition - startPosition;
		Command driveCommand;
		
		if(targetDistance < 0) {
			driveCommand = new DriveInchesAccelerate(DEFAULT_ACCELERATION, -targetDistance, DEFAULT_MAX_SPEED, true);
		} else {
			driveCommand = new DriveInchesAccelerate(DEFAULT_ACCELERATION, targetDistance, DEFAULT_MAX_SPEED);
		}
		
		return driveCommand;
	}
}
