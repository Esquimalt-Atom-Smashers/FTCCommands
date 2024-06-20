package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.command.Command;
import org.firstinspires.ftc.teamcode.hardware.AprilTagCamera;
import org.firstinspires.ftc.teamcode.subsystem.SubsystemBase;

public class TestingOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        AprilTagCamera camera = new AprilTagCamera(hardwareMap, "Webcam 1");
        camera.init();

    }
}

class WebcamSubsystem extends SubsystemBase {
    AprilTagCamera camera;

    public WebcamSubsystem(HardwareMap map) {
        camera = new AprilTagCamera(map, "Elliot stinks");
        camera.init();
    }

//    public Command detectTag(int tag) {
//        return camera.detectTag(tag);
//    }

}