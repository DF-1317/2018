package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class StopMotors extends Command {

	StopMotors()
	{
		super("StopMotors");
		requires(Robot.mecanumDriveTrain);
	}
	
	@Override
	protected void execute() {
		Robot.mecanumDriveTrain.stop();
	}
	
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return true;
	}

}
