package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.DistanceMap;
import org.usfirst.frc.team1317.robot.Logger;
import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * A command to deposit a cube in autonomous
 */
public class AutonomousScale extends CommandGroup {

	public static final		Logger syslog	= new Logger("1317", AutonomousScale.class.getSimpleName());
	Boolean					ScaleLeft		= false;
	double					heading			= 90.0;
	Command					TurnLeft 		= new TurnDegrees(-90.0, 1.0);
	Command					TurnRight		= new TurnDegrees(90.0, 1.0);
	int						startingPosition;
	boolean					crossFront;
	double					delay;

	/**
	 * creates a new command to deposit cube in autonomous
	 * 
	 * @param plateLocations - a string representing the positions of the plates. The code is expecting a string a three characters that are either 'L' or 'R'. If it does not get this, it will throw a NullPointerException.
	 * @param startingPosition - a number representing the starting position of the robot in autonomous
	 * @param crossFront - a boolean value representing whether the robot will cross in front of the switch if it must cross the field. <code>true</code> represents that the robot will cross between the player station wall and the switch. <code>false</code> means that the robot will cross between the switch and the platform. 
	 * @param delay - the time in seconds the robot will wait before starting its routine
	 * 
	 * @see AutonomousSwitch
	 */
    public AutonomousScale(String plateLocations, int startingPosition, boolean crossFront, double delay) {
		this.startingPosition = startingPosition;
		this.crossFront = crossFront;
		this.delay = delay;
		if (plateLocations.charAt(1) == 'L') {
			ScaleLeft = true;
			heading = -heading;
		}

		if (RobotMap.isCompetitionRobot) {
			_autoCompetition();
		} else {
			_autoTest();
		}
	} // AutonomousScale

	private void _autoCompetition() {
    	//waits the specified amount of time
    	addSequential(new Wait(delay));
    	//if the robot is in the center position
    	if (startingPosition == Robot.Center_Position) {
    		addSequential(_driveTo(DistanceMap.MIDWAY_AUTO_LINE));
    		addSequential(ScaleLeft ? TurnLeft:TurnRight);
    		addSequential(_driveTo(DistanceMap.HORIZONTAL_PAST_SWITCH));
    		addSequential(ScaleLeft ? TurnRight:TurnLeft);
    		addSequential(_driveTo(DistanceMap.MIDWAY_AUTO_TO_SCALE));
    		addSequential(ScaleLeft ? TurnRight:TurnLeft);
    	}
    	else {
    		//if the scale is on the same position as the robot
    		if((startingPosition==Robot.Left_Position&&ScaleLeft)||(startingPosition==Robot.Right_Position&&!ScaleLeft)) {
    			addSequential(_driveTo(DistanceMap.MIDWAY_AUTO_LINE
						+ DistanceMap.MIDWAY_AUTO_TO_SCALE));
    			addSequential(ScaleLeft ? TurnRight:TurnLeft);
    		} else { //if we have to cross the court to get to the scale
    			//if we're crossing in front of the switch
    			if(crossFront) {
    				addSequential( _driveTo(DistanceMap.MIDWAY_AUTO_LINE) );
	    			addSequential( ScaleLeft ? TurnRight : TurnLeft );
	    			addSequential( _driveTo(DistanceMap.CROSS_COURT) );
	    			addSequential( ScaleLeft ? TurnLeft : TurnRight );
	    			addSequential( _driveTo(DistanceMap.MIDWAY_AUTO_TO_SWITCH) );
	    			addSequential( ScaleLeft ? TurnLeft : TurnRight );
    			} else { //if we're crossing behind the switch
    				addSequential( _driveTo(DistanceMap.MIDWAY_AUTO_LINE
							+ DistanceMap.MIDWAY_AUTO_TO_SWITCH
							+ DistanceMap.SWITCH_TO_MIDWAY_SCALE) );
	    			addSequential( ScaleLeft ? TurnLeft : TurnRight );
	    			addSequential( _driveTo(DistanceMap.CROSS_COURT) );
	    			addSequential( ScaleLeft ? TurnRight : TurnLeft );
	    			addSequential( _driveTo(DistanceMap.SWITCH_TO_SCALE
							- DistanceMap.SWITCH_TO_MIDWAY_SCALE) );
	    			addSequential( ScaleLeft ? TurnRight : TurnLeft );
    			}

    		}
    	}
    	//elevator starts moving up
    	addParallel(new PositionElevatorTime(1.0, 0.5));
    	//approach the scale, regardless of path taken
    	addSequential(new Wait(1.0));
    	addSequential(Robot.ultrasonicDriveToDistance(DistanceMap.APPROACH_SCALE));
    	//always place cube at the end of autonomous
    	addSequential(new PlaceCube());
    } // _autoCompetition

	private void _autoTest() {
		//waits the specified amount of time
		addSequential(new Wait(delay));
		//if the robot is in the center position
		if (startingPosition == Robot.Center_Position) {
			addSequential(_driveTo(12.0));
			addSequential(ScaleLeft ? _turnLeft():_turnRight());
			addSequential(_driveTo(24.0));
			addSequential(ScaleLeft ? _turnRight():_turnLeft());
			addSequential(_driveTo(12.0));
			addSequential(ScaleLeft ? _turnRight():_turnLeft());
		}
		else {
			//if the scale is on the same position as the robot
			if((startingPosition == Robot.Left_Position && ScaleLeft)
					|| (startingPosition == Robot.Right_Position && !ScaleLeft)) {
				addSequential(_driveTo(48.0));
				addSequential(ScaleLeft ? _turnRight():_turnLeft());
			} else { //if we have to cross the court to get to the scale
				//if we're crossing in front of the switch
				if(crossFront) {
					addSequential( _driveTo(24.0) );
					addSequential( ScaleLeft ? _turnLeft() : _turnRight() );
					addSequential( _driveTo(60.0) );
					addSequential( ScaleLeft ? _turnRight() : _turnLeft() );
					addSequential( _driveTo(24.0) );
					addSequential( ScaleLeft ? _turnRight() : _turnLeft() );
				} else { //if we're crossing behind the switch
					addSequential( _driveTo(36.0) );
					addSequential( ScaleLeft ? _turnLeft() : _turnRight() );
					addSequential( _driveTo(60.0) );
					addSequential( ScaleLeft ? _turnRight() : _turnLeft() );
					addSequential( _driveTo(12.0) );
					addSequential( ScaleLeft ? _turnRight() : _turnLeft() );
				}

			}
		}
		//elevator starts moving up
		// addParallel(new PositionElevatorTime(1.0, 0.5));
		//approach the scale, regardless of path taken
		addSequential(_driveTo(2.0));
		addSequential(new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, 2.0, Robot.DEFAULT_MAX_SPEED, true));
		addSequential( new Wait(1.0));
		addSequential(new DriveInchesUltrasonic(36.0));
		//always place cube at the end of autonomous
		// addSequential(new PlaceCube());
	} // _autoTest

    private Command _turnLeft() {
    	return new TurnDegrees(-90.0, 0.1);
    }
    
    private Command _turnRight() {
    	return new TurnDegrees(90.0, 0.1);
    }
	
	private Command _driveTo(double target) {
		return new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, target, Robot.DEFAULT_MAX_SPEED);
	} // _driveTo
}
