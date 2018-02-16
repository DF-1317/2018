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
public class DanceSine extends Command {
	
	Arm arm;
	Climber climber;
	Elevator elevator;
	Claw claw;
	MecanumDriveTrainCAN driveTrain;
	Timer AutoTimer = new Timer();
	
    public DanceSine() {
    	super("DanceSine");
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
		driveTrain.driveCartesian(0, 0, Math.sin(AutoTimer.get()) * 0.5);
		elevator.move(Math.sin(AutoTimer.get() * (1/6)) * 0.5);
		climber.move(Math.sin(AutoTimer.get() * (1/6)) * 0.5);
		if((AutoTimer.get() / 2) % 2 == 0) {
			claw.toggleClaw();
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
