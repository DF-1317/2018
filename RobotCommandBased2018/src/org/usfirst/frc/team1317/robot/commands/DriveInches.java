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
	double rotations;
	double targetPos;
	int speed;
	WPI_TalonSRX FLMotor;
	WPI_TalonSRX FRMotor;
	
    public DriveInches(double inches, int speed) {
        requires(Robot.mecanumDriveTrain);
        rotations = inches / WHEEL_CIRCUMFERENCE;
        targetPos = TICKS_PER_REVOLUTION * rotations;
        this.speed = speed; //speed is currently in sensor units/100 milliseconds
        //may possibly want to scale speed to other units
        FLMotor = Robot.mecanumDriveTrain.FLMotor;
        FRMotor = Robot.mecanumDriveTrain.FRMotor;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.mecanumDriveTrain.BRMotor.follow(FRMotor);
    	Robot.mecanumDriveTrain.BLMotor.follow(FLMotor);
    	FLMotor.configMotionCruiseVelocity(speed,0);
    	FRMotor.configMotionCruiseVelocity(speed,0);
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
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
