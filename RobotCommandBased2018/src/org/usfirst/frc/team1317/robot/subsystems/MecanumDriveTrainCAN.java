package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.PIDTurning;
import org.usfirst.frc.team1317.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MecanumDriveTrainCAN extends MecanumDriveTrain {
	// creates objects representing the Motor Controllers at the right ports
	public WPI_TalonSRX FLMotor;
	public WPI_TalonSRX FRMotor;
	public WPI_TalonSRX BLMotor;
	public WPI_TalonSRX BRMotor;
	
	//encoders
	Encoder FLEncoder;
	Encoder FREncoder;
	Encoder BLEncoder;
	Encoder BREncoder;
	
	Solenoid Piston;
	
	public PIDTurning turning;
	
	AHRS navX = new AHRS(Port.kMXP);
	AnalogGyro gyro = new AnalogGyro(RobotMap.AnalogGyroPort);
	
	final double distancePerPulse = 2;
	
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
		FLEncoder = new Encoder(RobotMap.FLMotorEncoderPort1,RobotMap.FLMotorEncoderPort2);
		FREncoder = new Encoder(RobotMap.FRMotorEncoderPort1,RobotMap.FRMotorEncoderPort2);
		BLEncoder = new Encoder(RobotMap.BLMotorEncoderPort1,RobotMap.BLMotorEncoderPort2);
		BREncoder = new Encoder(RobotMap.BRMotorEncoderPort1,RobotMap.BRMotorEncoderPort2);
		FLEncoder.setDistancePerPulse(distancePerPulse);
		FREncoder.setDistancePerPulse(distancePerPulse);
		BLEncoder.setDistancePerPulse(distancePerPulse);
		BREncoder.setDistancePerPulse(distancePerPulse);
		
		Piston = new Solenoid(RobotMap.DriveTrainPistonPort);
		
		turning = new PIDTurning(this, navX);

		FLMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		FLMotor.setSensorPhase(true);
		FRMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		FRMotor.setSensorPhase(true);
	}
	
	public void printEncoderPulses()
	{
		System.out.println("FL: " + FLEncoder.get());
		SmartDashboard.putNumber("FL Sensor Position", FLMotor.getSensorCollection().getQuadraturePosition());
		SmartDashboard.putNumber("FR Sensor Velocity", FRMotor.getSensorCollection().getQuadratureVelocity());
		System.out.println("FR: " + FREncoder.get());
		System.out.println("BL: " + BLEncoder.get());
		System.out.println("BR: " + BREncoder.get());
	}
	
	public void printAnalogGyroOutput()
	{
		SmartDashboard.putNumber("Analog Gyro", gyro.getAngle());
	}
	
	public void printNavXYawOutput()
	{
		SmartDashboard.putNumber("NavX Yaw", navX.getAngle());
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
