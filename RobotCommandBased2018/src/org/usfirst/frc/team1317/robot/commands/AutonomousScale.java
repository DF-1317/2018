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
		requires(Robot.mecanumDriveTrain);
    	requires(Robot.claw);
    	requires(Robot.el);
    	requires(Robot.arm);
		addSequential(new ClawClose());
		if (plateLocations.charAt(1) == 'L') ScaleLeft = true;
		else if(plateLocations.charAt(1) == 'R') ScaleLeft = false;
		else return;
		if (RobotMap.isCompetitionRobot) {
			_autoCompetition();
		} else {
			_autoTest2();
		}
	} // AutonomousScale

	private void _autoCompetition() {
    	//waits the specified amount of time
    	addSequential(new Wait(delay));
    	//if the robot is in the center position
    	if (startingPosition == Robot.Center_Position) {
    		addSequential(_driveTo(DistanceMap.MIDWAY_AUTO_LINE));
    		addSequential(ScaleLeft ? Face.left():Face.right());
    		addSequential(_driveTo(DistanceMap.HORIZONTAL_PAST_SWITCH));
    		addSequential(Face.forward());
    		addSequential(_driveTo(DistanceMap.MIDWAY_AUTO_TO_SCALE));
    	}
    	else {
    		//if the scale is on the same position as the robot
    		if((startingPosition==Robot.Left_Position&&ScaleLeft)||(startingPosition==Robot.Right_Position&&!ScaleLeft)) {
    			addSequential(_driveTo(DistanceMap.MIDWAY_AUTO_LINE
						+ DistanceMap.MIDWAY_AUTO_TO_SCALE));
    		} else { //if we have to cross the court to get to the scale
    			//if we're crossing in front of the switch
    			if(crossFront) {
    				addSequential( _driveTo(DistanceMap.MIDWAY_AUTO_LINE) );
	    			addSequential( ScaleLeft ? Face.left() : Face.right() );
	    			addSequential( _driveTo(DistanceMap.CROSS_COURT) );
	    			addSequential( Face.forward() );
	    			addSequential( _driveTo(DistanceMap.MIDWAY_AUTO_TO_SWITCH) );
    			} else { //if we're crossing behind the switch
    				addSequential( _driveTo(DistanceMap.MIDWAY_AUTO_LINE
							+ DistanceMap.MIDWAY_AUTO_TO_SWITCH
							+ DistanceMap.SWITCH_TO_MIDWAY_SCALE) );
	    			addSequential( ScaleLeft ? Face.left() : Face.right() );
	    			addSequential( _driveTo(DistanceMap.CROSS_COURT) );
	    			addSequential( Face.forward() );
	    			addSequential( _driveTo(DistanceMap.SWITCH_TO_SCALE
							- DistanceMap.SWITCH_TO_MIDWAY_SCALE) );
    			}
    		}
    	}
    	//always face the scale
    	addSequential(ScaleLeft? Face.right() : Face.left());
    	//drive forward to correct turning error
    	addSequential(_driveTo(2.0));
    	//elevator starts moving up
    	//approach the scale, regardless of path taken
    	addSequential(new Wait(1.0));
    	addSequential(new ApproachAndElevate(DistanceMap.DISTANCE_FROM_WALL_SCALE, DistanceMap.ELEVATOR_TO_SCALE_TIME));
    	//always place cube at the end of autonomous
    	addSequential(new PlaceCube());
    	
    	//reset things
    	addSequential(new ArmDown());
    	addSequential(new PositionElevatorTime(DistanceMap.ELEVATOR_TO_SCALE_TIME, -0.7));
    	//addSequential(new DanceFull());
    	
    } // _autoCompetition

	private void _autoTest() {
		//waits the specified amount of time
		addSequential(new Wait(delay));
		//if the robot is in the center position
		if (startingPosition == Robot.Center_Position) {
			addSequential(_driveTo(12.0));
			addSequential(ScaleLeft ? Turn.left() : Turn.right() );
			addSequential(_driveTo(24.0));
			addSequential(ScaleLeft ? Turn.right() : Turn.left() );
			addSequential(_driveTo(48.0));
		}
		else {
			//if the scale is on the same position as the robot
			if((startingPosition == Robot.Left_Position && ScaleLeft)
					|| (startingPosition == Robot.Right_Position && !ScaleLeft)) {
				addSequential(_driveTo(60.0));
			} else { //if we have to cross the court to get to the scale
				//if we're crossing in front of the switch
				if(crossFront) {
					addSequential( _driveTo(12.0) );
					addSequential( ScaleLeft ? Turn.left() : Turn.right() );
					addSequential( _driveTo(60.0) );
					addSequential( ScaleLeft ? Turn.right() : Turn.left() );
					addSequential( _driveTo(48.0) );
				} else { //if we're crossing behind the switch
					addSequential( _driveTo(36.0) );
					addSequential( ScaleLeft ? Turn.left() : Turn.right() );
					addSequential( _driveTo(60.0) );
					addSequential( ScaleLeft ? Turn.right() : Turn.left() );
					addSequential( _driveTo(24.0) );
				}

			}
		}
		addSequential( ScaleLeft ? Turn.right() : Turn.left() );
		//elevator starts moving up
		// addParallel(new PositionElevatorTime(1.0, 0.5));
		//approach the scale, regardless of path taken
		addSequential(_driveTo(2.0));
		//addSequential(new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, 2.0, Robot.DEFAULT_MAX_SPEED, true));
		addSequential( new Wait(1.0));
		addSequential(new DriveInchesUltrasonic(48.0));
		//always place cube at the end of autonomous
		// addSequential(new PlaceCube());
	} // _autoTest

	private void _autoTest2() {
		//waits the specified amount of time
		addSequential(new Wait(delay));
		//if the robot is in the center position
		if (startingPosition == Robot.Center_Position) {
			addSequential(_driveTo(12.0));
			addSequential(ScaleLeft ? Face.left() : Face.right() );
			addSequential(_driveTo(24.0));
			addSequential(Face.forward());
			addSequential(_driveTo(48.0));
		}
		else {
			//if the scale is on the same position as the robot
			if((startingPosition == Robot.Left_Position && ScaleLeft)
					|| (startingPosition == Robot.Right_Position && !ScaleLeft)) {
				addSequential(_driveTo(60.0));
			} else { //if we have to cross the court to get to the scale
				//if we're crossing in front of the switch
				if(crossFront) {
					addSequential( _driveTo(12.0) );
					addSequential( ScaleLeft ? Face.left() : Face.right() );
					addSequential( _driveTo(60.0) );
					addSequential( Face.forward() );
					addSequential( _driveTo(48.0) );
				} else { //if we're crossing behind the switch
					addSequential( _driveTo(36.0) );
					addSequential( ScaleLeft ? Face.left() : Face.right() );
					addSequential( _driveTo(60.0) );
					addSequential( Face.forward() );
					addSequential( _driveTo(24.0) );
				}

			}
		}
		addSequential( ScaleLeft ? Face.right() : Face.left() );
		//elevator starts moving up
		// addParallel(new PositionElevatorTime(1.0, 0.5));
		//approach the scale, regardless of path taken
		addSequential(_driveTo(2.0));
		//addSequential(new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, 2.0, Robot.DEFAULT_MAX_SPEED, true));
		addSequential( new Wait(1.0));
		addSequential(new DriveInchesUltrasonic(48.0));
		//always place cube at the end of autonomous
		// addSequential(new PlaceCube());
	} // _autoTest2
	
	private Command _driveTo(double target) {
		return new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, target, Robot.DEFAULT_MAX_SPEED);
	} // _driveTo
}
