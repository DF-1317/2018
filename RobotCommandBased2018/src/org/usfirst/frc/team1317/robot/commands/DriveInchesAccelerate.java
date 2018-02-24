package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Logger;
import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.RobotMap;
import org.usfirst.frc.team1317.robot.subsystems.MecanumDriveTrainCAN;

import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
	Logger periodicLog = new Logger("1317", "DriveInchesAccelerate", 5);
	
	MecanumDriveTrainCAN driveTrain = Robot.mecanumDriveTrain;
	
	public static final double SLOW_SPEED = 0.15;
	public static double offset;
	
    public DriveInchesAccelerate(double acceleration, double distance, double maxSpeed, double targetAngle, boolean inReverse) {
        offset = SmartDashboard.getNumber("Offset", 10.0);
        this.acceleration = acceleration / 50;
        this.distance = distance - offset;
        this.thirdway = distance / 3;
        this.maxSpeed = maxSpeed;
        this.targetAngle = targetAngle;
        if(inReverse) {
        	multiplier = -1;
        } else {
        	multiplier = 1;
        }
    }
    
    public DriveInchesAccelerate(double acceleration, double distance, double maxSpeed) {
    	this(acceleration, distance, maxSpeed, 1000.0, false);
    }
    
    public DriveInchesAccelerate(double acceleration, double distance, double maxSpeed, double targetAngle) {
    	this(acceleration, distance, maxSpeed, targetAngle, false);
    }
    
    public DriveInchesAccelerate(double acceleration, double distance, double maxSpeed, boolean inReverse) {
    	this(acceleration, distance, maxSpeed, 1000.0, inReverse);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	driveTrain.resetEncoderDistance();
    	driveTrain.driveNavigator.setPIDSourceType(PIDSourceType.kDisplacement);
    	startingDistance = driveTrain.getDrivingController().pidGet();
    	if(targetAngle == 1000.0) {
    		targetAngle = driveTrain.navX.getAngle();
    	}
    	syslog.log("Target Distance: " + distance);
    }

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
    	distanceNow = (driveTrain.getDrivingController().pidGet() - startingDistance) * multiplier;
    	double angleNow = driveTrain.navX.getAngle();
		if(phase == 1) {
			currentSpeed += acceleration;
			if(currentSpeed >= maxSpeed) {
				currentSpeed = maxSpeed;
				phase = 2;
				accelDistance = distanceNow;
				syslog.log("Entering Phase 2; accelDistance: " + accelDistance);
				accelDistance *= 4;
			}
			if((distanceNow) >= thirdway) {
				phase = 3;
				syslog.log("Entering Phase 3 early; current distance: " + distanceNow); 
			}
		} else if(phase == 2) {
			if((distanceNow + accelDistance) >= distance) {
				phase = 3;
				syslog.log("Entering Phase 3; current distance: " + distanceNow);
			}
		} else if(phase == 3) {
			if(currentSpeed >= SLOW_SPEED) {
				currentSpeed -= acceleration * 2;
			} else {
				currentSpeed = SLOW_SPEED;
				phase = 4;
				syslog.log("Entering Phase 4; current distance: " + distanceNow);
			}
			if(distanceNow >= distance) {
				currentSpeed = 0;
				phase = 5;
				syslog.log("Entering Phase 5 from Phase 3; current distance: " + distanceNow);
			}
		} else if(phase == 4) {
			if(distanceNow >= distance) {
				currentSpeed = -1;
				phase = 5;
				syslog.log("Entering Phase 5; current distance: " + distanceNow);
			}
		} else {
			currentSpeed = -1;
			finished = true;
			syslog.log("Finished; current distance: " + distanceNow);
		}
		double angleError = angleNow - targetAngle;
		periodicLog.log("Current Distance: " + distanceNow + "; Current Speed: " + currentSpeed + "; Angle Error: " + angleError);
		//periodicLog.log("Current Speed: " + currentSpeed);
		//periodicLog.log("Angle Error: " + angleError);
		driveTrain.driveCartesian(0.0, currentSpeed * multiplier, -angleError*turnProportion);
		System.out.println("currentSpeed " + currentSpeed);
		System.out.println("remaining distance " + (distance - distanceNow));
	}
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (distanceNow >= distance);
    }

    // Called once after isFinished returns true
    protected void end() {
    	driveTrain.stop();
    	syslog.log("Finished; Current distance: " + (driveTrain.getDrivingController().pidGet() - startingDistance));
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	driveTrain.stop();
    }
}
