package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.subsystem.MecanumDrivebaseSubsystem;

public class GyroWrapper {
    private BHI260IMU gyro;

    public GyroWrapper(BHI260IMU gyro) {
        this.gyro = gyro;
        setupGyro();
    }

    public double getHeading() {
        return gyro.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
    }
    private void setupGyro() {
        IMU.Parameters parameters = new IMU.Parameters(
            new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
                RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
            )
        );
        gyro.initialize(parameters);
//        resetGyro();

    }

    private void reset() {
//        gyro.zeroYaw();
    }
}
