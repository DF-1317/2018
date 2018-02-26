package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Logger;
import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.RobotMap;
import org.usfirst.frc.team1317.robot.subsystems.Arm;
import org.usfirst.frc.team1317.robot.subsystems.Claw;
import org.usfirst.frc.team1317.robot.subsystems.Climber;
import org.usfirst.frc.team1317.robot.subsystems.Elevator;
import org.usfirst.frc.team1317.robot.subsystems.MecanumDriveTrainCAN;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Don't ask
 */
public class DanceSine extends Command {
	
	Arm arm;
	Climber climber;
	Elevator elevator;
	Claw claw;
	MecanumDriveTrainCAN driveTrain;
	Timer AutoTimer = new Timer();
	
	Logger periodicLog = new Logger("1317", DanceSine.class.getSimpleName(), 10);
	Logger syslog = new Logger("1317", DanceSine.class.getSimpleName());
	
	double maxLength;
	
    public DanceSine(double maxLength) {
    	super("DanceSine");
    	
    	this.maxLength = maxLength;
    	
    	if(RobotMap.isCompetitionRobot) {
	    	requires(Robot.arm);
	    	requires(Robot.climb);
	    	requires(Robot.el);
	    	requires(Robot.claw);
    	}
    	requires(Robot.mecanumDriveTrain);
    	
    	if(RobotMap.isCompetitionRobot) {
	    	arm = Robot.arm;
	    	climber = Robot.climb;
	    	elevator = Robot.el;
	    	claw = Robot.claw;
    	}
    	driveTrain = Robot.mecanumDriveTrain;
    	
    	setInterruptible(true);
    	syslog.log("DanceSine contruction complete");
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	AutoTimer.reset();
    	AutoTimer.start();
    	syslog.log("DanceSine init complete");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	double speed = Math.sin(AutoTimer.get() * 6.0);
		driveTrain.driveCartesian(0, 0, speed * 0.5);
		if(RobotMap.isCompetitionRobot) {
			elevator.move(speed * 0.5);
			arm.move(speed * 0.1);
			if((AutoTimer.get() / 2) % 2 == 0) {
				claw.toggleClaw();
			}
		}
		periodicLog.log("Current speed: " + speed * 0.5);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return AutoTimer.get() >= maxLength;
    }

    // Called once after isFinished returns true
    protected void end() {
    	driveTrain.stop();
    	if(RobotMap.isCompetitionRobot) {
	    	elevator.move(0);
	    	arm.move(0);
    	}
    	AutoTimer.stop();
    	syslog.log("DanceSine ended");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	driveTrain.stop();
    	if(RobotMap.isCompetitionRobot) {
	    	elevator.move(0);
	    	arm.move(0);
    	}
    	AutoTimer.stop();
    	syslog.log("DanceSine interrupted");
    }
}
