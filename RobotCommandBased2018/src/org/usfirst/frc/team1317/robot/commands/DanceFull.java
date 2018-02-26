package org.usfirst.frc.team1317.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class DanceFull extends CommandGroup {

    public DanceFull() {
    	addSequential(new DanceSine(2));
    	addSequential(new TurnDegrees(180));
    	addSequential(new DanceSine(2));
    }
}
