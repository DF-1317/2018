package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.DistanceMap;
import org.usfirst.frc.team1317.robot.Logger;
import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * This is the <code>CommandGroup</code> used in the autonomous phase of the match if the robot places the cube on the switch.
 */
public class AutonomousSwitch extends CommandGroup {

	public static final		Logger syslog	= new Logger("1317", AutonomousSwitch.class.getSimpleName());
	Boolean					SwitchLeft		= false;
	int						startingPosition;
	boolean					crossFront;
	double					delay;
	/** 
	 * 
	 * @param plateLocations String containing the game data for the match
	 * @param startingPosition Integer representing the starting position of the robot
	 * @param crossFront Boolean representing whether the robot crosses the court in front of or behind the switch
	 * @param delay Delay in seconds the robot waits after the match starts before running this command (The elevator will still go up immediately.)
	 */
    public AutonomousSwitch(String plateLocations, int startingPosition, boolean crossFront, double delay) {
		super("AutonomousSwitch");
		this.startingPosition	= startingPosition;
		this.crossFront			= crossFront;
		this.delay				= delay;
    	
    	// Assign a value to SwitchLeft variable based on the game data
    	if (plateLocations.charAt(0) == 'L') SwitchLeft = true;

		if (RobotMap.isCompetitionRobot) {
			_autoCompetition();
		} else {
			_autoTest2();
		}
	} // AutonomousSwitch


	private void _autoCompetition() {
    	//will wait the delay before starting
    	addSequential( new Wait(delay) );
    	
    	//if the robot is in the center position
    	if(startingPosition == Robot.Center_Position)
    	{	
    		// Drive midway to the auto line, turn based on the side of the switch is ours, 
    		// drive level to the side of the switch, turn towards the switch, and approach it.
			addSequential( _driveTo(DistanceMap.MIDWAY_AUTO_LINE));
			addSequential( SwitchLeft ? FaceLeft : FaceRight);
    		addSequential( _driveTo(DistanceMap.HORIZONTAL_PAST_SWITCH));
    		addSequential( FaceForward );
    	}
    	else
    	{
    		//if the switch and robot are on the same side
    		if((startingPosition==Robot.Left_Position&&SwitchLeft)||(startingPosition==Robot.Right_Position&&!SwitchLeft)) {
    			addSequential( _driveTo(DistanceMap.MIDWAY_AUTO_TO_SWITCH + DistanceMap.MIDWAY_AUTO_LINE) );
    			if(SwitchLeft) {
    				addSequential(FaceRight);
    			} else {
    				addSequential(FaceLeft);
    			}
    		}
    		//if the switch and robot are on opposite sides
    		else {
				// Instructions to reaching the switch based on whether the robot is crossing the court in front of or behind the switch
    			if(crossFront) {
	    			addSequential( _driveTo(DistanceMap.MIDWAY_AUTO_LINE) );
	    			addSequential( SwitchLeft ? FaceLeft : FaceRight );
	    			addSequential( _driveTo(DistanceMap.CROSS_COURT) );
	    			addSequential( FaceForward );
	    			addSequential( _driveTo(DistanceMap.MIDWAY_AUTO_TO_SWITCH) );
	    			addSequential( SwitchLeft ? FaceRight : FaceLeft );
    			} else {
	    			addSequential( _driveTo(DistanceMap.MIDWAY_AUTO_LINE + DistanceMap.MIDWAY_AUTO_TO_SWITCH + DistanceMap.SWITCH_TO_MIDWAY_SCALE) );
	    			addSequential( SwitchLeft ? FaceLeft : FaceRight );
	    			addSequential( _driveTo(DistanceMap.CROSS_COURT) );
	    			addSequential( FaceForward );
	    			addSequential( _driveTo(DistanceMap.SWITCH_TO_MIDWAY_SCALE) );
	    			addSequential( SwitchLeft ? FaceRight : FaceLeft );
    			}
    			
    		}
    	}
    	// This command will always raise the elevator \\
    	addParallel( new PositionElevator(PositionElevator.SWITCH_POS));
    	addSequential ( new Wait(1.0));
    	// The robot will always approach the switch
		addSequential( new DriveInchesUltrasonic(DistanceMap.APPROACH_SWITCH) );
    	// Always place the cube
		addSequential( new PlaceCube() );
    } // _autoCompetition


