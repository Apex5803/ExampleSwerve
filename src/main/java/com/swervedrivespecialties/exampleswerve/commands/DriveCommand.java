package com.swervedrivespecialties.exampleswerve.commands;

// import com.swervedrivespecialties.exampleswerve.ConfigValues;
import com.swervedrivespecialties.exampleswerve.Robot;
import com.swervedrivespecialties.exampleswerve.subsystems.DrivetrainSubsystem;
// import com.swervedrivespecialties.exampleswerve.utils.NumberUtils;


import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import org.frcteam2910.common.robot.Utilities;

public class DriveCommand extends Command {

    public DriveCommand() {
        requires(DrivetrainSubsystem.getInstance());
    }

    @Override
    protected void execute() {
        double forward = Robot.getOi().getXbox1().getRawAxis(1);
        double strafe = Robot.getOi().getXbox1().getRawAxis(0);

        forward =  Utilities.deadband(forward,  .10 );
        // Square the forward stick
        forward = Math.copySign(Math.pow(forward, 2.0), forward);

        
        strafe =  Utilities.deadband(strafe, .10);
        // Square the strafe stick
        strafe = Math.copySign(Math.pow(strafe, 2.0), strafe);

        double rotation = Robot.getOi().getXbox1().getRawAxis(4);
        rotation = Utilities.deadband(rotation, 0.1);
        // Square the rotation stick
        rotation = Math.copySign(Math.pow(rotation, 2.0), rotation);

        // DrivetrainSubsystem.getInstance().drive(new Translation2d(forward, strafe), rotation, Robot.drivetrain.fieldOriented);
         DrivetrainSubsystem.getInstance().drive(new Translation2d(forward, strafe), rotation, true);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
