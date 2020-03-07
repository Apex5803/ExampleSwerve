package com.swervedrivespecialties.exampleswerve;

import com.swervedrivespecialties.exampleswerve.commands.VisionDriveCommands.AlignToTarget;
import com.swervedrivespecialties.exampleswerve.subsystems.DrivetrainSubsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class OI {
    /*
       Add your joysticks and buttons here
     */
    private XboxController xbox1 = new XboxController(0);
    private XboxController xbox2 = new XboxController(1);
    public OI() {
        // Back button zeroes the drivetrain
        new JoystickButton(xbox1, 7).whenPressed(
                new InstantCommand(() -> {
                    Robot.drivetrain.resetGyroscope();
                }
                )
        );

        JoystickButton DriverA = new JoystickButton(xbox1, 1);
        DriverA.whileHeld(new AlignToTarget());
    }

    public XboxController getXbox1() {
        return xbox1;
    }
    public XboxController getXbox2() {
        return xbox2;
    }

}
