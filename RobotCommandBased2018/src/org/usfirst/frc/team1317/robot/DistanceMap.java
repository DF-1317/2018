package org.usfirst.frc.team1317.robot;

public class DistanceMap {
	//all values are in inches.
	
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
	public static final double ROBOT_WIDTH = 26.0+3.0;
	public static final double ROBOT_LENGTH = 32.5+3.0;
	
	
	
	// These are constants for field distances to be used in autonomous paths
	public static final double MIDWAY_OFFSET = 30.0;
	public static final double COURT_WIDTH = PORTAL_WIDTH * 2 + PLAYER_STATION_WIDTH;
	public static final double SWITCH_WIDTH = COURT_WIDTH - SIDE_WALL_TO_SWITCH * 2;
	
	public static final double AUTO_LINE = WALL_TO_AUTO_LINE - ROBOT_LENGTH + 2.0;
	public static final double MIDWAY_AUTO_LINE = AUTO_LINE - MIDWAY_OFFSET;
	public static final double HORIZONTAL_PAST_SWITCH = (COURT_WIDTH + SIDE_WALL_TO_SWITCH * 2)/2 + 10.0;
	public static final double CROSS_COURT = HORIZONTAL_PAST_SWITCH * 2;
	public static final double MIDWAY_AUTO_TO_SCALE = WALL_TO_NULL_ZONE - MIDWAY_AUTO_LINE;
	public static final double MIDWAY_AUTO_TO_SWITCH = WALL_TO_SWITCH - MIDWAY_AUTO_LINE;
	public static final double APPROACH_SWITCH = (CROSS_COURT - SWITCH_WIDTH)/2;
	public static final double APPROACH_SWITCH_SIDE = WALL_TO_SWITCH - MIDWAY_AUTO_LINE;
	public static final double APPROACH_SCALE = 20.0;
	public static final double SWITCH_TO_SCALE = WALL_TO_NULL_ZONE - WALL_TO_SWITCH;
	public static final double SWITCH_TO_MIDWAY_SCALE = SWITCH_TO_SCALE / 2;
	public static final double CENTER_TO_SWITCH = SWITCH_WIDTH / 2 - 10.0;
	
}
