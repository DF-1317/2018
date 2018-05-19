package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Console;
import org.usfirst.frc.team1317.robot.Logger;
import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.RobotMap;
import org.usfirst.frc.team1317.robot.subsystems.MecanumDriveTrainCAN;

import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 */
public class DriveInchesAccelerate extends Command {

	final double turnProportion = 0.00;
	final double turnProportionEncoder = 0.00;
	
	double acceleration;
	double distance;
	double maxSpeed;
	double partway;
	
	double startingDistance;
	double multiplier;
	double distanceNow;
	
	double accelDistance;
	double currentSpeed = 0;
	
	double targetAngle;
	
	int phase = 1;
	boolean onTarget = false;
	int doneCount = 0;
	boolean finished = false;
	
	Logger syslog = new Logger("1317", "DriveInchesAccelerate");
	Logger periodicLog = new Logger("1317", "DriveInchesAccelerate", 5);
	
	MecanumDriveTrainCAN driveTrain = Robot.mecanumDriveTrain;
	
	public static final double SLOW_SPEED = 0.15;
	public static final int WAIT_COUNT = 15;
	public static double SLOW_DISTANCE_MULTIPLIER = 2;
	public static double DECELERATE_FRACTION = 0.5;
	

	/**
	 * Table of offsets to apply for the listed distances
	 *
	 * The key of the table represents the distance the caller wants to move, the value for the key is the offset
	 * to apply which will correct for any overrun the drivetrain will have in it.
	 */
	private static final Map<Double,Double> Offsets;

	private static final double AllowableError = 2.0;
	static {
		Map<Double,Double> tmp = new TreeMap<Double,Double>();
		if(RobotMap.isCompetitionRobot) {
			tmp.put(2.0, 1.0);
			tmp.put(4.0, 2.0);
			tmp.put(6.0, 5.0);
			tmp.put(8.0, 8.0);
			tmp.put(10.0, 10.0);
			tmp.put(12.0, 11.0);
			tmp.put(14.0, 13.0);
			tmp.put(16.0, 14.5);
			tmp.put(18.0, 16.0);
			tmp.put(24.0, 19.0);
			tmp.put(30.0, 24.0);
			tmp.put(60.0, 17.0);
			tmp.put(80.0, 26.0);
			tmp.put(100.0, 15.0);
			tmp.put(120.0, 15.0);
		}
		else {
		tmp.put(2.0, 0.0);
		tmp.put(4.0, 1.0);
		tmp.put(6.0, 2.0);
		tmp.put(8.0, 3.0);
		tmp.put(10.0, 4.0);
		tmp.put(12.0, 6.0);
		tmp.put(14.0, 7.0);
		tmp.put(16.0, 8.0);
		tmp.put(18.0, 9.0);
		tmp.put(24.0, 10.0);
		tmp.put(30.0, 12.0);
		tmp.put(60.0, 10.0);
		tmp.put(80.0, 5.0);
		tmp.put(100.0, 2.0);
		tmp.put(120.0, 0.0);
		}
		Offsets = Collections.unmodifiableMap(tmp);
	}
	
	double kP = 0.0;
	boolean usingGyro = false;

	/**
	 * Use the configured offset table to figure out what correction is needed
	 *
	 * The target distance is used to find the right distance offset we need to apply. We look through the table
	 * to see in what range the target is, and return the value in the table that occurs at the base of
	 * that range. If the target is < the minimum in the table, we return 0. If > the largest key,
	 * we return the value for the largest key in the table.
	 *
	 * @param target		the intended distance the caller wants to drive
	 * @return the offset to apply to the distance to adjust for the drive train
	 */
	static double getOffset( double target ) {
		double offset = 0.0;
		for (Map.Entry<Double,Double> e : Offsets.entrySet()) {
			if (e.getKey() > target) break;
			offset = e.getValue();
		}
		return offset;
	}
	
    public DriveInchesAccelerate(double acceleration, double distance, double maxSpeed, double targetAngle, boolean inReverse) {
    	
    	requires(Robot.mecanumDriveTrain);
    	DECELERATE_FRACTION = SmartDashboard.getNumber("Decelerate Fraction", 0.45);
    	
        this.acceleration = acceleration / 50;
		this.distance = distance; /* - getOffset(distance);*/
        //this.partway = distance / (SLOW_DISTANCE_MULTIPLIER + 1);
		this.partway = distance * (1.0-DECELERATE_FRACTION);
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
    	usingGyro = driveTrain.navX.isConnected();
    	doneCount = 0;
    	if(targetAngle == 1000.0&& usingGyro) {
    		targetAngle = driveTrain.navX.getAngle();
    	}
    	syslog.log("Target Distance: " + distance);
    }

