package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Logger;
import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.navigation.AutonomousDrivingController;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class FindConstants extends Command {
	
	double[] constants;
	Logger syslog = new Logger("1317", FindConstants.class.getSimpleName());

	static double TOLERANCE = 3.0;
	AutonomousDrivingController drivingController = Robot.mecanumDriveTrain.getDrivingController();
	
    public FindConstants(double[] constants) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.mecanumDriveTrain);
    	this.constants = constants;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	for (double constant : constants) {
    		String answer = findOne(constant);
    		syslog.log(answer);
    		System.out.println(answer);
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
    
    private String findOne(double startDistance) {
    	
    	double driveDistance = startDistance;
    	double actualDistance = drivingController.pidGet();
    	Command driveCommand = new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, driveDistance, Robot.DEFAULT_MAX_SPEED);
    	Timer timer = new Timer();
    	boolean isDone = false;
    	
    	timer.reset();
    	
    	while(!isDone) {
    		if(!driveCommand.isRunning() && !driveCommand.isCompleted()) {
    			driveCommand.start();
    		} else if(driveCommand.isCompleted()) {
    			timer.delay(2);
    			actualDistance = drivingController.pidGet();
    			if ((actualDistance - TOLERANCE / 2) >= startDistance && (actualDistance + TOLERANCE / 2) <= startDistance) {
    				isDone = true;
    			} else if(actualDistance > startDistance) {
    				driveDistance *= 1.5;
    			} else {
    				driveDistance *= 0.5;
    			}
    			driveCommand = new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, driveDistance, Robot.DEFAULT_MAX_SPEED);
    		}
    	}
    	
    	return "Target Distance: " + startDistance + "; Command Distance: " + driveDistance;
    }
}
