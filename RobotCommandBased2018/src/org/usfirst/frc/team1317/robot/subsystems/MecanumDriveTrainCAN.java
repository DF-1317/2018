package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.RobotMap;
import org.usfirst.frc.team1317.robot.navigation.AutonomousDrivingController;
import org.usfirst.frc.team1317.robot.navigation.AutonomousTurningController;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * this class represents the specific drive train used on Digital Fusion's 2018 robot.
 * The robot uses TalonSRX motor controllers over CAN.
 * 
 * @see MecanumDriveTrain
 *
 */
public class MecanumDriveTrainCAN extends MecanumDriveTrain implements PIDOutput {
	
	//constants representing encoder and wheel properties.
	public static final int TICKS_PER_REVOLUTION = 4096;
	public static final double WHEEL_CIRCUMFERENCE = 6*Math.PI;
	final double distancePerPulse = WHEEL_CIRCUMFERENCE / TICKS_PER_REVOLUTION;
	
	//PID Constants
	final double driveP = 0.01;
	final double driveI = 0.0;
	final double driveD = 0.0;
	final double driveF = 0.0;
	final double driveToleranceInches = 1.0;
	
	final double turnP = 0.02;
	final double turnI = 0.0;
	final double turnD = 0.0;
	final double turnF = 0.0;
	final double toleranceDegrees = 1.0;
	
	final double UltrasonicP = 0.01;
	final double UltrasonicI = 0.0;
	final double UltrasonicD = 0.0;
	final double UltrasonicF = 0.0;
	final double UltrasonicTolerance = 2.0;
	
	// creates objects representing the Motor Controllers at the right ports
	public WPI_TalonSRX FLMotor;
	public WPI_TalonSRX FRMotor;
	public WPI_TalonSRX BLMotor;
	public WPI_TalonSRX BRMotor;
	
	//encoders
	//Encoder FLEncoder;
	//Encoder FREncoder;
	public Encoder BLEncoder;
	public Encoder BREncoder;
	
	//piston for moving 
	Solenoid Piston;
	
	//gyro sensors
	public AHRS navX = new AHRS(SPI.Port.kMXP);
	AnalogGyro gyro = new AnalogGyro(RobotMap.AnalogGyroPort);
	
	//PID Controllers
	PIDController turnController;
	PIDController distanceController;
	PIDController UltrasonicDrivingController;
	
	//Navigators
	public AutonomousTurningController turnNavigator;
	public AutonomousDrivingController driveNavigator;
	
	
	/**
	 * creates a new MecanumDriveTrainObject. This constructor also calls <code>initialize</code>, so it does not need to be called again.
	 */
	public MecanumDriveTrainCAN() {
		super();
		//set ups the motor controllers
		FLMotor = new WPI_TalonSRX(RobotMap.FLMotorPort);
		FRMotor = new WPI_TalonSRX(RobotMap.FRMotorPort);
		BLMotor = new WPI_TalonSRX(RobotMap.BLMotorPort);
		BRMotor = new WPI_TalonSRX(RobotMap.BRMotorPort);
		// Some of the motors need to be told the other direction is forward
		BRMotor.setInverted(false);
		FLMotor.setInverted(false);
		BLMotor.setInverted(false);
		FRMotor.setInverted(false);
		// set up base class
		initialize(FLMotor,FRMotor,BLMotor,BRMotor);
		//set up the encoders attached to the RoboRIO
		//FLEncoder = new Encoder(RobotMap.FLMotorEncoderPort1,RobotMap.FLMotorEncoderPort2);
		//FREncoder = new Encoder(RobotMap.FRMotorEncoderPort1,RobotMap.FRMotorEncoderPort2);
		BLEncoder = new Encoder(RobotMap.BLMotorEncoderPort1,RobotMap.BLMotorEncoderPort2, false, EncodingType.k4X);
		BREncoder = new Encoder(RobotMap.BRMotorEncoderPort1,RobotMap.BRMotorEncoderPort2);
		//FLEncoder.setDistancePerPulse(distancePerPulse);
		//FREncoder.setDistancePerPulse(distancePerPulse);
		BLEncoder.setDistancePerPulse(distancePerPulse);
		BREncoder.setDistancePerPulse(distancePerPulse);
		BLEncoder.setMaxPeriod(.1);
		BREncoder.setMaxPeriod(.1);
		BLEncoder.setMinRate(10);
		BREncoder.setMinRate(10);
		BLEncoder.setSamplesToAverage(7);
		BREncoder.setSamplesToAverage(7);
		
		turnNavigator = new AutonomousTurningController();
		driveNavigator = new AutonomousDrivingController(turnNavigator);
		
		//set up solenoid
		Piston = new Solenoid(RobotMap.DriveTrainPistonPort);

		//configure encoders attached to Talons
		FLMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		FLMotor.setSensorPhase(true);
		FRMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		FRMotor.setSensorPhase(true);
	}
	
