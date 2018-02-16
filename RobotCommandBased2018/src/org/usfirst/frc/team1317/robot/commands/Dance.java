package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
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
public class Dance extends Command {
	
	Arm arm;
	Climber climber;
	Elevator elevator;
	Claw claw;
	MecanumDriveTrainCAN driveTrain;
	Timer AutoTimer = new Timer();
	
    public Dance() {
    	super("Dance");
    	requires(Robot.arm);
    	requires(Robot.climb);
    	requires(Robot.el);
    	requires(Robot.claw);
    	requires(Robot.mecanumDriveTrain);
    	
    	arm = Robot.arm;
    	climber = Robot.climb;
    	elevator = Robot.el;
    	claw = Robot.claw;
    	driveTrain = Robot.mecanumDriveTrain;
    	
    	setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	AutoTimer.reset();
    	AutoTimer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(AutoTimer.get() < 1) {
    		driveTrain.driveCartesian(0, 0, 0.5);
    		elevator.move(0.5);
    		claw.openClaw();
    		climber.move(0.5);
    	}
    	else if(AutoTimer.get() < 2) {
    		driveTrain.driveCartesian(0, 0, -0.5);
    		elevator.move(-0.5);
    		claw.closeClaw();
    		climber.move(-0.5);
    	}
    	else if(AutoTimer.get() < 3) {
    		AutoTimer.reset();
    	}
    		
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	driveTrain.stop();
    	elevator.move(0);
    	climber.move(0);
    }
}
