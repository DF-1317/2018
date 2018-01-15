package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Wait extends Command {
	
	double TimeToWait;
	Timer AutoTimer = new Timer();
	
	Wait(double time)
	{
		super("Wait");
		TimeToWait = time;
	}
	
	@Override
	protected void initialize()
	{
		AutoTimer.reset();
		AutoTimer.start();
	}

	@Override
	protected boolean isFinished() {
		if(AutoTimer.get()>TimeToWait)
			return true;
		return false;
	}

}
