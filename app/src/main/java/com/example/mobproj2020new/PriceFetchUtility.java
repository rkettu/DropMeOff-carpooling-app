package com.example.mobproj2020new;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PriceFetchUtility {

    public static String fromStream(InputStream in) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while((line = bufferedReader.readLine()) != null){
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }
}
