package org.firstinspires.ftc.teamcode.subsystem;


import com.qualcomm.hardware.bosch.BHI260IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.teamcode.command.Command;
import org.firstinspires.ftc.teamcode.command.ConditionalRunCommand;
import org.firstinspires.ftc.teamcode.command.InstantCommand;
import org.firstinspires.ftc.teamcode.command.RunCommand;
import org.firstinspires.ftc.teamcode.hardware.BHI260IMUImpl;

import java.util.function.DoubleSupplier;

public class MecanumDrivebase extends SubsystemBase {
    private final DcMotorEx[] motors;
    private boolean fieldCentric;
    private BHI260IMUImpl gyro;
    private Subsystem[] requirements;

    public MecanumDrivebase(DcMotorEx frontLeftMotor, DcMotorEx frontRightMotor, DcMotorEx rearLeftMotor, DcMotorEx rearRightMotor,
                            BHI260IMU gyro, IMU.Parameters parameters, Subsystem... requirements) {
        motors = new DcMotorEx[]{frontLeftMotor, frontRightMotor, rearLeftMotor, rearRightMotor};
        this.gyro = new BHI260IMUImpl(gyro, parameters);
        this.requirements = requirements;
        resetEncoders();
    }

    private void resetEncoders() {
        for (DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }

    private void driveFieldRelative(double forward, double strafe, double rotate) {
        double gyroRadians = Math.toRadians(-gyro.getHeading());
        double _forward = strafe * Math.sin(gyroRadians) + forward * Math.cos(gyroRadians);
        double _strafe = strafe * Math.cos(gyroRadians) - forward * Math.sin(gyroRadians);

        motors[0].setPower(_forward - _strafe + rotate);
        motors[1].setPower(_forward - _strafe - rotate);
        motors[2].setPower(_forward + _strafe + rotate);
        motors[3].setPower(_forward + _strafe - rotate);
    }

    private void driveRobotRelative(double forward, double strafe, double rotate) {
        motors[0].setPower(forward - strafe + rotate);
        motors[1].setPower(forward - strafe - rotate);
        motors[2].setPower(forward + strafe + rotate);
        motors[3].setPower(forward + strafe - rotate);
    }

    public Command driveCommand(DoubleSupplier forwardSupplier, DoubleSupplier strafeSupplier, DoubleSupplier rotateSupplier) {
        return new ConditionalRunCommand(
                driveFieldRelativeCommand(forwardSupplier, strafeSupplier, rotateSupplier),
                driveRobotRelativeCommand(forwardSupplier, strafeSupplier, rotateSupplier),
                () -> fieldCentric, requirements);
    }

    public Command driveFieldRelativeCommand(DoubleSupplier forwardSupplier, DoubleSupplier strafeSupplier, DoubleSupplier rotateSupplier) {
        return new RunCommand(() -> driveFieldRelative(forwardSupplier.getAsDouble(), strafeSupplier.getAsDouble(), rotateSupplier.getAsDouble()), requirements);
    }

    public Command driveRobotRelativeCommand(DoubleSupplier forwardSupplier, DoubleSupplier strafeSupplier, DoubleSupplier rotateSupplier) {
        return new RunCommand(() -> driveRobotRelative(forwardSupplier.getAsDouble(), strafeSupplier.getAsDouble(), rotateSupplier.getAsDouble()), requirements);
    }

    public Command setFieldCentricCommand(boolean fieldCentric) {
        return new InstantCommand(() -> this.fieldCentric = fieldCentric);
    }

    public Command resetGyro() {
        return new InstantCommand(() -> gyro.reset());
    }
}
