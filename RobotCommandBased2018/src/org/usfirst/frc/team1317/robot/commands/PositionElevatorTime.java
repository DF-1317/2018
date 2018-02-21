package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class PositionElevatorTime extends Command {
	
	Timer timer = new Timer();
	Elevator el;
	double speed;
	double time;
	double batteryVoltage;
	
    public PositionElevatorTime(double time, double speed) {
		super("PositionElevatorTime");
        requires(Robot.el);
        el = Robot.el;
        this.time = time;
        this.speed = speed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.reset();
    	timer.start();
    	batteryVoltage = RobotController.getBatteryVoltage();
    	speed *= (12.0/batteryVoltage);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	el.move(speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (timer.get()>=time);
    }

    // Called once after isFinished returns true
    protected void end() {
    	timer.stop();
    	el.move(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	timer.stop();
    	el.move(0.0);
    }
}
