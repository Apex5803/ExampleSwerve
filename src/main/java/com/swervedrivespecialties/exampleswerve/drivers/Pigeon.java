package com.swervedrivespecialties.exampleswerve.drivers;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import org.frcteam2910.common.drivers.Gyroscope;
import org.frcteam2910.common.math.Rotation2;

public class Pigeon extends Gyroscope {
    private final PigeonIMU pigeon;

    public Pigeon(int canId) {
        pigeon = new PigeonIMU(canId);
    }

    public Pigeon(TalonSRX talon) {
        pigeon = new PigeonIMU(talon);
    }

    @Override
    public void calibrate() {

    }

    @Override
    public Rotation2 getUnadjustedAngle() {
        return Rotation2.fromDegrees(pigeon.getFusedHeading());
    }

    @Override
    public double getUnadjustedRate() {
        return 0.0;
    }
}
