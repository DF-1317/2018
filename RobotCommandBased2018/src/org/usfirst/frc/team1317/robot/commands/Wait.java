package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This command waits a specific amount of time
 */

public class Wait extends Command {
	
	// Declare wait time and timer object
	double TimeToWait;
	double tNow;
	Timer AutoTimer = new Timer();
	
	/**
	 * @param time - Amount of time in seconds to wait.
	 */
	Wait(double time)
	{
		super("Wait");
		TimeToWait = time;
		tNow = 0.0;
	}
	
	/**
	 * Initialize command by resetting and starting timer
	 */
	@Override
	protected void initialize()
	{
		AutoTimer.reset();
		AutoTimer.start();
	}
	
	@Override
	protected void execute() {
		tNow = AutoTimer.get();
	}
	
	/**
	 * Method to check if wait is done.
	 */
	@Override
	protected boolean isFinished() {
		return tNow >= TimeToWait;
	}
}