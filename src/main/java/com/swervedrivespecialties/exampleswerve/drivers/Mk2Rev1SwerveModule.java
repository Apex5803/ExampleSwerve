package com.swervedrivespecialties.exampleswerve.drivers;

import com.revrobotics.*;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.RobotController;
import org.frcteam2910.common.drivers.SwerveModule;
import org.frcteam2910.common.math.Vector2;

public class Mk2Rev1SwerveModule extends SwerveModule {
    private static final double ANGLE_TICKS_PER_RADIAN = (2.0 * Math.PI) / 18.0;
    private static final double DRIVE_TICKS_PER_INCH = 1.0 / (4.0 * Math.PI / 60.0 * 15.0 / 20.0 * 24.0 / 38.0 * 18.0); // 0.707947

    private final double angleOffset;

    private AnalogInput angleAnalogEncoder;
    private CANSparkMax angleMotor;
    private CANEncoder angleEncoder;
    private CANPIDController angleController;
    private CANSparkMax driveMotor;
    private CANEncoder driveEncoder;
    private CANPIDController driveController;

    public Mk2Rev1SwerveModule(Vector2 modulePosition, double angleOffset,
                               CANSparkMax angleMotor, CANSparkMax driveMotor, AnalogInput angleAnalogEncoder) {
        super(modulePosition);
        this.angleOffset = angleOffset;
        this.angleAnalogEncoder = angleAnalogEncoder;
        this.angleMotor = angleMotor;
        this.angleEncoder = angleMotor.getEncoder();
        this.angleController = angleMotor.getPIDController();
        this.driveMotor = driveMotor;
        this.driveEncoder = driveMotor.getEncoder();
        this.driveController = driveMotor.getPIDController();

        driveMotor.setSmartCurrentLimit(60);
        driveMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus0, 500);
        driveMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 5);
        driveMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 5);

        driveEncoder.setPositionConversionFactor(1.0 / DRIVE_TICKS_PER_INCH);
        driveEncoder.setVelocityConversionFactor(60.0 / DRIVE_TICKS_PER_INCH);

        double angle = (1.0 - angleAnalogEncoder.getVoltage() / RobotController.getVoltage5V()) * 2.0 * Math.PI + angleOffset;
        angle %= 2.0 * Math.PI;
        if (angle < 0.0) {
            angle += 2.0 * Math.PI;
        }

        angleMotor.setSmartCurrentLimit(20);
        angleMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus0, 500);
        angleMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 500);
        angleMotor.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 5);
        angleController.setP(1.5);
        angleController.setD(0.5);
        angleEncoder.setPosition(angle);
        angleEncoder.setPositionConversionFactor(ANGLE_TICKS_PER_RADIAN);
    }

    @Override
    protected double readAngle() {
        double angle = (1.0 - angleAnalogEncoder.getVoltage() / RobotController.getVoltage5V()) * 2.0 * Math.PI + angleOffset;
        angle %= 2.0 * Math.PI;
        if (angle < 0.0) {
            angle += 2.0 * Math.PI;
        }

        return angle;
    }

    @Override
    protected double readDistance() {
        return driveEncoder.getPosition();
    }

    protected double readVelocity() {
        return driveEncoder.getVelocity();
    }

    protected double readDriveCurrent() {
        return driveMotor.getOutputCurrent();
    }

    @Override
    public double getCurrentVelocity() {
        return readVelocity();
    }

    @Override
    public double getDriveCurrent() {
        return readDriveCurrent();
    }

    @Override
    protected void setTargetAngle(double angle) {
        double motorAngle = angleEncoder.getPosition();
        motorAngle %= 2.0 * Math.PI;
        if (motorAngle < 0.0) {
            motorAngle += 2.0 * Math.PI;
        }

        double newTarget = angle + angleEncoder.getPosition() - readAngle();
        if (angle - readAngle() > Math.PI) {
            newTarget -= 2.0 * Math.PI;
        } else if (angle - readAngle() < -Math.PI) {
            newTarget += 2.0 * Math.PI;
        }

        angleController.setReference(newTarget, ControlType.kPosition);
    }

    @Override
    protected void setDriveOutput(double output) {
        driveMotor.set(output);
        driveController.setReference(12.0 * output, ControlType.kVoltage);
    }
}
