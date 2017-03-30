package org.usfirst.frc.team1279.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public abstract class DriveTrain {

	public RobotDrive drive;

	double throttleScale = 1;
	double turnScale = -1;
	
	abstract public void encoderDistance(double speed, double distance, Vision vision, double timeout);
	abstract public void encoderTurn(double speed, double distance, double timeout);
	abstract public void driveUntilDigital(double speed, Vision vision, DigitalInput input, double timeout);
	
	abstract public void resetEncoders();
	abstract public int getAverageEncoders();

	public void setReversed(boolean reverse) {
		if (reverse) {
			throttleScale = -Math.abs(throttleScale);
		} else {
			throttleScale = Math.abs(throttleScale);
		}
	}

	public boolean getReversed() {
		return throttleScale < 0;
	}

	public void drive(double throttle, double turn) {
		drive.arcadeDrive(throttle * throttleScale, turn * turnScale);
		//System.out.println("Driving: " + throttle + ":" + turn);
	}
	
}
