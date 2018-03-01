package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.DistanceMap;
import org.usfirst.frc.team1317.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DropIfSide extends CommandGroup {

    public DropIfSide(String plateLocations, int startingPosition, boolean crossFront, double delay) {
    	requires(Robot.mecanumDriveTrain);
    	requires(Robot.claw);
    	requires(Robot.el);
    	requires(Robot.arm);
    	System.out.println("Constructed DropIfSide");
    	addSequential(new ClawClose());
    	addSequential( new Wait(delay));
    	addSequential( new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, DistanceMap.MIDWAY_AUTO_LINE + 28 + DistanceMap.APPROACH_SWITCH_SIDE, Robot.DEFAULT_MAX_SPEED));
    	if((plateLocations.charAt(0)=='L'&& startingPosition == Robot.Left_Position) || plateLocations.charAt(0) == 'R' && startingPosition == Robot.Right_Position) {
    		addSequential(new PositionElevatorTime(DistanceMap.ELEVATOR_TO_SWITCH_TIME, 1.0));
    		addSequential(new PlaceCube());
    	}
    }
}
