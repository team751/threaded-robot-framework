package org.team751.dev;


import org.team751.framework.RobotTask;
import org.team751.framework.SharedData;
import org.team751.framework.ThreadedRobot;
import org.team751.framework.util.Key;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

public class FrameworkTestRobot extends ThreadedRobot {
	
	/**
	 * Keys to access the 4 supported joysticks.
	 * They are in the order defined by the driver station.
	 */
	public static final Key[] joysticks = Key.createSet(4);
	
	/**
	 * Key for the left drivetrain jaguar
	 */
	public static final Key driveLeftController = new Key();
	
	/**
	 * Key for the right drivetrain jaguar
	 */
	public static final Key driveRightController = new Key();
    
    public void robotInit() {
    	
    	//Create shared objects and put them into the shared data
    	for(int i = 0; i < joysticks.length; i++){
    		SharedData.put(joysticks[i], new Joystick(i + 1));
    	}
    	
    	SharedData.put(driveLeftController, new Jaguar(1));
    	SharedData.put(driveRightController, new Jaguar(2));
    	
    	//Add drive task
    	addTask(new RobotTask() {

    		
			protected void teleop() throws InterruptedException {
				
				//Get a reference to joystick zero
				Joystick joystick = (Joystick) SharedData.get(joysticks[0]);
				//Get values from the joystick
				double x = joystick.getX();
				double y = joystick.getY();
				//Done using the joystick; release it so that other tasks can use it
				SharedData.release(joysticks[0]);
				
				SpeedController left = (SpeedController) SharedData.get(driveLeftController);
				SpeedController right = (SpeedController) SharedData.get(driveRightController);
				RobotDrive drive = new RobotDrive(left, right);
				drive.arcadeDrive(y, x);
				
				SharedData.release(driveLeftController);
				SharedData.release(driveRightController);
				
				sleep((long) (kMinTaskTime * 1000));
			}
    		
    	});
    	
    	addTask(new RobotTask() {
			protected void teleop() throws InterruptedException {
				System.out.println("Teleoperated! "+DriverStation.getInstance().getMatchTime());
				sleep(2000);
			}
    		
    	});
    }
    
}
