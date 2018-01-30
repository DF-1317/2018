package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team1317.robot.*;

public class TurnDegrees extends Command {

	PIDTurning turner;
	double degrees = 0;
	double speed = 1.0;
	boolean done = false;
	public TurnDegrees(double degrees, double speed)
	{
		super("TurnDegrees");
		requires(Robot.mecanumDriveTrain);
		turner = Robot.mecanumDriveTrain.turning;
		this.degrees = degrees;
		this.speed = speed;
		setInterruptible(true);
	}
	@Override
	protected void initialize()
	{
		done = false;
		turner.reset();
	}
	
	@Override
	protected void execute()
	{
		done = turner.turnDegrees(degrees, speed);
	}
	
	@Override
	protected boolean isFinished() {
		return done;
	}
	
	@Override
	protected void end()
	{
		turner.stop();
	}

}
