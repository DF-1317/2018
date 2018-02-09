package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 *
 */
public class PositionElevatorPID extends Command {

	// Some things that do things
	static final double kP = 0.01;
	static final double kI = 0.00;
	static final double kD = 0.00;
	static final double kF = 0.00;
	static final double kToleranceInches = 1.0;
	
	// Objects representing other things
	PIDController moveController;
	Encoder encoder;
	WPI_TalonSRX motor;
	
	// Variable representing the target position of the elevator
	double position;
	
	/**
	 * The constructor for the command
	 * @param position - target position for the elevator.
	 */
    public PositionElevatorPID(double position) {
    	
    	super("PositionElevatorPID");
    	requires(Robot.el);
    	motor = Robot.el.ElevatorMotor;
    	encoder = Robot.el.ElevatorEncoder;
    	moveController = new PIDController(kP, kI, kD, kF, encoder, motor);
    	moveController.setOutputRange(-1.0, 1.0);
    	moveController.setAbsoluteTolerance(kToleranceInches);
    	moveController.setName("Elevator", "Height Controller");
    	LiveWindow.add(moveController);
    	this.position = position;
    	setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	moveController.setSetpoint(position);
    	moveController.enable();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return moveController.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	moveController.disable();
    	motor.stopMotor();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	moveController.disable();
    	motor.stopMotor();
    }
}
