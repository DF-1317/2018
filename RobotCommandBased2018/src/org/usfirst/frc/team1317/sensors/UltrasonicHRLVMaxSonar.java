package org.usfirst.frc.team1317.sensors;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

/**
 * Represents an analog ultrasonic sensor, specifically MB1013-000, which reads 5mm/5mV
 * @author Digital Fusion
 *
 */
public class UltrasonicHRLVMaxSonar extends SensorBase implements PIDSource {
	
	protected PIDSourceType m_pidSource = PIDSourceType.kDisplacement;
	/**
	 * represents the units the sesnor can use
	 *
	 */
	public enum Unit
	{
		kInches,
		kMillimeters
	}
	
	Unit units = Unit.kMillimeters;
	AnalogInput sensor;
	/**
	 * 
	 * @param port the analog port the ultrasonic sensor is plugged into
	 */
	public UltrasonicHRLVMaxSonar(int port) {
		sensor = new AnalogInput(port);
	}
	
	/**
	 * 
	 * @param port the analog port the ultrasonic sensor is plugged into
	 * @param units the units the class will use by default
	 */
	public UltrasonicHRLVMaxSonar(int port, Unit units) {
		sensor = new AnalogInput(port);
		setUnit(units);
	}
	/**
	 * 
	 * @return the range the detected in millimeters
	 */
	public double getRangeMM() {
		return sensor.getVoltage()*5/(4.88/1000);
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
		if(units == Unit.kInches) {
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
	public void setUnit(Unit unit)
	{
		units = unit;
	}
	
	/**
	 * 
	 * @return the current default unit
	 */
	public Unit getUnits() {
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
