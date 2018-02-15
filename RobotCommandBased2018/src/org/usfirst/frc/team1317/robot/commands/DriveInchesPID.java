package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.navigation.*;
import org.usfirst.frc.team1317.robot.navigation.AutonomousTurningController.TurnMode;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;


/**
 * This command causes the robot to drive a certain amount of inches over a certain distance at a certain heading.
 * 
 * This class should allow the robot to travel in arcs if the turning speed is low and the robot starts at a different angle then the heading angle.
 * 
 */
public class DriveInchesPID extends Command {
	
	Encoder LeftEncoder;
	Encoder RightEncoder;
	
	double distanceSetpoint;
	double headingSetpoint;
	double drivingSpeed;
	double turningSpeed;
	
	/**
	 * constructs this command
	 * 
	 * @param inches - the distance the robot is to drive in inches
	 * @param heading - the heading the robot should be at by the end of the command
	 * @param drivingSpeed - the speed the robot should drive at
	 * @param turningSpeed - the speed the robot should turn at
	 */
    public DriveInchesPID(double inches, double heading, double drivingSpeed, double turningSpeed) {
    	super("DriveInchesPID");
        requires(Robot.mecanumDriveTrain);
        LeftEncoder = Robot.mecanumDriveTrain.BLEncoder;
        RightEncoder = Robot.mecanumDriveTrain.BREncoder;
        distanceSetpoint = inches;
        headingSetpoint = turningSpeed;
        this.drivingSpeed = drivingSpeed;
        this.turningSpeed = turningSpeed;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	LeftEncoder.reset();
    	RightEncoder.reset();
    	Robot.mecanumDriveTrain.setTurnControllerMode(TurnMode.withDriving);
    	Robot.mecanumDriveTrain.driveNavigator.setForwardSpeed(drivingSpeed);
    	Robot.mecanumDriveTrain.driveNavigator.setTurningSpeed(turningSpeed);
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	Robot.mecanumDriveTrain.enableTurnController(headingSetpoint);
    	Robot.mecanumDriveTrain.enableMoveController(distanceSetpoint);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return (Robot.mecanumDriveTrain.turnControllerOnTarget()&&Robot.mecanumDriveTrain.moveControllerOnTarget());
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	Robot.mecanumDriveTrain.disableTurnController();
    	Robot.mecanumDriveTrain.disableMoveController();
    	Robot.mecanumDriveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	Robot.mecanumDriveTrain.disableTurnController();
    	Robot.mecanumDriveTrain.disableMoveController();
    	Robot.mecanumDriveTrain.stop();
    }
}
