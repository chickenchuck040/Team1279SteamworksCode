package org.usfirst.frc.team1279.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Vision implements Constants{

	public static final String TABLE = "RaspberryPi";

	public static final String CAMERA_KEY = "camera";
	public static final int PI_CAMERA = 0;
	public static final int USB_CAMERA = 1;

	public static final String PROCESS_KEY = "process";
	public static final int NO_PROCESSING = 0;
	public static final int GEAR_SINGLE_PROCESSING = 1;
	public static final int GEAR_CONTINUOS_PROCESSING = 2;
	public static final int GEAR_MANUAL_PROCESSING = 3;
	public static final int CLIMBER_PROCESSING = 4;
	
	public static final String TURN_KEY = "turn";
	public static final double TURN_AMOUNT = 1;
	public static final double TURN_ERROR = 0.01;
	
	public static final String LOCK_KEY = "lock";
	
	public static final String DISTANCE_KEY = "distance";
	
	public static final String RECORD_KEY = "record";

	public NetworkTable table;

	public Vision() {

	}

	public void init() {
		table = NetworkTable.getTable(TABLE);
	}

	public void setCamera(int camera) {
		table.putNumber(CAMERA_KEY, camera);
	}
	
	public int getCamera(){
		return (int) table.getNumber(CAMERA_KEY, USB_CAMERA);
	}
	
	public void flipCamera(){
		if(getCamera() == PI_CAMERA){
			setCamera(USB_CAMERA);
		}else{
			setCamera(PI_CAMERA);
		}
	}
	
	public int getProcess(){
		return (int) table.getNumber(PROCESS_KEY, NO_PROCESSING);
	}
	
	public void setProcess(int process){
		table.putNumber(PROCESS_KEY, process);
	}
	
	public double getTurn(){
		if(getLock()){
			return table.getNumber("turn", 0)*VISION_TURN_K;
		}else{
			return 0;
		}
	}
	
	public double getDistance(){
		return table.getNumber(DISTANCE_KEY, 0);
	}
	
	public boolean getLock(){
		return table.getBoolean(LOCK_KEY, false);
	}
	
	public void setRecord(boolean record){
		table.putBoolean(RECORD_KEY, record);
	}
}
