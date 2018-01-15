package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class MecanumDriveTrainCAN extends MecanumDriveTrain {
	// creates objects representing the Motor Controllers at the right ports
	WPI_TalonSRX FLMotor;
	WPI_TalonSRX FRMotor;
	WPI_TalonSRX BLMotor;
	WPI_TalonSRX BRMotor;

	public MecanumDriveTrainCAN() {
		FLMotor = new WPI_TalonSRX(RobotMap.FLMotorPort);
		FRMotor = new WPI_TalonSRX(RobotMap.FRMotorPort);
		BLMotor = new WPI_TalonSRX(RobotMap.BLMotorPort);
		BRMotor = new WPI_TalonSRX(RobotMap.FRMotorPort);
		// Some of the motors need to be told the other direction is forward
		FRMotor.setInverted(true);
		BRMotor.setInverted(true);
		// set up base class
		Initialize(FLMotor,FRMotor,BLMotor,BRMotor);
	}
}
