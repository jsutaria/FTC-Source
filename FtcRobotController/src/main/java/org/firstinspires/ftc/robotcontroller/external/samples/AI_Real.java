package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jainil.sutaria on 10/20/2016.
 */

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="AI Real", group="Record/PlayBack")
public class AI_Real extends OpMode {

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

        startTime = System.currentTimeMillis();

    }

    @Override
    public void loop()
    {
        /*In the loop we are simply calling the method
         * that controls the movement of the robot
         * */
        Movement();
    }



    void setMotor(int n, double power) {
        /*A quick and easy way to set the _n_th
        * motor at _power_ power*/
        motors.get(n).motor.setPower(power);
    }

    static long startTime = 0; //static start time of when the robot starts to run so that the robot knows when to move how much
    static int currentTime; //static time of what the current time is (in millis)
    void Movement()
    {
        /**
         * This method controls the movement of the robot
         */

        if(startTime == 0) startTime = System.currentTimeMillis(); //if hasnt been initialized, initialize the start time
        currentTime =  (int) (System.currentTimeMillis() - startTime); //set current time based on current time millis and start time

        telemetry.addData("time", currentTime); //log to telemetry for debugging
        if(currentTime < 3700) { //check if the value is less than 3700 seconds (so if yes to run)
            telemetry.addData("play", true); //log to telemetry
            /*
             * Set each motor to going at 1/2 speed so its more accurate
             */
            setMotor(0, -0.5);
            setMotor(1, 0.5);
            setMotor(2, -0.5);
            setMotor(3, 0.5);
        } else {
            telemetry.addData("play", false); //log to telemetry
            /*
             * Stop each motor
             */
            setMotor(0, 0.0);
            setMotor(1, 0.0);
            setMotor(2, 0.0);
            setMotor(3, 0.0);
        }

    }
}
