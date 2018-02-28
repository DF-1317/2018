package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.subsystems.Arm;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This command moves the arm down
 * 
 * @see ArmDown
 * @see ArmToggle
 */
public class ArmDown extends Command {

	
	double timeToMove = 0.5;
	Arm arm;
	Timer timer = new Timer();

    public ArmDown(double time) {
        super("ArmDown");
        //We are using the arm subsystem
        requires(Robot.arm);
        arm = Robot.arm;
        //this command can be interrupted
        setInterruptible(true);
        timeToMove = time;
    }
    
    public ArmDown() {
    	super("ArmDown");
        //We are using the arm subsystem
        requires(Robot.arm);
        arm = Robot.arm;
        //this command can be interrupted
        setInterruptible(true);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	timer.reset();
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	//move motor backwards at full speed
    	arm.move(-0.3);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
    	//We are done if the bottom switch is pressed.
        return (timer.get() >= timeToMove);
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	//stop the arm when we are done
    	arm.move(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	arm.move(0);
    }
}
