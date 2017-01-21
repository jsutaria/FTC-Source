package org.firstinspires.ftc.robotcontroller.external.samples;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.InputType;
import android.widget.EditText;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by jainil.sutaria on 1/20/2017.
 */
@Autonomous(name = "Play Recording", group = "Record/PlayBack")
public class AIOpPlay extends OpMode {

    List<List<Hardware>> data; // This ArrayList will store all of the data sets that our robot will run from

    List<Hardware> CURRENTDATA;

    int step;
    int dataSetIndex;

    int blueAlliance;

    /**
     * Runs once OpMode is selected on phone
     * Use to setup variables that don't rely on the state of the robot
     */
    @Override
    public void init() {
        data = new ArrayList<>();
        CURRENTDATA = new ArrayList<>();

        dataSetIndex = 0;
        try {
            ReadData(new File(String.format("%s/%s.txt", Environment.getExternalStorageDirectory(), "AUTO-RED"))); // Import data from the file
        } catch (IOException e) {
            e.printStackTrace();
        }

        step = 0;
    }


    void ReadData(File f) throws IOException {
        Scanner in = new Scanner(f);
        int numHardware = in.nextInt(); // Number of hardware devices to add
        in.nextLine();

        List<Hardware> dataSet = new ArrayList<>();

        for (int i = 0; i < numHardware; i++) {
            int numData = in.nextInt();
            String[] hardwareId = in.next().split("_"); // Separate the id into hardware type and name
            Hardware ah;

            // Setup the piece of hardware
            if (hardwareId[0].equals("MOTOR")) {
                DcMotor motor = hardwareMap.dcMotor.get(hardwareId[1]);
                ah = new Hardware(hardwareId[0], hardwareId[1], motor);
            } else {
                Servo servo = hardwareMap.servo.get(hardwareId[1]);
                ah = new Hardware(hardwareId[0], hardwareId[1], servo);
            }

            List<Double> data = new ArrayList<>();
            for (int j = 0; j < numData; j++) {
                data.add(in.nextDouble()); // Add each value from the .txt file to the hardware's data array
            }
            ah.SetValues(data); // Add the values to the hardware
            in.nextLine();

            dataSet.add(ah); // Add the hardware to this data set
        }

        in.close();

        data.add(dataSet); // Add this data set to the array of all data sets
    }

    @Override
    public void loop() {
        if (time > .1 && step < CURRENTDATA.size()) {
            for (Hardware ah : CURRENTDATA)
                ah.SetPosition(ah.dataValues.get(step++));
            resetStartTime();
        }

    }
}