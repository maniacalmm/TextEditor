package TE;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by TangDexian on 2017/04/03.
 */
public class OpenSave {
    public static void main(String[] args) {
        List<Character> readedList = new ArrayList<>();
        TE.Editor textEditor = new TE.Editor();
        textEditor.main(args);
        System.out.println("List length before writing!   " + readedList.size());
        if (args.length != 1) {
            System.out.println("Plz give the proper filename.");
            System.exit(1);
        }
        String FiletoReadorChange = args[0];

        try{
            FileWriter writer;
            File inputfile = new File(FiletoReadorChange);
            // Check to see if the file exits!
            if (!inputfile.exists()) {
                System.out.println("File doesn't exist, creating new file...");
                writer = new FileWriter(FiletoReadorChange);
            }

            FileReader reader = new FileReader(FiletoReadorChange);
            BufferedReader bufferReader = new BufferedReader(reader);

            int intRead = -1;

            while ((intRead = bufferReader.read()) != -1) {
                char charRead = (char) intRead;
                readedList.add(charRead);
            }


            System.out.println("Successfully added it to buffer");
            System.out.println("List size after writing    " + readedList.size());
            //Close the reader and writer
            bufferReader.close();
        } catch(IOException e) {
            System.out.println("Error when copying, fix your code.");
        }
    }


}
