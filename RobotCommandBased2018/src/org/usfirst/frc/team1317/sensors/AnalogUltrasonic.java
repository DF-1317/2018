package org.usfirst.frc.team1317.sensors;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

//much of this class is based on the class edu.wpi.first.wpilibj.Ultrasonic

/**
 * Represents an analog ultrasonic sensor, specifically MB1013-000, which reads 5mm/5mV
 * @author Digital Fusion
 *
 */
public class AnalogUltrasonic extends AnalogInput implements PIDSource {
	
	protected PIDSourceType m_pidSource = PIDSourceType.kDisplacement;
	//in mm/mV
	protected double scaleFactor = 5/4.88;
	
	
	Ultrasonic.Unit units = Ultrasonic.Unit.kMillimeters;
	AnalogInput sensor;
	/**
	 * 
	 * @param port the analog port the ultrasonic sensor is plugged into
	 */
	public AnalogUltrasonic(int port) {
		super(port);
	}
	
	/**
	 * 
	 * @param port the analog port the ultrasonic sensor is plugged into
	 * @param units the units the class will use by default
	 */
	public AnalogUltrasonic(int port, Ultrasonic.Unit units) {
		super(port);
		setUnit(units);
	}
	
	/**
	 * Sets the scale factor for converting the voltage of the sensor to distance in mm/mV
	 * 
	 * @param factor - How many millimeters one millivolt of sensor data represents 
	 */
	public void setScaleFactor(double factor)
	{
		scaleFactor = factor;
	}
	
	/**
	 * gets the current scale factor set
	 * @return the current scale factor in mm/mV
	 */
	public double getScaleFactor()
	{
		return scaleFactor;
	}
	
	/**
	 * 
	 * @return the range the detected in millimeters
	 */
	public double getRangeMM() {
		return getVoltage()*1000*scaleFactor;
	}
	
	/**
	 * 
	 * @return the range detected in millimeters
	 */
	public double getRangeInches() {
		return getRangeMM()/25.4;
	}
	
	/**
	 * 
	 * @return the range in the default units
	 */
	public double getRange() {
		if(units == Ultrasonic.Unit.kInches) {
			return getRangeInches();
		}
		else {
			return getRangeMM();
		}
	}
	/**
	 * Sets the default units for use in <code>get()</code> and <code>pidGet</code>
	 * @param unit the default unit to set
	 */
	public void setUnit(Ultrasonic.Unit unit)
	{
		units = unit;
	}
	
	/**
	 * 
	 * @return the current default unit
	 */
	public Ultrasonic.Unit getUnits() {
		return units;
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		builder.setSmartDashboardType("Ultrasonic");
	    builder.addDoubleProperty("Value", this::getRangeMM, null);
	}

	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		if (!pidSource.equals(PIDSourceType.kDisplacement)) {
		      throw new IllegalArgumentException("Only displacement PID is allowed for ultrasonics.");
		    }
		    m_pidSource = pidSource;
	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return m_pidSource;
	}

	@Override
	public double pidGet() {
		switch (units) {
	      case kInches:
	        return getRangeInches();
	      case kMillimeters:
	        return getRangeMM();
	      default:
	        return 0.0;
	    }
	}

}
