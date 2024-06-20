package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;
import static com.qualcomm.hardware.rev.RevHubOrientationOnRobot.*;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class BHI260IMUImpl {

    private BHI260IMU gyro;

    public BHI260IMUImpl(BHI260IMU gyro, IMU.Parameters parameters) {
        this.gyro = gyro;
        setupGyro(parameters);
    }

    public double getHeading() {
        return gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }

    private void setupGyro(IMU.Parameters parameters) {
        gyro.initialize(parameters);
        reset();
    }

    private void reset() {
        gyro.resetYaw();
    }
}
