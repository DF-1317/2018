package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 * this class represents the specific drive train used on Digital Fusion's 2018 robot.
 * The robot uses TalonSRX motor controllers over CAN.
 * 
 * @see MecanumDriveTrain
 *
 */
public class MecanumDriveTrainCAN extends MecanumDriveTrain  {
	
	//constants representing encoder and wheel properties.
	public static final int TICKS_PER_REVOLUTION = 4096;
	public static final double WHEEL_CIRCUMFERENCE = 6*Math.PI;
	final double distancePerPulse = WHEEL_CIRCUMFERENCE / TICKS_PER_REVOLUTION;
	
	
	
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
	public AHRS navX = new AHRS(Port.kMXP);
	AnalogGyro gyro = new AnalogGyro(RobotMap.AnalogGyroPort);
	
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
		//BREncoder.setDistancePerPulse(distancePerPulse);
		BLEncoder.setMaxPeriod(.1);
		BLEncoder.setMinRate(10);
		BLEncoder.setSamplesToAverage(7);
		
		//set up solenoid
		Piston = new Solenoid(RobotMap.DriveTrainPistonPort);

		//configure encoders attached to Talons
		FLMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		FLMotor.setSensorPhase(true);
		FRMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		FRMotor.setSensorPhase(true);
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

}
