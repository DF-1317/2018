package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.OI;
import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This command attaches joystick input to the elevator
 */
public class JoystickElevatorCommand extends Command {

    public JoystickElevatorCommand() {
    	super("JoystickElevatorCommand");
        requires(Robot.el);
        setInterruptible(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.el.move(OI.OtherJoystick.getY());
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
    }
}
