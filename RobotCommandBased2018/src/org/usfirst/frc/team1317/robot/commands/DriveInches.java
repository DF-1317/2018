package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveInches extends Command {

	final double WHEEL_CIRCUMFERENCE = 19;
	final int ACCELERATION = 6000;
	final int TICKS_PER_REVOLUTION = 4096;
	final int ALLOWABLE_ERROR = 24; //allowable error in ticks
	double rotations;
	double targetPos;
	double speed;
	WPI_TalonSRX FLMotor;
	WPI_TalonSRX FRMotor;
	
    public DriveInches(double inches, double speed) {
    	super("DriveInches");
        requires(Robot.mecanumDriveTrain);
        rotations = inches / WHEEL_CIRCUMFERENCE;
        targetPos = TICKS_PER_REVOLUTION * rotations;
        this.speed = speed/TICKS_PER_REVOLUTION * WHEEL_CIRCUMFERENCE * 1000; //speed is currently in sensor units/100 milliseconds
        //may possibly want to scale speed to other units
        FLMotor = Robot.mecanumDriveTrain.FLMotor;
        FRMotor = Robot.mecanumDriveTrain.FRMotor;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	Robot.mecanumDriveTrain.BRMotor.follow(FRMotor);
    	Robot.mecanumDriveTrain.BLMotor.follow(FLMotor);
    	int intSpeed = (int) Math.round(speed);
    	FLMotor.configMotionCruiseVelocity(intSpeed,0);
    	FRMotor.configMotionCruiseVelocity(intSpeed,0);
    	FLMotor.configMotionAcceleration(ACCELERATION,0);
    	FRMotor.configMotionAcceleration(ACCELERATION,0);
    	FLMotor.setSelectedSensorPosition(0,0,0);
    	FRMotor.setSelectedSensorPosition(0,0,0);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	FLMotor.set(ControlMode.MotionMagic, targetPos);
    	FRMotor.set(ControlMode.MotionMagic, targetPos);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(Math.abs((FLMotor.getSensorCollection().getQuadraturePosition()-targetPos))<=ALLOWABLE_ERROR)
    	{
    		if(Math.abs((FRMotor.getSensorCollection().getQuadraturePosition()-targetPos))<=ALLOWABLE_ERROR)
    			return true;
    	}
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	FLMotor.set(ControlMode.PercentOutput, 0.0);
    	FRMotor.set(ControlMode.PercentOutput, 0.0);
    	Robot.mecanumDriveTrain.BRMotor.set(ControlMode.PercentOutput, 0.0);
    	Robot.mecanumDriveTrain.BLMotor.set(ControlMode.PercentOutput, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
