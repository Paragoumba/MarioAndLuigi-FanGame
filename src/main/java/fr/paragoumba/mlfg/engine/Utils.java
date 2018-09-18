package fr.paragoumba.mlfg.engine;

import fr.paragoumba.mlfg.engine.graph.Texture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Utils {

    public static String loadResource(String fileName) throws IOException, ClassNotFoundException {

        String result;

        try(InputStream in = Class.forName(Utils.class.getName()).getResourceAsStream(fileName);
            Scanner scanner = new Scanner(in, "UTF-8")){

            result = scanner.useDelimiter("\\A").next();

        }

        return result;

    }

    public static List<String> readAllLines(String fileName) throws Exception {

        List<String> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Class.forName(Utils.class.getName()).getResourceAsStream(fileName)))) {

            String line;
            while ((line = br.readLine()) != null) {

                list.add(line);

            }
        }

        return list;

    }

    public static float[] listToArray(List<Float> list) {

        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];

        for (int i = 0; i < size; i++) {

            floatArr[i] = list.get(i);

        }

        return floatArr;
    }
}
