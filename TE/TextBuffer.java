package TE;

import javafx.scene.Group;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

import static TE.Editor.*;

/**
 * Created by TangDexian on 2017/04/01.
 */

public class TextBuffer {
    private int PosX;
    private int PosY;
    static List<TextUnit> TextList = new ArrayList<>();
    static FastList FastTextList = new FastList();
    static double TextWidth;
    double TextHeight;
    static int ListSize;


    public static void addText(Group root, Text T, String C) {
        int X = FastTextList.CursorNode.getTextUnit().getX();
        int Y = FastTextList.CursorNode.getTextUnit().getY();
        TextUnit TU = new TextUnit(T, C, X, Y);
        textRoot.getChildren().add(T);
        FastTextList.add(TU);
        ListSize = FastTextList.size();

        //List printing Test Unit
        /*
        FastTextList.TempNodeSentinelUpdate();
        while(FastTextList.TempNode.nextN != null) {
            System.out.print(FastTextList.TempNode.nextN.getTextUnit().getString());
            FastTextList.TempNode.nextN = FastTextList.TempNode.nextN.nextN;
        }
        System.out.println("");
        */
        TextRendering.UpdatePosFromCursorNode(FastTextList.CursorNode);
    }

    public static void addNewLine(String C) {
        int X = FastTextList.CursorNode.getTextUnit().getX();
        int Y = FastTextList.CursorNode.getTextUnit().getY();
        TextUnit TU = new TextUnit(C, X, Y);
        FastTextList.add(TU);
        ListSize = FastTextList.size();
        TextRendering.newLine();
        TextRendering.UpdatePosFromCursorNode(FastTextList.CursorNode);
    }


    public static int prevTextHeight() {
        if (FastTextList.CursorNode.nextN.prevN.getTextUnit().getString().equals("NewLine")){
            return FastTextList.CursorNode.nextN.prevN.getTextUnit().getHeight();
        }
        return FastTextList.CursorNode.nextN.prevN.getTextUnit().getHeight();
    }

    public static void removeText(Group root) {
        if (FastTextList.EndNode.prevN!=FastTextList.Sentinel) {
            TextUnit TUtoD = FastTextList.CursorNode.nextN.prevN.getTextUnit();
            textRoot.getChildren().remove(TUtoD.getText());
            FastTextList.delete();
            TextRendering.UpdatePosFromCursorNode(FastTextList.CursorNode);
        }
    }


}
