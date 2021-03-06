package org.usfirst.frc.team1317.robot;

/**
 * This class contains constants for field dimensions
 */

public class DistanceMap {
	//all values are in inches, except where otherwise noted.
	
	//These are constants that reflect actual field dimensions that can be used for calculating the paths if we want to be more flexible
	//These are from https://firstfrc.blob.core.windows.net/frc2018/Drawings/LayoutandMarkingDiagram.pdf
	public static final double WALL_TO_AUTO_LINE = 120.0;
	public static final double WALL_TO_SWITCH = 144.0;
	public static final double WALL_TO_FAR_SIDE_OF_SWITCH = 196.0;
	public static final double WALL_TO_PLATFORM = 261.47;
	public static final double WALL_TO_NULL_ZONE = 288.0;
	public static final double NULL_ZONE_LENGTH = 72.0;
	public static final double PLAYER_STATION_WIDTH = 264.0;
	public static final double PORTAL_WIDTH = 29.69; //the distance parallel to the player station wall from 
	public static final double NULL_ZONE_WIDTH = 95.25;
	public static final double SIDE_WALL_TO_SWITCH = 85.25;
	
	//these are robot constants. They will need to be changed
	public static final double ROBOT_WIDTH = 26.0+7.0;
	public static final double ROBOT_LENGTH = 32.5+7.0;
	public static final double DISTANCE_BETWEEN_WHEELS = 22.0;
	
	
	// These are constants for field distances to be used in autonomous paths
	public static final double MIDWAY_OFFSET = 65.0; //this should take into account the robot width and the stack of powercubes
	public static final double COURT_WIDTH = PORTAL_WIDTH * 2 + PLAYER_STATION_WIDTH;
	public static final double SWITCH_WIDTH = COURT_WIDTH - SIDE_WALL_TO_SWITCH * 2;
	
	public static final double AUTO_LINE = 100.0;
	public static final double MIDWAY_AUTO_LINE = 50.0;
	public static final double HORIZONTAL_PAST_SWITCH = 115.5; // old value: (COURT_WIDTH + SIDE_WALL_TO_SWITCH * 2)/2 + 10.0;
	public static final double HORIZONTAL_PAST_SWITCH_LEFT = 126.0;
	public static final double HORIZONTAL_PAST_SWITCH_RIGHT = 105.0;	
	public static final double CROSS_COURT = 231.0; 
	public static final double MIDWAY_AUTO_TO_SCALE = 254.25; //old value: WALL_TO_NULL_ZONE - MIDWAY_AUTO_LINE ;
	public static final double MIDWAY_AUTO_TO_SWITCH = 98.25; //WALL_TO_SWITCH - MIDWAY_AUTO_LINE;
	public static final double APPROACH_SWITCH = 19.31;
	public static final double APPROACH_SWITCH_SIDE = 50.5;
	public static final double APPROACH_SCALE = 5.63 - 3;
	public static final double SWITCH_TO_SCALE = 156.0; // old value: WALL_TO_NULL_ZONE - WALL_TO_SWITCH;
	public static final double SWITCH_TO_MIDWAY_SCALE = 67.24; //old value: SWITCH_TO_SCALE / 2;
	public static final double MIDWAY_SCALE_TO_SCALE = 88.77;
	public static final double ROBOT_WALL_TO_SCALE = 304.25; 
	public static final double ROBOT_WALL_TO_SWITCH = 148.25; 
	public static final double ROBOT_WALL_TO_MIDWAY_SCALE = 215.49;
	public static final double CENTER_TO_SWITCH = 55.0;
	public static final double CENTER_TO_SWITCH_LEFT = 75.5; //old was 65.5
	public static final double CENTER_TO_SWITCH_RIGHT = 55.5; //old was 45.5
	public static final double SWITCH_CROSS_FRONT = 170.5;
	
	//the following values are used when aligning the robot by ultrasonic sensor
	
	//the distance from back of the robot to the wall when the front of the robot is aligned with the front of the scale minus the distance we want to be from the scale
	//We will also need to add the distance the ultrasonic sensor is from the back of the robot to this value
	public static final double DISTANCE_FROM_WALL_SCALE = 32.07 - 2.0 + 11.0; 
	
	//see comments on DISTANCE_FROM_WALL_SCALE
	public static final double DISTANCE_FROM_WALL_SWITCH = 47.75 - 2.0 + 11.0;
	
	//Distance used to line up when approaching the switch from the front. See also comments on DISTANCE_FROM_WALL_SCALE
	public static final double DISTANCE_FROM_PLAYERSTATIONWALL_SWITCH = 100.5 - 2.0 + 11.0;
	
	//times for moving arm and elevator
	public static final double ARM_MOVE_TIME = 0.8;
	public static final double ELEVATOR_TO_SWITCH_TIME = 1.0;
	public static final double ELEVATOR_TO_SCALE_TIME = 3.6;
	
}
