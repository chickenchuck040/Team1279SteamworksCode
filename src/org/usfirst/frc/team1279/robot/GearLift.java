package org.usfirst.frc.team1279.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class GearLift {

	public static final double UP_VOLTAGE = 12;
	public static final double DOWN_VOLTAGE = -12;

	public static final int TALON_MAX_CURRENT = 18; //15;
	public static final double SOFT_MAX_CURRENT = 18; //15;

	public CANTalon masterTalon;
	public CANTalon slaveTalon;

	GearClaw gearClaw;

	private NetworkTable robotTable;

	public GearLift(GearClaw claw, int master, int slave, NetworkTable robotTable) {
		gearClaw = claw;

		masterTalon = new CANTalon(master);
		masterTalon.changeControlMode(TalonControlMode.Voltage);
		masterTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		masterTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		masterTalon.configPeakOutputVoltage(+12.0f, -12.0f);
		masterTalon.ConfigFwdLimitSwitchNormallyOpen(false);
		masterTalon.ConfigFwdLimitSwitchNormallyOpen(true);
		masterTalon.setCurrentLimit(TALON_MAX_CURRENT);
		masterTalon.EnableCurrentLimit(true);

		slaveTalon = new CANTalon(slave);
		slaveTalon.changeControlMode(TalonControlMode.Follower);
		slaveTalon.set(master);
		slaveTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		slaveTalon.configPeakOutputVoltage(+12.0f, -12.0f);
		slaveTalon.setCurrentLimit(TALON_MAX_CURRENT);
		slaveTalon.EnableCurrentLimit(true);

		this.robotTable = robotTable;
	}

	public GearLift(GearClaw claw, int master, NetworkTable robotTable) {
		gearClaw = claw;

		masterTalon = new CANTalon(master);
		masterTalon.changeControlMode(TalonControlMode.Voltage);
		masterTalon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		masterTalon.configNominalOutputVoltage(+0.0f, -0.0f);
		masterTalon.configPeakOutputVoltage(+12.0f, -12.0f);
		masterTalon.setCurrentLimit(TALON_MAX_CURRENT);
		masterTalon.EnableCurrentLimit(true);

		this.robotTable = robotTable;
	}

	public void periodic() {
		
		if(slaveTalon != null) robotTable.putNumber("slaveliftcurrent", slaveTalon.getOutputCurrent());

		if (masterTalon.getOutputCurrent() >= SOFT_MAX_CURRENT) {
			this.stopGear();
		}

		if (slaveTalon != null && slaveTalon.getOutputCurrent() >= SOFT_MAX_CURRENT) {
			this.stopGear();
		}

		if (masterTalon.isFwdLimitSwitchClosed() || masterTalon.isRevLimitSwitchClosed()) {
			//this.stopGear();
		}
		
		gearClaw.down = isUp();
		gearClaw.up = isDown();

		robotTable.putNumber("masterliftcurrent", masterTalon.getOutputCurrent());
		robotTable.putBoolean("liftup", isUp());
		robotTable.putBoolean("liftdown", isDown());
		
	}
	
	public boolean isUp(){
		return !masterTalon.isFwdLimitSwitchClosed();
	}
	
	public boolean isDown(){
		return masterTalon.isRevLimitSwitchClosed();
	}

	public boolean raiseGear() {
		if (gearClaw.isClosedEnough()) {
			masterTalon.set(UP_VOLTAGE);
			return true;
		} else {
			return false;
		}

	}

	public boolean lowerGear() {
		if (gearClaw.isClosedEnough()) {
			masterTalon.set(DOWN_VOLTAGE);
			return true;
		} else {
			return false;
		}

	}

	public void stopGear() {
		masterTalon.set(0);
		// Some extra safety
		if(slaveTalon != null) slaveTalon.set(0);
	}

	public boolean driveGear(double amount) {
		if (gearClaw.isClosedEnough()) {
			if (amount > 0) {
				masterTalon.set(amount * UP_VOLTAGE);
			} else {
				masterTalon.set(-amount * DOWN_VOLTAGE);
			}
			return true;
		} else {
			return false;
		}
	}

}
