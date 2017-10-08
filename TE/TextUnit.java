package TE;

import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Created by TangDexian on 2017/04/01.
 */

/**
 * Text Unit is where we bound the position information with the Text Object itself.
 */

public class TextUnit {
    public Text T;
    public String content;
    int LineNumber;
    int X;
    int Y;
    /*
    EndNode Constructor and CursorNode, since we want CursorNode to also store the Cursor XY information

    Also for newLine Element
     */
    public TextUnit(String S, int X, int Y) {
        content = S;
        this.X = X;
        this.Y = Y;
    }

    /*
    usual textUnit constructor
     */
    public TextUnit(Text T, String C, int PosX, int PosY) {
        T.setText(C);
        T.setFont(Font.font(Editor.fontName, Editor.fontSize));
        T.setTextOrigin(VPos.TOP);
        T.setX(PosX);
        T.setY(PosY);
        this.T = T;
        content = C;
        X = PosX;
        Y = PosY;
    }


    public void DisplayUpdate() {
        //System.out.println(content);
        if (!content.equals("EndNode") && !content.equals("Sentinel")
                && !content.equals("NewLine")){
            T.setX(this.X);
            T.setY(this.Y);
        }
    }

    public void updateY(int Y) {
        this.Y = Y;

    }
    public void updateX(int X) {
        this.X = X;

    }

    public void setFont() {
        if (!content.equals("EndNode") && !content.equals("Sentinel") && !content.equals("NewLine")){
            T.setFont(Font.font(Editor.fontName, Editor.fontSize));
        }
    }

    int getX() {
        return X;
    }

    int getY() {
        return Y;
    }

    public int getWidth(){
        if (content.equals("NewLine")){
            return 0;
        }

        if (content.equals("Sentinel")) {
            return 0;
        }

        if (content.equals("EndNode")) {
            return 0;
        }
        /*
        if (FastTextList.CursorNode.nextN.getTextUnit().getString().equals("EndNode")){
            return 0;
        }
        */
        return (int) Math.round(T.getLayoutBounds().getWidth());
    }

    public int getHeight() {
        return (int) Math.round(T.getLayoutBounds().getHeight());
    }


    String getString() {
        return content;
    }

    public Text getText() {
        return T;
    }
}
