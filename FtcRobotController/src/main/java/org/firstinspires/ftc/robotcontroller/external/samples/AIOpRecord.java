package org.firstinspires.ftc.robotcontroller.external.samples;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.InputType;
import android.widget.EditText;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jainil.sutaria on 1/20/2017.
 */

@Autonomous(name = "Record Autonomous", group = "Record/PlayBack")
public class AIOpRecord extends OpMode
{
    String fileName; // Name of the file to write to, set in init

    List<Hardware> data; // This ArrayList will store all of the data1 that our robot will record

    /**
     * Runs once OpMode is selected on the phone
     * Use to setup variables that don't rely on the state of the robot
     */
    @Override
    public void init()
    {
        //Context context = FtcRobotControllerActivity.getContext(); // Because we aren't in the main activity, this is a roundabout way of getting the Context
        data = new ArrayList<>();

        // Set up input field
       // final EditText input = new EditText(context);
        //input.setInputType(InputType.TYPE_CLASS_TEXT); // Just a normal text field

//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        /*
        builder.setTitle("Set Filename")
                .setMessage("What file should the values be saved to? Don't include file extensions")
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() // We only want a confirm button
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        fileName = input.getText().toString(); // Sets the filename to whatever the user entered
                    }
                })
                .show(); // Show the user the alert

        AlertDialog.Builder chooseAlliance = new AlertDialog.Builder(context);
        chooseAlliance.setTitle("Which alliance are we?")
                .setPositiveButton("Blue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fileName += "-BLUE";
                    }
                })
                .setNegativeButton("Red", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fileName += "-RED";
                    }
                }).show();
                */

        fileName = "AUTO-RED";
        for (String s : Values.MOTORS)
        {
                DcMotor motor = hardwareMap.dcMotor.get(s);
                data.add(new Hardware("MOTOR", s, motor));
        }
    }

    /**
     * Runs in a loop after Start is pressed
     */
    @Override
    public void loop()
    {
        if (time > .1) // We're recording values every .1 seconds
        {
            for (Hardware ah: data)
            {
                if (ah.isMotor) ah.AddValue(ah.motor.getCurrentPosition());
                else ah.AddValue(ah.servo.getPosition());
            }

            resetStartTime();
        }

        // This is the same as DriverControl
        Movement();
    }


    void setMotor(int n, double power) {
        data.get(n).motor.setPower(power);
    }


    /**
     * This method controls the movement of the robot
     */
    void Movement()
    {
        double throttle = gamepad1.left_stick_y;
        double direction = gamepad1.left_stick_x;

        if(gamepad1.dpad_up) {
            setMotor(0, -1.0);
            setMotor(1, 1.0);
            setMotor(2, -1.0);
            setMotor(3, 1.0);
        } else if (gamepad1.dpad_down) {
            setMotor(0, 1.0);
            setMotor(1, -1.0);
            setMotor(2, 1.0);
            setMotor(3, -1.0);
        } else if (gamepad1.dpad_right) {
            setMotor(0, -1.0);
            setMotor(1, 1.0);
            setMotor(2, 1.0);
            setMotor(3, -1.0);
        } else if (gamepad1.dpad_left) {
            setMotor(0, 1.0);
            setMotor(1, -1.0);
            setMotor(2, -1.0);
            setMotor(3, 1.0);
        } else {
            double right = 1.0 * Range.clip((throttle - direction) * Values.MOTORSPEED, -1, 1);
            double left = -1.0 * Range.clip((throttle + direction) * Values.MOTORSPEED, -1, 1);

            data.get(1).motor.setPower(left);
            data.get(3).motor.setPower(left);
            data.get(0).motor.setPower(right);
            data.get(2).motor.setPower(right);
        }

    }
    /**
     * Runs at the end of the OpMode
     */
    @Override
    public void stop()
    {
        try {
            WriteData(new File(String.format("%s/%s.txt", Environment.getExternalStorageDirectory(), fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void WriteData(File f) throws IOException
    {
        Writer out = new BufferedWriter(new FileWriter(f));
        out.write(Integer.toString(data.size())); // Number of recorded values
        for (Hardware ah : data)
        {
            out.append(Integer.toString(ah.dataValues.size()) + " ") // Number of data points for current hardware
                    .append(ah.hardwareType + "_")
                    .append(ah.hardwareName + " ");

            for (Double d : ah.dataValues)
                out.append(Double.toString(d) + " "); // Data values
        }
        out.close();
    }
}
