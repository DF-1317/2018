package org.usfirst.frc.team1317.robot;

import org.usfirst.frc.team1317.robot.subsystems.*;
import edu.wpi.first.wpilibj.Timer;

import java.util.Map;

//This is from last year's attempt at using vision to control the robot in autonomous.
//It has been slightly modified as to work with the new code
public class Targeting {
	
	//This variable is from last year's vision tracking.
	final double TargetX = 18+207/2; //x +w/2 at target 
	 
	final int TargetXError1 = 20;
	final int TargetXError2 = 13;
	final int TargetXError3 = 7;
	final double DistanceError = 0.03;
	final int WidthAtTarget = 207;
	final int WidthAtFarthestPoint = 50;
	final int FarthestPointInches = 104;
	final double ForwardSpeed = -0.2;
	final double ForwardSpeed2 = -0.09;
	final double ForwardSpeed3 = -0.04;
	final double SlidingSpeed = 0.4;
	final double TurningSpeed = 0.19;
	final double TurningCorrectionSpeed = 0.1;
	
	//numbers having to do with last year's game
	private byte liftNumber;
	final byte LeftLift = 0;
	final byte CenterLift = 1;
	final byte RightLift = 2;
	
	int CenterCounter = 0;
	Timer turnTimer = new Timer();

	int TargetXError = TargetXError1;
	
	MecanumDriveTrain driveTrain;
	Map<String,Object> currentBoundingBox;

	public Targeting()
	{
		driveTrain = Robot.mecanumDriveTrain;
		turnTimer.reset();
		turnTimer.start();
	}
	
	public void setCurrentBoundingBox(Map<String,Object> boundingBox)
	{
		if(boundingBox != null)
		{
			if((double)boundingBox.get("x")==0.0&&(double)boundingBox.get("y")==0.0 && (double)boundingBox.get("w")==0.0)
			{
				currentBoundingBox = null;
				return;
			}
			currentBoundingBox = boundingBox;
		}
	}
	
	public Boolean adjustCourse()
	{
		double turning=0.0;
		double sliding=0.0;
		double forward=0.0;
		Boolean DoneAligningX=false;
		Boolean DoneMovingForward=false;
		if (currentBoundingBox == null)
		{
			Console.show(5, "Searching for Target...");
			if(liftNumber == LeftLift)
			{
				driveTrain.driveCartesian(-0.3, -0.2, 0.0);
			}
			else if (liftNumber == RightLift)
			{
				driveTrain.driveCartesian(0.3, -0.2, 0.0);
			}
			else if (liftNumber == CenterLift)
			{
				
				/*if(CenterCounter>600)
				{
					CenterCounter=0;
				}
				else if (CenterCounter>450)
				{
					driveTrain.driveCartesian(-0.2, 0.0, 0.0);
				}
				else if(CenterCounter>150)
				{
					driveTrain.driveCartesian(0.2, 0.0, 0.0);
				}
				else
				{
					driveTrain.driveCartesian(-0.2, 0.0, 0.0);
				}
				CenterCounter++;*/
				driveTrain.driveCartesian(0.0, 0.0, 0.1);
			}
			return false;
		}
		else
		{
			double xNow = (double)currentBoundingBox.get("x") + (double)currentBoundingBox.get("w")/2.0;
			double distance = estimateDistancetoTarget();
			double xDifference = xNow-TargetX;
			Console.show(3, "xnow: "+xNow + ", distance: " + distance);
			if (distance>DistanceError)
			{
				if(distance<6.0)
				{
					TargetXError=TargetXError3;
					forward = ForwardSpeed3;
				}
				else if(distance<14.0)
				{
					TargetXError = TargetXError2;
					forward = ForwardSpeed2;
				}
				else
				{
					TargetXError = TargetXError1;
					forward=ForwardSpeed;
				}
				if(Math.abs(xDifference)>=TargetXError*2)
				{
					forward = 0.0;
				}
			}
			else
			{
				forward = 0.0;
				DoneMovingForward = true;
			}
			if(Math.abs(xDifference)<=TargetXError)
			{
				turning = 0.0;
				sliding = 0.0;
				DoneAligningX = true;
				turnTimer.reset();
				turnTimer.start();
				
			}
			else if (turnTimer.get()<0.5)
			{
				if(xNow<TargetX)
				{
					//sliding = SlidingSpeed;
					//turning = -TurningCorrectionSpeed;
					if(xDifference<-2.0*TargetXError)
						turning = -TurningSpeed;
					else
						turning = xDifference*TurningSpeed/(2.0*TargetXError);
				}
				else if (xNow>TargetX)
				{
					//sliding = -SlidingSpeed;
					//turning = TurningCorrectionSpeed;
					if(xDifference>2.0*TargetXError)
						turning = TurningSpeed;
					else
						turning = xDifference*TurningSpeed/(2.0*TargetXError);
				}
			}
			else if (turnTimer.get()>=0.8)
			{
				turnTimer.reset();
				turnTimer.start();
			}
			Console.show(3, "sliding: " + sliding + ", forward: " + forward + "turning: " + turning);
			driveTrain.driveCartesian(sliding, forward, turning);
			Console.show(4, "Aligned: " + DoneAligningX + ", Forward: " + DoneMovingForward);
			if(DoneAligningX&&DoneMovingForward)
			{
				driveTrain.driveCartesian(0.0, 0.0, 0.0);
				return true;
			}
			else
			{
				return false;
			}
		}
	}
	
	public double estimateDistancetoTarget()
	{
		return (WidthAtTarget-(double)currentBoundingBox.get("w")) /
				  (WidthAtTarget-WidthAtFarthestPoint)*FarthestPointInches;
	}
	
	public void setLiftTarget(byte target)
	{
		liftNumber = target;
	}
}
