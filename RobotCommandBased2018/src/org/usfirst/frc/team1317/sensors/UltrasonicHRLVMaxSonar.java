package org.usfirst.frc.team1317.sensors;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;

public class UltrasonicHRLVMaxSonar extends SensorBase {
	
	AnalogInput sensor;
	
	public UltrasonicHRLVMaxSonar(int port) {
		sensor = new AnalogInput(port);
	}
	
	public double getRangeMM() {
		return sensor.getVoltage()*5/(4.88/1000);
	}

	@Override
	public void initSendable(SendableBuilder builder) {
		builder.setSmartDashboardType("Ultrasonic");
	    builder.addDoubleProperty("Value", this::getRangeMM, null);
	}
}
