package com.swervedrivespecialties.exampleswerve.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel;
import com.swervedrivespecialties.exampleswerve.RobotMap;
// import com.swervedrivespecialties.exampleswerve.Drivers.Pigeon;
import com.swervedrivespecialties.exampleswerve.commands.DriveCommand;

import edu.wpi.first.wpilibj.AnalogInput;
// import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frcteam2910.common.drivers.Gyroscope;
import org.frcteam2910.common.drivers.SwerveModule;
import org.frcteam2910.common.math.Vector2;
import org.frcteam2910.common.robot.drivers.Mk2SwerveModuleBuilder;
// import org.frcteam2910.common.robot.drivers.Mk2SwerveModuleBuilder.MotorType;

import com.swervedrivespecialties.exampleswerve.drivers.*;

public class DrivetrainSubsystem extends Subsystem {
    private static final double TRACKWIDTH = 28.5;// 28.5
    private static final double WHEELBASE = 17.375; //17.375

    private static final double FRONT_LEFT_ANGLE_OFFSET = -Math.toRadians(7.11);
    private static final double FRONT_RIGHT_ANGLE_OFFSET = -Math.toRadians(68.00);
    private static final double BACK_LEFT_ANGLE_OFFSET = -Math.toRadians(292.00);
    private static final double BACK_RIGHT_ANGLE_OFFSET = -Math.toRadians(69.15);
    private CANSparkMax FrontLeftAngleMotor;
    private CANSparkMax FrontLeftDriveMotor;
    private CANSparkMax FrontRightAngleMotor;
    private CANSparkMax FrontRightDriveMotor;
    private CANSparkMax BackLeftAngleMotor;
    private CANSparkMax BackLeftDriveMotor;
    private CANSparkMax BackRightAngleMotor;
    private CANSparkMax BackRightDriveMotor;


    private static DrivetrainSubsystem instance;
    public boolean fieldOriented;

    
    private final SwerveModule frontRightModule = new Mk2SwerveModuleBuilder(
        new Vector2(TRACKWIDTH / 2.0, -WHEELBASE / 2.0))
        .angleEncoder(new AnalogInput(RobotMap.DRIVETRAIN_FRONT_RIGHT_ANGLE_ENCODER), FRONT_RIGHT_ANGLE_OFFSET)
        .angleMotor(FrontRightAngleMotor = new CANSparkMax(RobotMap.DRIVETRAIN_FRONT_RIGHT_ANGLE_MOTOR, MotorType.kBrushless)
        )
        .driveMotor(FrontRightDriveMotor = new CANSparkMax(RobotMap.DRIVETRAIN_FRONT_RIGHT_DRIVE_MOTOR, MotorType.kBrushless)
        )
        .build();
    private final SwerveModule frontLeftModule = new Mk2SwerveModuleBuilder(
            new Vector2(TRACKWIDTH / 2.0, WHEELBASE / 2.0))
            .angleEncoder(new AnalogInput(RobotMap.DRIVETRAIN_FRONT_LEFT_ANGLE_ENCODER), FRONT_LEFT_ANGLE_OFFSET)
            .angleMotor(FrontLeftAngleMotor = new CANSparkMax(RobotMap.DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR, MotorType.kBrushless)
                    )
            .driveMotor(FrontLeftDriveMotor = new CANSparkMax(RobotMap.DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR, MotorType.kBrushless)
             )
            .build();
    
    private final SwerveModule backLeftModule = new Mk2SwerveModuleBuilder(
            new Vector2(-TRACKWIDTH / 2.0, WHEELBASE / 2.0))
            .angleEncoder(new AnalogInput(RobotMap.DRIVETRAIN_BACK_LEFT_ANGLE_ENCODER), BACK_LEFT_ANGLE_OFFSET)
            .angleMotor(BackLeftAngleMotor = new CANSparkMax(RobotMap.DRIVETRAIN_BACK_LEFT_ANGLE_MOTOR, MotorType.kBrushless)
            )
            .driveMotor(BackLeftDriveMotor = new CANSparkMax(RobotMap.DRIVETRAIN_BACK_LEFT_DRIVE_MOTOR, MotorType.kBrushless)
            )
            .build();
    private final SwerveModule backRightModule = new Mk2SwerveModuleBuilder(
            new Vector2(-TRACKWIDTH / 2.0, -WHEELBASE / 2.0))
            .angleEncoder(new AnalogInput(RobotMap.DRIVETRAIN_BACK_RIGHT_ANGLE_ENCODER), BACK_RIGHT_ANGLE_OFFSET)
            .angleMotor(BackRightAngleMotor = new CANSparkMax(RobotMap.DRIVETRAIN_BACK_RIGHT_ANGLE_MOTOR, MotorType.kBrushless)
            )
            .driveMotor(BackRightDriveMotor = new CANSparkMax(RobotMap.DRIVETRAIN_BACK_RIGHT_DRIVE_MOTOR, MotorType.kBrushless)
            )
            .build();

