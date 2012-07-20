package org.team751.dev;

import org.team751.framework.RobotTask;
import org.team751.framework.SharedData;
import org.team751.framework.ThreadedRobot;
import org.team751.framework.util.Key;
import org.team751.inav.INavDataProvider;
import org.team751.inav.InertialNavigationTask;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.SpeedController;

public class FrameworkTestRobot extends ThreadedRobot {

	/**
	 * Keys to access the 4 supported joysticks. They are in the order defined
	 * by the driver station.
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

	public static final Key accelerometer = new Key();

	public static final Key gyro = new Key();

	public void robotInit() {

		// Create shared objects and put them into the shared data
		for (int i = 0; i < joysticks.length; i++) {
			SharedData.put(joysticks[i], new Joystick(i + 1));
		}

		SharedData.put(driveLeftController, new Jaguar(1));
		SharedData.put(driveRightController, new Jaguar(2));

		// Set up sensors
		SharedData.put(accelerometer,
				new ADXL345_I2C(SensorBase.getDefaultDigitalModule(),
						ADXL345_I2C.DataFormat_Range.k4G));
		SharedData.put(gyro, new Gyro(1));

		addTask(new InertialNavigationTask(new INavDataProvider() {

			public double getHeading() throws InterruptedException {
				double heading = 0;
				Gyro gyroscope = (Gyro) SharedData.get(gyro);
				try {
					heading = gyroscope.getAngle();
				} finally {
					SharedData.release(gyro);
				}
				return heading;
			}

			public double getAcceleration() throws InterruptedException {
				
				double acceleration = 0;
				
				ADXL345_I2C accel = (ADXL345_I2C) SharedData.get(accelerometer);
				try {
					acceleration = accel
							.getAcceleration(ADXL345_I2C.Axes.kY);
				} finally {
					SharedData.release(accelerometer);
				}

				return acceleration;
			}

		}));

		// Add drive task
		addTask(new RobotTask() {

			protected void teleop() throws InterruptedException {
				
				double x = 0, y = 0;

				// Get a reference to joystick zero
				Joystick joystick = (Joystick) SharedData.get(joysticks[0]);
				try {
					// Get values from the joystick
					x = joystick.getX();
					y = joystick.getY();

				} finally {
					// Done using the joystick; release it so that other tasks
					// can
					// use it
					SharedData.release(joysticks[0]);
				}

				SpeedController left = (SpeedController) SharedData
						.get(driveLeftController);
				SpeedController right = (SpeedController) SharedData
						.get(driveRightController);
				RobotDrive drive = new RobotDrive(left, right);
				drive.arcadeDrive(y, x);

				SharedData.release(driveLeftController);
				SharedData.release(driveRightController);

				sleep((long) (kMinTaskTime * 1000));
			}

		});

		addTask(new RobotTask() {
			protected void teleop() throws InterruptedException {
				System.out.println("Teleoperated! "
						+ DriverStation.getInstance().getMatchTime());
				sleep(2000);
			}

		});
	}

}
