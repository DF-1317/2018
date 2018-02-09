package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveInchesPID extends Command implements PIDSource, PIDOutput {

	//distance constants
	public static final double kP = 0.01;
	public static final double kI = 0.0;
	public static final double kD = 0.0;
	public static final double kF = 0.0;
	public static final double inchTolerance = 1.0;
	
	//steering controller constants
	public static final double sP = 0.01;
	public static final double sI = 0.0;
	public static final double sD = 0.0;
	public static final double sF = 0.0;
	
	PIDController distanceController;
	PIDController steeringController;
	
	Encoder LeftEncoder;
	Encoder RightEncoder;
	
	double setPoint;
	
	
    public DriveInchesPID(double inches) {
        requires(Robot.mecanumDriveTrain);
        distanceController = new PIDController(kP,kI,kD,kF,this,this);
        distanceController.setOutputRange(-1.0, 1.0);
        distanceController.setAbsoluteTolerance(inchTolerance);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	LeftEncoder.reset();
    	RightEncoder.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	distanceController.setSetpoint(setPoint);
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

	@Override
	public void pidWrite(double output) {
		
		
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double pidGet() {
		return (LeftEncoder.getDistance() + RightEncoder.getDistance())/2;
	}
}
