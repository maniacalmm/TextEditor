package TE;

import javafx.scene.Group;
import javafx.scene.text.Text;

import java.io.*;

import static javafx.application.Application.launch;
import static TE.Editor.FileName;

/**
 * Created by Tang on 4/3/2017.
 */
public class Opener {

    static void open(String[] args, Group root) {
        //System.out.println("List length before writing!   " + readedList.size());
        //Editor.CursorSet(CursorX, CursorY);
        if (args.length != 1) {
            System.out.println("Plz give the proper filename.");
            System.exit(1);
        }
        String FiletoRead = args[0];
        FileName = FiletoRead;

        File inputfile = new File(FiletoRead);
        // Check to see if the file exits!

        try {
            if (!inputfile.exists()) {
                System.out.println("File doesn't exist, creating new file...");
                return;
            }

            FileReader reader = new FileReader(FiletoRead);
            BufferedReader bufferReader = new BufferedReader(reader);

            int intRead = -1;

            while ((intRead = bufferReader.read()) != -1) {
                Text T = new Text();
                char charRead = (char) intRead;
                //System.out.print(intRead);
                if (intRead == '\n') {
                    TE.TextBuffer.addNewLine("NewLine");
                } else {
                    TE.TextBuffer.addText(root, T, Character.toString(charRead));
                }
            }

            TextBuffer.FastTextList.CursortoBegin();
            Editor.fileHeight = TextBuffer.FastTextList.EndNode.getTextUnit().getY();
            System.out.println("file height is  " + Editor.fileHeight);

            System.out.println("Successfully added it to buffer");
            //Close the reader and writer

        } catch(IOException e) {
            System.out.println("Error when copying, fix your code.");
        }
    }
}
