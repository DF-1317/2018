package org.usfirst.frc.team1317.robot.subsystems;

import org.usfirst.frc.team1317.robot.RobotMap;
import org.usfirst.frc.team1317.robot.commands.JoystickElevatorCommand;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * 
 */
public class Elevator extends PIDSubsystem {
	
	public static final double kP = 0.01;
	public static final double kI = 0.0;
	public static final double kD = 0.0;
	public static final double kF = 0.0;
	public static final double kTolerance = 10;
	
	// create objects representing the motor controlling the elevator
	public WPI_TalonSRX ElevatorMotor;
	
	// encoders
	public Encoder ElevatorEncoder;
	
	public Elevator() {
		super("Elevator Controller", kP, kI, kD, kF);
		// set up elevator motor
		ElevatorMotor = new WPI_TalonSRX(RobotMap.ElevatorPort);
		ElevatorEncoder = new Encoder(RobotMap.ElevatorMotorEncoderPortA, RobotMap.ElevatorMotorEncoderPortB);
		// Just in case
		ElevatorMotor.setInverted(false);
		ElevatorMotor.configPeakCurrentLimit(50, 10);
		ElevatorMotor.enableCurrentLimit(false);
		setAbsoluteTolerance(kTolerance);
		setOutputRange(-1.0,1.0);
	}
	
	/**
	 * prints the pulses from the encoder to the SmartDashoard
	 */
	public void PrintEncoderPulses() {
		SmartDashboard.putNumber("Elevator Position", ElevatorEncoder.get());
	}
	
	/**
	 * moves the elevator motor
	 * 
	 * @param speed - the speed the motor on the elevator moves as a ratio of maximum motor power. Range from -1.0 to 1.0
	 */
	public void move(double speed) {
		if(ElevatorMotor.getOutputCurrent()<38.0) {
			ElevatorMotor.set(speed);
		}
	}
	
	/**
	 * gets the position of the elevator motor
	 * @return the pulses from the encoder on the motor
	 */
	public double getPosition() {
		return ElevatorEncoder.get();
	}

	@Override
    public void initDefaultCommand() {
    	setDefaultCommand(new JoystickElevatorCommand());
    }

	@Override
	protected double returnPIDInput() {
		return ElevatorEncoder.getRaw();
	}

	@Override
	protected void usePIDOutput(double output) {
		move(output);
	}
	
	@Override
	public void periodic() {
		SmartDashboard.putNumber("Elevator Current", ElevatorMotor.getOutputCurrent());
	}
}

