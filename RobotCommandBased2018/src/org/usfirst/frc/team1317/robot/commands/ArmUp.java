package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ArmUp extends Command {
	
	Arm arm;

    public ArmUp() {
        super("ArmUp");
        //requires the arm
        requires(Robot.arm);
        arm = Robot.arm;
        //this command can be interrupted
        setInterruptible(true);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	//move the arm up
    	arm.move(1);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
    	//this is finished when the top switch is pressed
        return arm.topSwitchPressed();
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	//stop arm when finished
    	arm.move(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	arm.move(0);
    }
}
