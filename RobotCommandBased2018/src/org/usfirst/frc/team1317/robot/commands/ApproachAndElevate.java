package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Approaches the target by moving a certain distance from a wall, while at the same time raising the elevator
 */
public class ApproachAndElevate extends CommandGroup {

    public ApproachAndElevate(double distanceFromWall, double elevateTime) {
        addParallel(new UltrasonicDriveToDistance(distanceFromWall));
        addParallel(new PositionElevatorTime(elevateTime, 0.7));
    }
}
