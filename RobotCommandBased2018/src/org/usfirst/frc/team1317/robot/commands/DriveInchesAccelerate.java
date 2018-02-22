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

	final double turnProportion = 0.02;
	
	double acceleration;
	double distance;
	double maxSpeed;
	double thirdway;
	
	double startingDistance;
	double multiplier;
	double distanceNow;
	
	double accelDistance;
	double currentSpeed = 0;
	
	double targetAngle;
	
	int phase = 1;
	boolean finished = false;
	
	Logger syslog = new Logger("1317", "DriveInchesAccelerate");
	
	MecanumDriveTrainCAN driveTrain = Robot.mecanumDriveTrain;
	
	public static final double SLOW_SPEED = 0.2;
	
    public DriveInchesAccelerate(double acceleration, double distance, double maxSpeed, boolean inReverse) {
        this.acceleration = acceleration / 50;
        this.distance = distance;
        this.thirdway = distance / 3;
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
    	driveTrain.resetEncoderDistance();
    	driveTrain.driveNavigator.setPIDSourceType(PIDSourceType.kDisplacement);
    	startingDistance = driveTrain.getDrivingController().pidGet();
    	targetAngle = driveTrain.navX.getAngle();
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
	    		accelDistance *= 2;
	    		
	    	}
	    	if((driveTrain.getDrivingController().pidGet() - startingDistance) * 3.0 >= distance) {
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
    	distanceNow = driveTrain.getDrivingController().pidGet() * multiplier;
    	double angleNow = driveTrain.navX.getAngle();
		if(phase == 1) {
			currentSpeed += acceleration;
			if(currentSpeed >= maxSpeed) {
				currentSpeed = maxSpeed;
				phase = 2;
				accelDistance = distanceNow - startingDistance;
				syslog.log("Entering Phase 2; accelDistance: " + accelDistance);
				accelDistance *= 2;
			}
			if((distanceNow - startingDistance) >= thirdway) {
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
			if(currentSpeed >= SLOW_SPEED) {
				currentSpeed -= acceleration;
			} else {
				currentSpeed = SLOW_SPEED;
				phase = 4;
				syslog.log("Entering Phase 4); current distance: " + (distanceNow - startingDistance));
			}
		} else if(phase == 4) {
			if(distanceNow >= distance) {
				currentSpeed = 0;
				phase = 5;
				syslog.log("Entering Phase 5; current distance: " + (distanceNow - startingDistance));
			}
		} else {
			driveTrain.stop();
			finished = true;
			syslog.log("Finished; current distance: " + (distanceNow - startingDistance));
		}
		double angleError = angleNow - targetAngle;
		syslog.log("Angle Error: " + angleError);
		driveTrain.driveCartesian(0.0, currentSpeed * multiplier, angleError*turnProportion);
		System.out.println("currentSpeed " + currentSpeed);
		System.out.println("remaining distance " + (distance - (distanceNow - startingDistance)));
	}
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (distanceNow >= distance);
    }

    // Called once after isFinished returns true
    protected void end() {
    	driveTrain.stop();
    	syslog.log("Finished");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	driveTrain.stop();
    }
}
