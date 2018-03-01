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
import org.usfirst.frc.team1317.robot.commands.DanceFull;
import org.usfirst.frc.team1317.robot.commands.DanceSine;
import org.usfirst.frc.team1317.robot.commands.DriveInchesAccelerate;
import org.usfirst.frc.team1317.robot.commands.DriveInchesPID;
import org.usfirst.frc.team1317.robot.commands.DropIfSide;
import org.usfirst.frc.team1317.robot.commands.PositionElevator;
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
	public static final double DEFAULT_ACCELERATION = 1;
	public static final double DEFAULT_MAX_SPEED = 0.7;
    
	static double ultrasonicAverageDistance = 0.0;
	
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
	Command armUp = new ArmUp();
	Command armDown = new ArmDown();
	Command driveTwoFeet;
	Command tempCommand;
	
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
		//m_chooser.addObject("Exchange Auto", "Exchange");
		//m_chooser.addObject("Turn Testing", "TestTurn");
		//m_chooser.addObject("Dance Mode", "Dance");
		//m_chooser.addObject("Dance Sine", "DanceSine");
		m_chooser.addObject("Drive Forward and Drop", "DriveDrop");
		m_chooser.addObject("Dance Full", "DanceFull");
		//puts the chooser on the dashboard
		SmartDashboard.putData("Auto mode", m_chooser);
		
		//adds options to the starting position chooser
		//positionChooser.addObject("Far Left", Far_Left_Position);
		positionChooser.addObject("Left", Left_Position);
		positionChooser.addDefault("Center", Center_Position);
		positionChooser.addObject("Right", Right_Position);
		//positionChooser.addObject("Far Right", Far_Right_Position);
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
		SmartDashboard.putNumber("Offset", 10.0);
		SmartDashboard.putNumber("GoToUltrasonic", 40.0);
		SmartDashboard.putNumber("Drive Inches", 60.0);
		SmartDashboard.putNumber("Elevator Seconds", 1.0);
		SmartDashboard.putNumber("Arm Seconds", 1.0);
		
		LiveWindow.add(armDown);
		LiveWindow.add(armUp);
		//turns the compressor on if the robot is the competition robot
		compressor.setClosedLoopControl(RobotMap.isCompetitionRobot);
		
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
		collectUltrasonicData();
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
			m_autonomousCommand = new DanceSine(2.0);
		}
		else if(mode == "DanceFull") {
			m_autonomousCommand = new DanceFull();
		}
		else if(mode == "DriveDrop") {
			m_autonomousCommand = new DropIfSide(GameData, position, crossFront, delay);
		}
		
		mecanumDriveTrain.zeroGyro();

		//start the autonomous command if it exists
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
			log("Starting Auto Command");
		}
		
		//(new ClawClose()).start();
		
		log("Autonomous Init Complete");
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		//runs commands during autonomous
		collectUltrasonicData();
		Scheduler.getInstance().run();
		mecanumDriveTrain.printNavXYawOutput();
		arm.logSwitch();
		SmartDashboard.putNumber("Average ultrasonic inches", ultrasonicAverageDistance);
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
		collectUltrasonicData();
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
		
		if(OI.TurnJoystick.getPOV() != -1&&mecanumDriveTrain.navX.isConnected()) {
			tempCommand = new TurnToAngle(PIDTurning.equivalentAngle(OI.TurnJoystick.getPOV()), 0.5);
		}
		if(OI.TurnJoystick.getRawButtonPressed(3)) {
			tempCommand = new DriveInchesAccelerate(DEFAULT_ACCELERATION, SmartDashboard.getNumber("Drive Inches", 60.0), DEFAULT_MAX_SPEED);
		} else if(OI.TurnJoystick.getRawButtonPressed(4)) {
			tempCommand = new DriveInchesAccelerate(DEFAULT_ACCELERATION, SmartDashboard.getNumber("Drive Inches", 60.0), DEFAULT_MAX_SPEED, true);
		}
		
		if(OI.TurnJoystick.getRawButtonPressed(1)) {
			tempCommand = ultrasonicDriveToDistance(SmartDashboard.getNumber("GoToUltrasonic", 40.0));
		}
		
		if(OI.OtherJoystick.getRawButtonPressed(9)) {
			tempCommand = new PositionElevatorTime(SmartDashboard.getNumber("Elevator Time", 1.0), 1.0);
		} else if (OI.OtherJoystick.getRawButtonPressed(10) ){
			tempCommand = new PositionElevatorTime(SmartDashboard.getNumber("Elevator Time", 1.0), -1.0);
		}
		
		if(OI.OtherJoystick.getRawButtonPressed(7)) {
			tempCommand = new ArmUp(SmartDashboard.getNumber("Arm Seconds", 1.0));
		} else if(OI.OtherJoystick.getRawButtonPressed(8)) {
			tempCommand = new ArmDown(SmartDashboard.getNumber("Arm Seconds", 1.0));
		}
		
		
		
		//if the temporary command is not null, running, or cancelled (i.e. exists and hasn't started yet), start it.
		if(tempCommand != null && !tempCommand.isRunning() && !tempCommand.isCompleted() && !tempCommand.isCanceled()) {
			tempCommand.start();
		}
		
		//print stuff to smart dashboard or console
		mecanumDriveTrain.printNavXYawOutput();
		mecanumDriveTrain.printNavXDistance();
		mecanumDriveTrain.printEncoderPulses();
		SmartDashboard.putNumber("Ultrasonic (in)", Ultrasonic.getRangeInches());
		SmartDashboard.putNumber("Average ultrasonic inches", ultrasonicAverageDistance);
		SmartDashboard.putNumber("Gear Tooth Counter", gt.getDistance());
		SmartDashboard.putNumber("PID Get Driving Controller", mecanumDriveTrain.getDrivingController().pidGet());
		SmartDashboard.putBoolean("Claw Open", claw.isClawOpen());
		arm.logSwitch();
		String joyMsg = "Ultrasonic (mm) " + Ultrasonic.getRangeMM();
		joyMsg += ", Move Joystick Y " + OI.MoveJoystick.getY();
		joyMsg += ", Move Joystick X " + OI.MoveJoystick.getX();
		joyMsg += ", Turn Joystick X " + OI.TurnJoystick.getX();
		joyMsg += ", Turn Joytcick POV " + OI.TurnJoystick.getPOV();
		//periodicLog.log(joyMsg);
		//periodicLog.log("Average Ultrasonic inches " + ultrasonicAverageDistance);
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
	
	static void collectUltrasonicData () {
		ultrasonicAverageDistance = (0.9*ultrasonicAverageDistance) + (0.1 * Ultrasonic.getAverageRangeInches());
	}
	
	
	public static Command ultrasonicDriveToDistance(double targetPosition) {
		double startPosition = ultrasonicAverageDistance;
		double targetDistance = targetPosition - startPosition;
		Command driveCommand;
		if(targetDistance < 0) {
			driveCommand = new DriveInchesAccelerate(DEFAULT_ACCELERATION, -targetDistance, DEFAULT_MAX_SPEED, true);
		} else {
			driveCommand = new DriveInchesAccelerate(DEFAULT_ACCELERATION, targetDistance, DEFAULT_MAX_SPEED);
		}
		
		return driveCommand;
	}
	
	public static double getUltrasonicAverageDistance() {
		return ultrasonicAverageDistance;
	}
}
