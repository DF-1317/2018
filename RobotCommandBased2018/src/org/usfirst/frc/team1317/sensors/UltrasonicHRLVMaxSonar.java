package org.usfirst.frc.team1317.sensors;

import edu.wpi.first.wpilibj.AnalogInput;

public class UltrasonicHRLVMaxSonar {
	AnalogInput sensor;
public UltrasonicHRLVMaxSonar(int port) 
{
	sensor = new AnalogInput(port);
	}
public double getdistancemm() {
	return sensor.getVoltage()*5/(4.88/1000);
}
}
