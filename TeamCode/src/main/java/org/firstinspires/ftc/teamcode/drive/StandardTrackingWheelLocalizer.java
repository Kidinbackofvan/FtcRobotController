package org.firstinspires.ftc.teamcode.drive;

import static org.firstinspires.ftc.teamcode.drive.DriveConstants.BK_ENCODER;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.LT_ENCODER;
import static org.firstinspires.ftc.teamcode.drive.DriveConstants.RT_ENCODER;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.util.Encoder;

import java.util.Arrays;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
@Config
public class StandardTrackingWheelLocalizer extends ThreeTrackingWheelLocalizer {
    // TODO: Physical characteristics of dead-wheel
    /*
     * GOBILDA Swingarm Odometry Pod: 3110-0001-0001
     * Wheel Durometer: 50A
     * 43mm width, 48mm diameter
     * 2000 Countable Events per Revolution
     * https://www.gobilda.com/swingarm-odometry-pod-48mm-wheel/
     * https://learnroadrunner.com/dead-wheels.html#three-wheel-odometry
     * https://learnroadrunner.com/dead-wheels.html#tuning-three-wheel
     */
    public static double TICKS_PER_REV = 2000;
    public static double WHEEL_RADIUS = 0.94; // in
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    // Offsets for dead-wheel points of contact
    public static double LATERAL_DISTANCE = 12.6; // in; distance between the left and right wheels
    public static double FORWARD_OFFSET = -7.36; // in; offset of the lateral wheel
    // Perpendicular wheel is in the back, so use negative number...
    // TODO: Done (9/2/24) - Physical positioning of dead-wheel points of contact
    //  (inches from center of rotation)
    //
    //     |  |               FRONT              |  |
    //  == |  |                                  |  | ==
    //  == |  |                 ^                |  | ==
    //  == |  |                /|\               |  | ==
    //  == |  |                 |                |  | ==
    //     |  |                +x                |  |
    //     |  |                 |                |  |
    //     |  |                 | (0,0)          |  |
    //     |  |  <---- -y ------o----- +y ---->  |  |
    //     |  |                 |                |  |
    //     |==|                 |                |==|
    //     |==|                -x                |==|
    //     |  |                 |                |  |
    //  == |  |                \|/               |  | ==
    //  == |  |                 v                |  | ==
    //  == |  |__________________________________|  | ==
    //  == |  |               (==)               |  | ==
    //     |  |__________________________________|  |

    public static double X_MULTIPLIER = 1; // Multiplier in the X direction
    public static double Y_MULTIPLIER = 1; // Multiplier in the Y direction

    private Encoder leftEncoder, rightEncoder, frontEncoder;

    private List<Integer> lastEncPositions, lastEncVels;

    public StandardTrackingWheelLocalizer(HardwareMap hardwareMap, List<Integer> lastTrackingEncPositions, List<Integer> lastTrackingEncVels) {
        super(Arrays.asList(
                new Pose2d(0, LATERAL_DISTANCE / 2, 0), // left
                new Pose2d(0, -LATERAL_DISTANCE / 2, 0), // right
                new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
        ));

        lastEncPositions = lastTrackingEncPositions;
        lastEncVels = lastTrackingEncVels;

        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, LT_ENCODER));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, RT_ENCODER));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, BK_ENCODER));

        // TODO: reverse any encoders using Encoder.setDirection(Encoder.Direction.REVERSE)
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        int leftPos = leftEncoder.getCurrentPosition();
        int rightPos = rightEncoder.getCurrentPosition();
        int frontPos = frontEncoder.getCurrentPosition();

        lastEncPositions.clear();
        lastEncPositions.add(leftPos);
        lastEncPositions.add(rightPos);
        lastEncPositions.add(frontPos);

        return Arrays.asList(
                encoderTicksToInches(leftPos) * X_MULTIPLIER,
                encoderTicksToInches(rightPos) * X_MULTIPLIER,
                encoderTicksToInches(frontPos) * Y_MULTIPLIER
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        int leftVel = (int) leftEncoder.getCorrectedVelocity();
        int rightVel = (int) rightEncoder.getCorrectedVelocity();
        int frontVel = (int) frontEncoder.getCorrectedVelocity();

        lastEncVels.clear();
        lastEncVels.add(leftVel);
        lastEncVels.add(rightVel);
        lastEncVels.add(frontVel);

        return Arrays.asList(
                encoderTicksToInches(leftVel) * X_MULTIPLIER,
                encoderTicksToInches(rightVel) * X_MULTIPLIER,
                encoderTicksToInches(frontVel) * Y_MULTIPLIER
        );
    }
}
