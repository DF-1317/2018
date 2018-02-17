package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class ClimbAlert extends Command {
	
	boolean displayAlert = false;
	final int TIMES = 15;
	final int DELAY = 100;
	int i;
	
    public ClimbAlert() {
    	SmartDashboard.putBoolean("Endgame", false);
    	setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	i = 0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	++i;
    	if((i * 20) % DELAY == 0) {
	    	displayAlert = !displayAlert;
	    	SmartDashboard.putBoolean("Endgame", displayAlert);
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	if(i * 20 > TIMES * DELAY) {
    		return true;
    	}
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	SmartDashboard.putBoolean("Endgame", true);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	SmartDashboard.putBoolean("Endgame", true);
    }
}
