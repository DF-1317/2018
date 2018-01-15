package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

import org.usfirst.frc.team1317.robot.Robot;

public class DriveForward extends Command {

	double TargetTime =0;
	double Speed = 0;
	Timer timer = new Timer();
	public DriveForward(double time, double speed)
	{
		super("DriveForward");
		requires(Robot.mecanumDriveTrain);
		TargetTime = time;
		Speed = speed;
		setInterruptible(true);
	}
	
	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		timer.reset();
		timer.start();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		Robot.mecanumDriveTrain.driveCartesian(Speed, 0.0, 0.0);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		if(timer.get()>=TargetTime)
			return true;
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		Robot.mecanumDriveTrain.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		Robot.mecanumDriveTrain.stop();
	}
}
