package org.usfirst.frc.team1317.robot.commands;

import org.usfirst.frc.team1317.robot.Robot;
import org.usfirst.frc.team1317.robot.navigation.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;


/**
 * This command causes the robot to drive a certain amount of inches over a certain distance at a certain heading.
 * 
 * This class should allow the robot to travel in arcs if the turning speed is low and the robot starts at a different angle then the heading angle.
 * 
 */
public class DriveInchesPID extends Command implements PIDSource {

	PIDSourceType m_pidSource = PIDSourceType.kDisplacement;
	
	//distance constants
	public static final double kP = 0.01;
	public static final double kI = 0.0;
	public static final double kD = 0.0;
	public static final double kF = 0.0;
	public static final double inchTolerance = 1.0;
	
	//steering controller constants
	public static final double sP = 0.02;
	public static final double sI = 0.0;
	public static final double sD = 0.0;
	public static final double sF = 0.0;
	public static final double degreeTolerance = 1.0;
	
	PIDController distanceController;
	PIDController steeringController;
	
	AutonomousDrivingController driver;
	AutonomousTurningController turner;
	
	Encoder LeftEncoder;
	Encoder RightEncoder;
	
	double distanceSetpoint;
	double headingSetpoint;
	
	/**
	 * constructs this command
	 * 
	 * @param inches - the distance the robot is to drive in inches
	 * @param heading - the heading the robot should be at by the end of the command
	 * @param drivingSpeed - the speed the robot should drive at
	 * @param turningSpeed - the speed the robot should turn at
	 */
    public DriveInchesPID(double inches, double heading, double drivingSpeed, double turningSpeed) {
    	super("DriveInchesPID");
        requires(Robot.mecanumDriveTrain);
        LeftEncoder = Robot.mecanumDriveTrain.BLEncoder;
        RightEncoder = Robot.mecanumDriveTrain.BREncoder;
        turner = new AutonomousTurningController();
        steeringController = new PIDController(sP,sI,sD,sF,Robot.mecanumDriveTrain.navX,turner);
        steeringController.setInputRange(-180.0, 180.0);
        steeringController.setContinuous(true);
        steeringController.setOutputRange(-1.0, 1.0);
        steeringController.setAbsoluteTolerance(degreeTolerance);
        headingSetpoint = heading;
        driver = new AutonomousDrivingController(turner, drivingSpeed, turningSpeed);
        distanceController = new PIDController(kP,kI,kD,kF,this,driver);
        distanceController.setContinuous(false);
        distanceController.setOutputRange(-1.0, 1.0);
        distanceController.setAbsoluteTolerance(inchTolerance);
        distanceSetpoint = inches;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	LeftEncoder.reset();
    	RightEncoder.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	steeringController.setSetpoint(headingSetpoint);
    	steeringController.enable();
    	distanceController.setSetpoint(distanceSetpoint);
    	distanceController.enable();
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return (distanceController.onTarget()&&steeringController.onTarget());
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	steeringController.disable();
    	distanceController.disable();
    	Robot.mecanumDriveTrain.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	steeringController.disable();
    	distanceController.disable();
    	Robot.mecanumDriveTrain.stop();
    }

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		if (!pidSource.equals(PIDSourceType.kDisplacement)) {
		      throw new IllegalArgumentException("Only displacement PID is allowed for this class.");
		}
    	m_pidSource = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		return m_pidSource;
	}

	@Override
	public double pidGet() {
		return (LeftEncoder.getDistance() + RightEncoder.getDistance())/2;
	}
}