    private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
            new Translation2d(TRACKWIDTH / 2.0, WHEELBASE / 2.0),
            new Translation2d(TRACKWIDTH / 2.0, -WHEELBASE / 2.0),
            new Translation2d(-TRACKWIDTH / 2.0, WHEELBASE / 2.0),
            new Translation2d(-TRACKWIDTH / 2.0, -WHEELBASE / 2.0)
    );

    public final Gyroscope gyroscope = new NavX(1);

    public DrivetrainSubsystem() {
        gyroscope.calibrate();      //TODO set inverted or not as necessary
        gyroscope.setInverted(true); // You might not need to invert the gyro 


        // FrontLeftAngleMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 35, 0.2));
        // FrontLeftAngleMotor.setNeutralMode(NeutralMode.Brake);
        // FrontLeftDriveMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 35, 0.2));
        // FrontLeftDriveMotor.setNeutralMode(NeutralMode.Coast);
        FrontLeftDriveMotor.setInverted(false);
        FrontRightDriveMotor.setInverted(true);
        // FrontRightAngleMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 35, 0.2));
        // FrontRightAngleMotor.setNeutralMode(NeutralMode.Brake);
        // FrontRightDriveMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 35, 0.2));
        // FrontRightDriveMotor.setNeutralMode(NeutralMode.Coast);
        // BackLeftAngleMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 35, 0.2));
        // BackLeftAngleMotor.setNeutralMode(NeutralMode.Brake);
        // BackLeftDriveMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 35, 0.2));
        // BackLeftDriveMotor.setNeutralMode(NeutralMode.Coast);
        BackLeftDriveMotor.setInverted(false);
        // BackRightAngleMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 35, 0.2));
        // BackRightAngleMotor.setNeutralMode(NeutralMode.Brake);
        BackRightDriveMotor.setInverted(true);
        // BackRightDriveMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 30, 35, 0.2));
        // BackRightDriveMotor.setNeutralMode(NeutralMode.Coast);
        frontLeftModule.setName("Front Left");
        frontRightModule.setName("Front Right");
        backLeftModule.setName("Back Left");
        backRightModule.setName("Back Right");
        fieldOriented = true;
    }

    public static DrivetrainSubsystem getInstance() {
        if (instance == null) {
            instance = new DrivetrainSubsystem();
        }

        return instance;
    }

    @Override
    public void periodic() {
        frontLeftModule.updateSensors();
        frontRightModule.updateSensors();
        backLeftModule.updateSensors();
        backRightModule.updateSensors();

        SmartDashboard.putNumber("Front Left Module Angle", Math.toDegrees(frontLeftModule.getCurrentAngle()));
        SmartDashboard.putNumber("Front Right Module Angle", Math.toDegrees(frontRightModule.getCurrentAngle()));
        SmartDashboard.putNumber("Back Left Module Angle", Math.toDegrees(backLeftModule.getCurrentAngle()));
        SmartDashboard.putNumber("Back Right Module Angle", Math.toDegrees(backRightModule.getCurrentAngle()));

        SmartDashboard.putNumber("Gyroscope Angle", gyroscope.getAngle().toDegrees());

        frontLeftModule.updateState(TimedRobot.kDefaultPeriod);
        frontRightModule.updateState(TimedRobot.kDefaultPeriod);
        backLeftModule.updateState(TimedRobot.kDefaultPeriod);
        backRightModule.updateState(TimedRobot.kDefaultPeriod);
    }

    public void drive(Translation2d translation, double rotation, boolean fieldOriented) {
        rotation *= 2.0 / Math.hypot(WHEELBASE, TRACKWIDTH);
        ChassisSpeeds speeds;
        if (fieldOriented) {
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(), rotation,
                    Rotation2d.fromDegrees(gyroscope.getAngle().toDegrees()));
        } else {
            speeds = new ChassisSpeeds(translation.getX(), translation.getY(), rotation);
        }

        SwerveModuleState[] states = kinematics.toSwerveModuleStates(speeds);
        frontLeftModule.setTargetVelocity(states[0].speedMetersPerSecond, states[0].angle.getRadians());
        frontRightModule.setTargetVelocity(states[1].speedMetersPerSecond, states[1].angle.getRadians());
        backLeftModule.setTargetVelocity(states[2].speedMetersPerSecond, states[2].angle.getRadians());
        backRightModule.setTargetVelocity(states[3].speedMetersPerSecond, states[3].angle.getRadians());
    }

    public void resetGyroscope() {
        gyroscope.setAdjustmentAngle(gyroscope.getUnadjustedAngle());
    }

    public double getRealAngle(){
        return gyroscope.getUnadjustedAngle().toDegrees();
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveCommand());
    }
}
