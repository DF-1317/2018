package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.subsystems.MecanumDriveTrainCAN;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveInchesAccelerate extends Command {

	double acceleration;
	double distance;
	double maxSpeed;
	
	double startingDistance;
	
	double accelDistance;
	double currentSpeed = 0;
	
	int phase = 1;
	boolean finished = false;
	
	MecanumDriveTrainCAN driveTrain = Robot.mecanumDriveTrain;
	
    public DriveInchesAccelerate(double acceleration, double distance, double maxSpeed) {
        this.acceleration = acceleration / 50;
        this.distance = distance;
        this.maxSpeed = maxSpeed;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	startingDistance = driveTrain.getDrivingController().pidGet();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(phase == 1) {
	    	if(currentSpeed + acceleration <= maxSpeed) {
	    		currentSpeed += acceleration;
	    	} else {
	    		currentSpeed = maxSpeed;
	    		phase = 2;
	    		accelDistance = driveTrain.getDrivingController().pidGet() - startingDistance;
	    	}
	    	if(driveTrain.getDrivingController().pidGet() * 2 >= distance) {
	    		phase = 3;
	    	}
    	} else if(phase == 2) {
    		if(driveTrain.getDrivingController().pidGet() + accelDistance >= distance) {
    			phase = 3;
    		}
    	} else if(phase == 3) {
    		if(currentSpeed - acceleration >= 0) {
    			currentSpeed -= acceleration;
    		} else {
    			currentSpeed = 0;
    			phase = 4;
    		}
    	} else {
    		finished = true;
    	}
    	driveTrain.driveCartesian(0.0, currentSpeed, 0.0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	driveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	driveTrain.stop();
    }
}
