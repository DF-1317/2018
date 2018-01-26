package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.RobotMap;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.*;
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
	}
	
	public void PrintEncoderPulses()
	{
		System.out.println("FL: " + FLEncoder.get());
		SmartDashboard.putNumber("FL Sensor Position", FLMotor.getSensorCollection().getQuadraturePosition());
		SmartDashboard.putNumber("FL Sensor Velocity", FLMotor.getSensorCollection().getQuadratureVelocity());
		System.out.println("FR: " + FREncoder.get());
		System.out.println("BL: " + BLEncoder.get());
		System.out.println("BR: " + BREncoder.get());
	}
	
	public void LowerTractionWheels()
	{
		Piston.set(true);
	}
	public void RaiseTractionWheels()
	{
		Piston.set(false);
	}
	
	public void ToggleTractionWheels()
	{
		Piston.set(!Piston.get());
	}

}
