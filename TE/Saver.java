package TE;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by TangDexian on 2017/04/04.
 */
public class Saver {
    public static void write(FastList.ListNode Sentinel) {
        try {
            /*
            here we use the built-in temp node to iterate through the TextList
             */
            TextBuffer.FastTextList.TempNodeSentinelUpdate();

            FileWriter writer = new FileWriter(Editor.FileName);
            while (TextBuffer.FastTextList.TempNode.nextN != TextBuffer.FastTextList.EndNode) {
                TextUnit T = TextBuffer.FastTextList.TempNode.nextN.getTextUnit();
                if (T.getString().equals("NewLine")){
                    writer.write(System.lineSeparator());
                }else {
                    writer.write(T.getString());
                }
                TextBuffer.FastTextList.TempNode.nextN = TextBuffer.FastTextList.TempNode.nextN.nextN;
            }
            System.out.println("File saved!");
            writer.close();
        } catch (IOException e) {
            System.out.println("Something went wrong in writing the file...");
        }
    }
}
