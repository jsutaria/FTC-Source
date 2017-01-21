package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jainil.sutaria on 10/20/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Driver Control", group="DriverControl")
public class DriverControl extends OpMode{

    List<Hardware> motors;

    @Override
    public void init()
    {
        /* Initialize all the motors into an arraylist
        * so that they can easily be called for
        * in the movement section of the autonomous
        * Each motor is mapped as MOTOR(name)
        * */
        motors = new ArrayList<>();
        for (String m : Values.MOTORS)
        {

            motors.add(new Hardware("MOTOR" + m, m, (DcMotor)hardwareMap.get(m)));
        }

    }

    @Override
    public void loop()
    {
        /*In the loop we are simply calling the method
         * that controls the movement of the robot
         * */
        Movement();
    }

    /**
     * This method controls the movement of the robot
     */


    void setMotor(int n, double power) {
        /*A quick and easy way to set the _n_th
        * motor at _power_ power*/
        motors.get(n).motor.setPower(power);
    }

    void Movement()
    {

        double throttle = gamepad1.left_stick_y; //the throttle of the wheels
        double direction = gamepad1.left_stick_x; //the direction to add/subtract from the throttle for right/left power

        setMotor(4, gamepad1.right_trigger - gamepad1.left_trigger); //arm mechanism power is based off of back triggers

        if(gamepad1.left_bumper) {
            setMotor(5, 0.4); //spinning the color sensor pushers to the left if the left bumper is pressed
        } else if (gamepad1.right_bumper) {
            setMotor(5, -0.4); //spinning the color sensor pushers to the right if the right bumper is pressed
        } else {
            setMotor(5, 0); //stop all if none is pressed
        }

        if(gamepad1.dpad_up) { //ALL WHEELS GO FORWARD
            setMotor(0, -1.0);
            setMotor(1, 1.0);
            setMotor(2, -1.0);
            setMotor(3, 1.0);
        } else if (gamepad1.dpad_down) { //ALL WHEELS GO BACKWARD
            setMotor(0, 1.0);
            setMotor(1, -1.0);
            setMotor(2, 1.0);
            setMotor(3, -1.0);
        } else if (gamepad1.dpad_right) { //MECANUM WHEEL RIGHT MOVEMENT
            setMotor(0, -1.0);
            setMotor(1, 1.0);
            setMotor(2, 1.0);
            setMotor(3, -1.0);
        } else if (gamepad1.dpad_left) { //MECANUM WHEEL LEFT MOVEMENT
            setMotor(0, 1.0);
            setMotor(1, -1.0);
            setMotor(2, -1.0);
            setMotor(3, 1.0);
        } else { //REGULAR TURNING OF MOTORS
            double right = 1.0 * Range.clip((throttle - direction) * Values.MOTORSPEED, -1, 1); //ADJUST VALUES OF MOTORS FOR PROPER TURNING
            double left = -1.0 * Range.clip((throttle + direction) * Values.MOTORSPEED, -1, 1); //ADJUST VALUES OF MOTORS FOR PROPER TURNING

            setMotor(1, left);
            setMotor(3, left);
            setMotor(0, right);
            setMotor(2, right);

        }

    }
}
