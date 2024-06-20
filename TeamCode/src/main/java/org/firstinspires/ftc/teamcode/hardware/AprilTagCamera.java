package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.teamcode.command.Command;
import org.firstinspires.ftc.teamcode.command.InstantCommand;
import org.firstinspires.ftc.teamcode.command.SequentialCommandGroup;
import org.firstinspires.ftc.teamcode.command.WaitUntilCommand;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AprilTagCamera {
    private VisionPortal visionPortal;
    private AprilTagProcessor processor;

    private float decimation = 2;

    private final HardwareMap hardwareMap;
    private final String webcamName;

    private boolean isTagFound = false;
    private boolean isInitialized = false;

    private AprilTagDetection detectedTag = null;

    public AprilTagCamera(HardwareMap hardwareMap, String webcamName) {
        this.hardwareMap = hardwareMap;
        this.webcamName = webcamName;
    }

    public AprilTagDetection getDetectedTag() {
        return detectedTag;
    }

    /** Keep detecting a tag until a tag is found. */
    public Command detectTag(int desiredTag, Runnable detectionAction) {
        if (!isInitialized) return null;

        List<AprilTagDetection> currentDetections = processor.getDetections();

        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                if ((desiredTag < 0) || (detection.id == desiredTag)) {
                    isTagFound = true;
                    detectedTag = detection;
                }
            }
        }
//        return new SequentialCommandGroup(new WaitUntilCommand(() -> isTagFound, true))
        return new InstantCommand(() -> System.out.println("test"));
    }

    public boolean isTagFound() {
        return isTagFound;
    }

    public void init() {
        processor = new AprilTagProcessor.Builder().build();
        processor.setDecimation(decimation);

        visionPortal = new VisionPortal.Builder()
                .setCamera(hardwareMap.get(WebcamName.class, webcamName))
                .addProcessor(processor)
                .build();

        setManualExposure(6, 250);
        isInitialized = true;
    }

    private void setManualExposure(int exposureMS, int gain) {
        // Make sure the camera is open first.
        if (visionPortal == null) return;

        // Make sure the camera is streaming.
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) return;

        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        exposureControl.setMode(ExposureControl.Mode.Manual);

        exposureControl.setExposure(exposureMS, TimeUnit.MILLISECONDS);
//        Thread.sleep(20);
        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(gain);
//        Thread.sleep(20);
    }
}
