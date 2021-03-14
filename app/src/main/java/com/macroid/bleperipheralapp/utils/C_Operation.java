package com.macroid.bleperipheralapp.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class C_Operation
{

    public C_Operation()
    {
    }


    public byte[] F_Answer(String text)
    {
        String[] temp = text.split(" ");
        switch (temp[1])
        {
            case "+":

                return String.valueOf(Integer.parseInt(temp[0]) + Integer.parseInt(temp[2])).getBytes(StandardCharsets.UTF_8);

            case "-":
                return String.valueOf(Integer.parseInt(temp[0]) - Integer.parseInt(temp[2])).getBytes(StandardCharsets.UTF_8);

            case "*":
                return String.valueOf(Integer.parseInt(temp[0]) * Integer.parseInt(temp[2])).getBytes(StandardCharsets.UTF_8);

            case "/":
                return String.valueOf(Integer.parseInt(temp[0]) / Integer.parseInt(temp[2])).getBytes(StandardCharsets.UTF_8);

            default:
                return "0".getBytes(StandardCharsets.UTF_8);
        }
    }

}