	// Called repeatedly when this Command is scheduled to run
	protected void executeOld() {
    	distanceNow = (driveTrain.getDrivingController().pidGet() - startingDistance) * multiplier;
    	double angleNow = 0.0;
    	if(usingGyro)
    		angleNow = driveTrain.navX.getAngle();
		if(phase == 1) {
			currentSpeed += acceleration;
			if(currentSpeed >= maxSpeed) {
				currentSpeed = maxSpeed;
				phase = 2;
				accelDistance = distanceNow;
				syslog.log("Entering Phase 2; accelDistance: " + accelDistance);
				accelDistance *= 4;
			}
			if((distanceNow) >= partway) {
				phase = 3;
				syslog.log("Entering Phase 3 early; current distance: " + distanceNow); 
			}
		} else if(phase == 2) {
			if((distanceNow) >= partway) {
				phase = 3;
				syslog.log("Entering Phase 3; current distance: " + distanceNow);
			}
		} else if(phase == 3) {
			if(currentSpeed > SLOW_SPEED) {
				currentSpeed -= acceleration * 1;
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
		double angleError = 0.0;
		if(usingGyro) {
			angleError = angleNow - targetAngle;
		}
		periodicLog.log("Current Distance: " + distanceNow + "; Current Speed: " + currentSpeed + "; Angle Error: " + angleError + "; Ultrasonic Distance: " + Robot.Ultrasonic.getRangeInches());
		//periodicLog.log("Current Speed: " + currentSpeed);
		//periodicLog.log("Angle Error: " + angleError);
		driveTrain.driveCartesian(0.0, currentSpeed * multiplier, -angleError*turnProportion);
		Console.show(4, "currentSpeed " + currentSpeed
				+ ", remaining distance " + (distance - distanceNow));
	}
	
	protected void execute() {
		distanceNow = (driveTrain.getDrivingController().pidGet() - startingDistance) * multiplier;
		double angleNow = 0.0;
		if(usingGyro) {
			driveTrain.navX.getAngle();
		}
		
		if(Math.abs(distanceNow - distance) <= AllowableError) {
			onTarget = true;
			doneCount++;
			currentSpeed = 0;
			syslog.log("On Target; current distance: " + distanceNow);
		}
		else { 
			doneCount = 0;
			if(phase == 1) {
				currentSpeed += acceleration;
				//correctEncoders();
				if(currentSpeed >= maxSpeed) {
					currentSpeed = maxSpeed;
					phase = 2;
					accelDistance = distanceNow;
					syslog.log("Entering Phase 2; accelDistance: " + accelDistance);
					accelDistance *= 4;
				}
				if((distanceNow) >= partway) {
					phase = 3;
					kP = currentSpeed/distanceNow;
					syslog.log("Entering Phase 3 early; current distance: " + distanceNow); 
				}
			} else if(phase == 2) {
				if((distanceNow) >= partway) {
					phase = 3;
					syslog.log("Entering Phase 3; current distance: " + distanceNow);
					kP = currentSpeed/distanceNow; 
				}
			} else if(phase == 3) {
				currentSpeed = kP*(distance - distanceNow);
				if(currentSpeed <= SLOW_SPEED) {
					phase = 4;
				}

			} else if(phase == 4) {
				if(distanceNow < distance ) {
					currentSpeed = SLOW_SPEED;
				}
				else {
					currentSpeed = -SLOW_SPEED;
				}
			}
		}

		double angleError = 0;
		if(usingGyro) {
			angleError = angleNow - targetAngle;
			driveTrain.driveCartesian(0.0, currentSpeed * multiplier, -angleError*turnProportion);
		}
		else {
			angleError = calculateEncoderSteeringError();
			driveTrain.driveCartesian(0.0, currentSpeed * multiplier, -angleError*turnProportionEncoder);
		}
		periodicLog.log("Current Distance: " + distanceNow + "; Current Speed: " + currentSpeed + "; Angle Error: " + angleError + "; Ultrasonic Distance: " + Robot.Ultrasonic.getRangeInches());
		//periodicLog.log("Current Speed: " + currentSpeed);
		//periodicLog.log("Angle Error: " + angleError);
		
		Console.show(4, "currentSpeed " + currentSpeed
				+ ", remaining distance " + (distance - distanceNow));
	}
	
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinishedOld() {
        return (distanceNow >= distance);
    }
    
    protected boolean isFinished() {
    	return (onTarget && doneCount >= WAIT_COUNT );
    }

    // Called once after isFinished returns true
    protected void end() {
    	driveTrain.stop();
    	syslog.log("Finished; Current distance: " + multiplier*(driveTrain.getDrivingController().pidGet() - startingDistance));
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	driveTrain.stop();
    	syslog.log("Interrupted");
    }
    
    
    private double calculateEncoderSteeringError() {
    	return -RobotMap.RightEncoderDirection * driveTrain.BREncoder.getDistance() + RobotMap.LeftEncoderDirection * driveTrain.BLEncoder.getDistance();
    }
    
    void correctEncoders() {
    	if((driveTrain.BLEncoder.get() > 0 && multiplier < 0) || (driveTrain.BLEncoder.get() < 0 && multiplier > 0)) {
    		RobotMap.LeftEncoderDirection *= -1;
    	}
    	if((driveTrain.BREncoder.get() > 0 && multiplier < 0) || (driveTrain.BREncoder.get() < 0 && multiplier > 0)) {
    		RobotMap.RightEncoderDirection *= -1;
    	}
    }
}
