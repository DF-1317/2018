package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Logger;
import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.subsystems.MecanumDriveTrainCAN;

import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveInchesAccelerate extends Command {

	double acceleration;
	double distance;
	double maxSpeed;
	double halfway;
	
	double startingDistance;
	double multiplier;
	
	double accelDistance;
	double currentSpeed = 0;
	
	int phase = 1;
	boolean finished = false;
	
	Logger syslog = new Logger("1317", "DriveInchesAccelerate");
	
	MecanumDriveTrainCAN driveTrain = Robot.mecanumDriveTrain;
	
    public DriveInchesAccelerate(double acceleration, double distance, double maxSpeed, boolean inReverse) {
        this.acceleration = acceleration / 50;
        this.distance = distance;
        this.halfway = distance / 2;
        this.maxSpeed = maxSpeed;
        if(inReverse) {
        	multiplier = -1;
        } else {
        	multiplier = 1;
        }
    }
    
    public DriveInchesAccelerate(double acceleration, double distance, double maxSpeed) {
    	this(acceleration, distance, maxSpeed, false);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	driveTrain.driveNavigator.setPIDSourceType(PIDSourceType.kDisplacement);
    	startingDistance = driveTrain.getDrivingController().pidGet();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void executeOld() {
    	if(phase == 1) {
	    	if(currentSpeed + acceleration <= maxSpeed) {
	    		currentSpeed += acceleration;
	    	} else {
	    		currentSpeed = maxSpeed;
	    		phase = 2;
	    		accelDistance = driveTrain.getDrivingController().pidGet() - startingDistance;
	    		syslog.log("Entering Phase 2; accelDistance: " + accelDistance);
	    		
	    	}
	    	if((driveTrain.getDrivingController().pidGet() - startingDistance) * 2.0 >= distance) {
	    		phase = 3;
	    		syslog.log("Entering Phase 3 early; current distance: " + (driveTrain.getDrivingController().pidGet() - startingDistance)); 
	    	}
    	} else if(phase == 2) {
    		if((driveTrain.getDrivingController().pidGet() - startingDistance) + accelDistance >= distance) {
    			phase = 3;
    			syslog.log("Entering Phase 3; current distance: " + (driveTrain.getDrivingController().pidGet() - startingDistance));
    		}
    	} else if(phase == 3) {
    		if(currentSpeed - acceleration >= 0.0) {
    			currentSpeed -= acceleration;
    		} else {
    			currentSpeed = 0;
    			phase = 4;
    			syslog.log("Entering Phase 4; current distance: " + (driveTrain.getDrivingController().pidGet() - startingDistance));
    		}
    	} else {
    		finished = true;
    	}
		System.out.println("Distance: " + (driveTrain.getDrivingController().pidGet() - startingDistance));
    	driveTrain.driveCartesian(0.0, currentSpeed, 0.0);
    	//System.out.println("currentSpeed " + currentSpeed);
    	//System.out.println("remaining distance " + (distance - (driveTrain.getDrivingController().pidGet() - startingDistance)));
    }

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
    	double distanceNow = driveTrain.getDrivingController().pidGet() * multiplier;
		if(phase == 1) {
			currentSpeed += acceleration;
			if(currentSpeed >= maxSpeed) {
				currentSpeed = maxSpeed;
				phase = 2;
				accelDistance = distanceNow - startingDistance;
				syslog.log("Entering Phase 2; accelDistance: " + accelDistance);
			}
			if((distanceNow - startingDistance) >= halfway) {
				phase = 3;
				syslog.log("Entering Phase 3 early; current distance: " + (distanceNow - startingDistance)); 
			}
		} else if(phase == 2) {
			if((distanceNow - startingDistance) + accelDistance >= distance) {
				phase = 3;
				syslog.log("Entering Phase 3; current distance: " + (distanceNow - startingDistance));
			}
		} else if(phase == 3) {
			currentSpeed = 0.2;
			if(distanceNow <= distance) {
				currentSpeed = 0;
				phase = 4;
				syslog.log("Entering Phase 4; current distance: " + (distanceNow - startingDistance));
			}
		} else {
			driveTrain.stop();
			finished = true;
			syslog.log("Finished); current distance: " + (distanceNow - startingDistance));
		}
		driveTrain.driveCartesian(0.0, currentSpeed * multiplier, 0.0);
		System.out.println("currentSpeed " + currentSpeed);
		System.out.println("remaining distance " + (distance - (distanceNow - startingDistance)));
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
