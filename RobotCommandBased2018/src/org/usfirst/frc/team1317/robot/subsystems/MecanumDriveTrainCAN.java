package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.PIDTurning;
import org.usfirst.frc.team1317.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MecanumDriveTrainCAN extends MecanumDriveTrain  {
	
	
	public static final int TICKS_PER_REVOLUTION = 4096;
	public static final double WHEEL_CIRCUMFERENCE = 6*Math.PI;
	
	
	// creates objects representing the Motor Controllers at the right ports
	public WPI_TalonSRX FLMotor;
	public WPI_TalonSRX FRMotor;
	public WPI_TalonSRX BLMotor;
	public WPI_TalonSRX BRMotor;
	
	//encoders
	//Encoder FLEncoder;
	//Encoder FREncoder;
	Encoder BLEncoder;
	//Encoder BREncoder;
	DigitalInput EncoderChannelA;
	DigitalInput EncoderChannelB;
	
	Solenoid Piston;
	
	public PIDTurning turning;
	
	AHRS navX = new AHRS(Port.kMXP);
	AnalogGyro gyro = new AnalogGyro(RobotMap.AnalogGyroPort);
	
	final double distancePerPulse = WHEEL_CIRCUMFERENCE / TICKS_PER_REVOLUTION;
	
	public MecanumDriveTrainCAN() {
		super();
		//set ups the motor controllers
		FLMotor = new WPI_TalonSRX(RobotMap.FLMotorPort);
		FRMotor = new WPI_TalonSRX(RobotMap.FRMotorPort);
		BLMotor = new WPI_TalonSRX(RobotMap.BLMotorPort);
		BRMotor = new WPI_TalonSRX(RobotMap.BRMotorPort);
		// Some of the motors need to be told the other direction is forward
		BRMotor.setInverted(true);
		FLMotor.setInverted(false);
		BLMotor.setInverted(false);
		FRMotor.setInverted(true);
		// set up base class
		Initialize(FLMotor,FRMotor,BLMotor,BRMotor);
		//set up the encoders
		//FLEncoder = new Encoder(RobotMap.FLMotorEncoderPort1,RobotMap.FLMotorEncoderPort2);
		//FREncoder = new Encoder(RobotMap.FRMotorEncoderPort1,RobotMap.FRMotorEncoderPort2);
		BLEncoder = new Encoder(RobotMap.BLMotorEncoderPort1,RobotMap.BLMotorEncoderPort2, false, EncodingType.k4X);
		//BREncoder = new Encoder(RobotMap.BRMotorEncoderPort1,RobotMap.BRMotorEncoderPort2);
		EncoderChannelA = new DigitalInput(0);
		EncoderChannelB = new DigitalInput(1);
		//FLEncoder.setDistancePerPulse(distancePerPulse);
		//FREncoder.setDistancePerPulse(distancePerPulse);
		BLEncoder.setDistancePerPulse(distancePerPulse);
		//BREncoder.setDistancePerPulse(distancePerPulse);
		BLEncoder.setMaxPeriod(.1);
		BLEncoder.setMinRate(10);
		BLEncoder.setSamplesToAverage(7);
		
		Piston = new Solenoid(RobotMap.DriveTrainPistonPort);
		
		turning = new PIDTurning(this, navX);

		FLMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		FLMotor.setSensorPhase(true);
		FRMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		FRMotor.setSensorPhase(true);
	}
	
	public void printEncoderPulses()
	{
		//System.out.println("FL: " + FLEncoder.get());
		//SmartDashboard.putNumber("BL Sensor Position", FLMotor.getSensorCollection().getQuadraturePosition());
		//SmartDashboard.putNumber("FR Sensor Position", FRMotor.getSensorCollection().getQuadraturePosition());
		//SmartDashboard.putNumber("BL Motor Inches", FLMotor.getSelectedSensorPosition(0)/TICKS_PER_REVOLUTION * WHEEL_CIRCUMFERENCE);
		//SmartDashboard.putNumber("FR Motor Inches", FRMotor.getSelectedSensorPosition(0)/TICKS_PER_REVOLUTION * WHEEL_CIRCUMFERENCE);
		//SmartDashboard.putNumber("BL Sensor Velocity", FLMotor.getSensorCollection().getQuadratureVelocity());
		SmartDashboard.putNumber("BL Encoder Position", BLEncoder.getDistance());
		SmartDashboard.putNumber("BL Raw Encoder Position", BLEncoder.getRaw());
		SmartDashboard.putNumber("BL Encoder Speed", BLEncoder.getRate());
		SmartDashboard.putNumber("BL Percent Motor Output", BLMotor.getMotorOutputPercent());
		//System.out.println("FR: " + FREncoder.get());
		System.out.println("BL: " + BLEncoder.get());
		//System.out.println("BR: " + BREncoder.get());
	}
	
	public void printAnalogGyroOutput()
	{
		SmartDashboard.putNumber("Analog Gyro", gyro.getAngle());
	}
	
	public void printNavXYawOutput()
	{
		SmartDashboard.putNumber("NavX Yaw", navX.getAngle());
	}
	
	public void resetEncoderDistance()
	{
		FLMotor.setSelectedSensorPosition(0,0,0);
    	FRMotor.setSelectedSensorPosition(0,0,0);
	}

	public void zeroGyro() {
		navX.zeroYaw();
	}
	
	public void lowerTractionWheels()
	{
		Piston.set(true);
	}
	public void raiseTractionWheels()
	{
		Piston.set(false);
	}
	
	public void toggleTractionWheels()
	{
		Piston.set(!Piston.get());
	}

}