	private void _autoTest() {
		double distance2wall = 48; // How far we are to side walls
		//will wait the delay before starting
		addSequential( new Wait(delay) );

		//if the robot is in the center position
		if(startingPosition == Robot.Center_Position)
		{
			// Drive midway to the auto line, turn based on the side of the switch is ours,
			// drive level to the side of the switch, turn towards the switch, and approach it.
			addSequential( _driveTo(12.0));
			addSequential( SwitchLeft ? Turn.left() : Turn.right());
			addSequential( _driveTo(24.0));
			addSequential( SwitchLeft ? Turn.right() : Turn.left() );
			distance2wall = 24; // This far to the driver's wall
		}
		else
		{
			//if the switch and robot are on the same side
			if((startingPosition == Robot.Left_Position && SwitchLeft)
					|| (startingPosition == Robot.Right_Position && !SwitchLeft)) {
				addSequential( _driveTo(24.0) );
				addSequential( SwitchLeft ? Turn.right() : Turn.left() );
			}
			//if the switch and robot are on opposite sides
			else {
				// Instructions to reaching the switch based on whether the robot is crossing the court in front of or behind the switch
				if(crossFront) {
					addSequential( _driveTo(12.0) );
					addSequential( SwitchLeft ? Turn.left() : Turn.right() );
					addSequential( _driveTo(60.0) );
					addSequential( SwitchLeft ? Turn.right() : Turn.left()  );
					addSequential( _driveTo(12.0) );
					addSequential( SwitchLeft ? Turn.right() : Turn.left() );
				} else {
					addSequential( _driveTo(36.0) );
					addSequential( SwitchLeft ? Turn.left() : Turn.right() );
					addSequential( _driveTo(60.0) );
					addSequential( SwitchLeft ? Turn.left() : Turn.right() );
					addSequential( _driveTo(12.0) );
					addSequential( SwitchLeft ? Turn.left() : Turn.right() );
				}

			}
		}
		// This command will always raise the elevator \\
		//addParallel( new PositionElevator(PositionElevator.SWITCH_POS));
		// The robot will always approach the switch
		addSequential( _driveTo(2.0));
		addSequential( new Wait(1.0));
		addSequential( new DriveInchesUltrasonic(distance2wall) );
		// Always place the cube
		//addSequential( new PlaceCube() );
	} // _autoTest

	private void _autoTest2() {
    	double distance2wall = 48; // How far we are to side walls
		// will wait the delay before starting
		addSequential( new Wait(delay) );

		//if the robot is in the center position
		if(startingPosition == Robot.Center_Position)
		{
			// Drive midway to the auto line, turn based on the side of the switch is ours,
			// drive level to the side of the switch, turn towards the switch, and approach it.
			addSequential( _driveTo(12.0));
			addSequential( SwitchLeft ? Face.left() : Face.right());
			addSequential( _driveTo(24.0));
			addSequential( Face.forward() );
			distance2wall = 24; // This far to the driver's wall
		}
		else
		{
			//if the switch and robot are on the same side
			if((startingPosition == Robot.Left_Position && SwitchLeft)
					|| (startingPosition == Robot.Right_Position && !SwitchLeft)) {
				addSequential( _driveTo(24.0) );
				addSequential( SwitchLeft ? Face.right() : Face.left() );
			}
			//if the switch and robot are on opposite sides
			else {
				// Instructions to reaching the switch based on whether the robot is crossing the court in front of or behind the switch
				if(crossFront) {
					addSequential( _driveTo(12.0) );
					addSequential( SwitchLeft ? Face.left() : Face.right() );
					addSequential( _driveTo(60.0) );
					addSequential( Face.forward()  );
				} else {
					addSequential( _driveTo(36.0) );
					addSequential( SwitchLeft ? Face.left() : Face.right() );
					addSequential( _driveTo(60.0) );
					addSequential( Face.backward() );
				}
				addSequential( _driveTo(12.0) );
				addSequential( SwitchLeft ? Face.right() : Face.left() );

			}
		}
		// This command will always raise the elevator \\
		//addParallel( new PositionElevator(PositionElevator.SWITCH_POS));
		// The robot will always approach the switch
		addSequential( _driveTo(2.0));
		addSequential( new Wait(1.0));
		addSequential( new DriveInchesUltrasonic(distance2wall) );
		// Always place the cube
		//addSequential( new PlaceCube() );
	} // _autoTest2

    private Command _driveTo(double target) {
    	return new DriveInchesAccelerate(Robot.DEFAULT_ACCELERATION, target, Robot.DEFAULT_MAX_SPEED);
	}
}