	public void initPIDControllers() {
		turnController = new PIDController(turnP, turnI, turnD, turnF, navX, turnNavigator);
		turnController.setInputRange(-180.0F, 180.0F);
		turnController.setOutputRange(-1.0, 1.0);
		turnController.setAbsoluteTolerance(toleranceDegrees);
		turnController.setContinuous(true);
		turnController.setName("Drive System", "Turn Controller");
		LiveWindow.add(turnController);
		distanceController = new PIDController(driveP, driveI, driveD, driveF, driveNavigator, driveNavigator);
		distanceController.setContinuous(false);
	    distanceController.setOutputRange(-1.0, 1.0);
	    distanceController.setAbsoluteTolerance(driveToleranceInches);
	    
	}
	
	/**
	 * puts data from the encoder on the SmartDashboard and prints it to the console
	 */
	public void printEncoderPulses()
	{
		//SmartDashboard.putNumber("BL Sensor Position", FLMotor.getSensorCollection().getQuadraturePosition());
		//SmartDashboard.putNumber("FR Sensor Position", FRMotor.getSensorCollection().getQuadraturePosition());
		//SmartDashboard.putNumber("BL Motor Inches", FLMotor.getSelectedSensorPosition(0)/TICKS_PER_REVOLUTION * WHEEL_CIRCUMFERENCE);
		//SmartDashboard.putNumber("FR Motor Inches", FRMotor.getSelectedSensorPosition(0)/TICKS_PER_REVOLUTION * WHEEL_CIRCUMFERENCE);
		//SmartDashboard.putNumber("BL Sensor Velocity", FLMotor.getSensorCollection().getQuadratureVelocity());
		SmartDashboard.putNumber("BL Encoder Position", BLEncoder.getDistance());
		SmartDashboard.putNumber("BL Raw Encoder Position", BLEncoder.getRaw());
		SmartDashboard.putNumber("BL Encoder Speed", BLEncoder.getRate());
		SmartDashboard.putNumber("BL Percent Motor Output", BLMotor.getMotorOutputPercent());
	}
	/**
	 * puts data from the analog gyro to the SmartDashboard
	 */
	public void printAnalogGyroOutput()
	{
		SmartDashboard.putNumber("Analog Gyro", gyro.getAngle());
	}
	
	/**
	 * puts the yaw angle from the NavX MXP to the SmartDashboard
	 */
	public void printNavXYawOutput()
	{
		SmartDashboard.putNumber("NavX Yaw", navX.getAngle());
		SmartDashboard.putNumber("NavX Yaw PIDget", navX.pidGet());
	}
	
	public void printNavXDistance()
	{
		SmartDashboard.putNumber("Distance X", navX.getDisplacementX());
		SmartDashboard.putNumber("Distance Y", navX.getDisplacementY());
		SmartDashboard.putNumber("Distance Z", navX.getDisplacementZ());
	}
	
	/**
	 * Resets the distance the encoders say they have traveled
	 * Currently only works for encoders attached to Talons
	 */
	public void resetEncoderDistance()
	{
		BLEncoder.reset();
		BREncoder.reset();
		FLMotor.setSelectedSensorPosition(0,0,0);
    	FRMotor.setSelectedSensorPosition(0,0,0);
	}

	/**
	 * Resets the NavX's Yaw angle to zero
	 */
	public void zeroGyro() {
		navX.zeroYaw();
	}
	
	public void resetNavXDistance() {
		navX.resetDisplacement();
	}
	
	public void enableTurnController(double setpoint) {
		turnController.setSetpoint(setpoint);
		turnController.enable();
	}
	
	public void disableTurnController() {
		turnController.disable();
	}
	
	public boolean turnControllerOnTarget() {
		return turnController.onTarget();
	}
	
	public void setTurnControllerMode(AutonomousTurningController.TurnMode mode) {
		turnNavigator.setMode(mode);
	}
	
	public void enableMoveController(double setpoint) {
		distanceController.setSetpoint(setpoint);
		distanceController.enable();
	}
	
	public void disableMoveController() {
		distanceController.disable();
	}
	
	public boolean moveControllerOnTarget() {
		return distanceController.onTarget();
	}
	
	
	/**
	 * lowers the traction wheels on the drive train
	 */
	public void lowerTractionWheels()
	{
		Piston.set(true);
	}
	
	/**
	 * raises the traction wheels on the drive train
	 */
	public void raiseTractionWheels()
	{
		Piston.set(false);
	}
	
	/**
	 * toggles whether the traction wheels are up of down
	 */
	public void toggleTractionWheels()
	{
		Piston.set(!Piston.get());
	}
	
	@Override
	public void pidWrite(double output) {
		driveCartesian(0.0,output,0.0);
	}

}