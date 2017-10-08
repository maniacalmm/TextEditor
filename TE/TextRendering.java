package TE;

import static TE.Editor.*;
import static TE.TextBuffer.FastTextList;

/**
 * Created by TangDexian on 2017/04/01.
 */
public class TextRendering {
    private int nextX;
    private int nextY;


    public static void UpdatePosFromCursorNode(FastList.ListNode P) {
        /*
        Phase #1, End point insertion.
        TempNode is initially set to EndNode, like cursor node.
         */

        updatePos(P);
        CursorNodeSet(P);
        // Decide if word wrap is necessary here.
        //System.out.println(FastTextList.CursorNode.nextN.prevN.getTextUnit().getX() + " ,  WW   " + WINDOW_WIDTH);
        if ((P.nextN.prevN.getTextUnit().getX() +
             P.nextN.prevN.getTextUnit().getWidth()) > WINDOW_WIDTH - 5){
            /*
            FastTextList.TempNodeCursorUpdate();
            System.out.println(WordBump());
            if (WordBump() && FastTextList.TempNode.getTextUnit().getY() != 0) {
                System.out.println("We are bumping");


                while(FastTextList.TempNode.nextN != FastTextList.CursorNode.nextN) {
                    updatePos(FastTextList.TempNode);
                    FastTextList.TempNode.nextN = FastTextList.TempNode.nextN.nextN;
                }
                updatePos(FastTextList.CursorNode);
                CursorNodeSet(FastTextList.CursorNode);
            } else {
            }
            */
                FastTextList.TempNodeCursorUpdate();
                WordWrap();
        }


        /*
        Phase #2, middle point insertion
        From here on, update the rest of the list, if there are any
         */
        FastTextList.TempNodeCursorUpdate(); // Synced the TempNode to CursorNode

        if (FastTextList.TempNode.nextN != FastTextList.EndNode) {
            FastTextList.TempNode.nextN = FastTextList.TempNode.nextN.nextN; // TempNode here is one Node after Cursor

            while (FastTextList.TempNode.nextN != null) { // this will also update the EndNode, which is awesome
                // update the pointed Text according to the previous one
                updatePos(FastTextList.TempNode);

                // Decide if word wrap is necessary here.
                //System.out.println(FastTextList.TempNode.nextN.prevN.getTextUnit().getX());
                if ((FastTextList.TempNode.nextN.prevN.getTextUnit().getX() +
                    FastTextList.TempNode.nextN.prevN.getTextUnit().getWidth()) > WINDOW_WIDTH - 5){
                    WordWrap();
                }

                FastTextList.TempNode.nextN = FastTextList.TempNode.nextN.nextN;
            }
        }

        fileHeight = FastTextList.EndNode.getTextUnit().getY();

    }

    private static boolean WordBump() {
        while (FastTextList.TempNode.nextN.getTextUnit().getX() != STARTING_CURSOR_X){
           if (FastTextList.TempNode.nextN.getTextUnit().getString().charAt(0) == 32){
               return false;
           }
           FastTextList.TempNode.nextN = FastTextList.TempNode.nextN.prevN;
        }
        return true;

    }

    private static void updatePos(FastList.ListNode P) {
        int PreX;
        int PreW;
        int PreY = ((TextUnit) P.nextN.prevN.getTextUnit()).getY();

        if (((TextUnit)P.nextN.prevN.getTextUnit()).getString().equals("NewLine")) {
            /*
            this is to cover the NewLine special case;
             */
            PreW = 0;
            PreX = STARTING_CURSOR_X;

            if(P.TU != null) {
                ((TextUnit) P.getTextUnit()).updateX(PreX + PreW); // update X for CursorNode
                ((TextUnit) P.getTextUnit()).updateY(PreY + fontSize);            // update Y for CursorNode
            }

            ((TextUnit) P.nextN.getTextUnit()).updateX(PreX + PreW); // update the Node Pointed by the Curosr Node;
            ((TextUnit) P.nextN.getTextUnit()).updateY(PreY + fontSize); // update the Node Pointed by the Curosr Node;
            P.nextN.getTextUnit().setFont();

            if (!((TextUnit) P.nextN.getTextUnit()).getString().equals("NewLine")) {
                ((TextUnit) P.nextN.getTextUnit()).DisplayUpdate();  // update the display position of the node posinted by Cursor.
            }

        } else {
            if (((TextUnit) P.nextN.prevN.getTextUnit()).getString().equals("Sentinel")) {
                PreW = 0;
            } else {
                PreW = (int) Math.round(((TextUnit) P.nextN.prevN.getTextUnit()).
                    getText().getLayoutBounds().getWidth());
            }

            PreX = ((TextUnit) P.nextN.prevN.getTextUnit()).getX();

            if (P.TU != null) {
                ((TextUnit) P.getTextUnit()).updateX(PreX + PreW); // update X for CursorNode
                ((TextUnit) P.getTextUnit()).updateY(PreY);            // update Y for CursorNode
            }

            ((TextUnit) P.nextN.getTextUnit()).updateX(PreX + PreW); // update the Node Pointed by the Curosr Node;
            ((TextUnit) P.nextN.getTextUnit()).updateY(PreY); // update the Node Pointed by the Curosr Node;
            P.nextN.getTextUnit().setFont();
            if (!((TextUnit) P.nextN.getTextUnit()).getString().equals("NewLine")) {
                ((TextUnit) P.nextN.getTextUnit()).DisplayUpdate();  // update the display position of the node posinted by Cursor.
            }
        }


   }


    public static void newLine() {
        ((TextUnit)FastTextList.CursorNode.getTextUnit()).updateX(STARTING_CURSOR_X);
        int PreY = ((TextUnit) FastTextList.CursorNode.nextN.prevN.getTextUnit()).getY();
        ((TextUnit)FastTextList.CursorNode.getTextUnit()).updateY(PreY + fontSize);
        CursorNodeSet(FastTextList.CursorNode);

    }

    public static void CursorNodeSet(FastList.ListNode P) {
        int CPX = ((TextUnit) P.getTextUnit()).getX();
        int CPY = ((TextUnit) P.getTextUnit()).getY();
        Cursor.setY(CPY);
        Cursor.setX(CPX);
        Cursor.setHeight(fontSize);
    }


    public static void WordWrap() {
        if (PreviousSpace()) {
            //System.out.println("we got a runner, and we are here");
        // Just once, we move that character need to wrap to the new line
        FastTextList.WordWrapNode.nextN.getTextUnit().updateX(STARTING_CURSOR_X);
        int currentHeight = FastTextList.WordWrapNode.nextN.getTextUnit().getY();
        FastTextList.WordWrapNode.nextN.getTextUnit().updateY(currentHeight + fontSize);
        FastTextList.WordWrapNode.nextN.getTextUnit().DisplayUpdate();
        // and now we update the rest of the text from here
        FastTextList.WordWrapNode.nextN = FastTextList.WordWrapNode.nextN.nextN;
            /*
            Updated till the node before cursor.
            */
            while (FastTextList.WordWrapNode.nextN != null){
                updatePos(FastTextList.WordWrapNode);
                //System.out.println(FastTextList.WordWrapNode.nextN.getTextUnit().content);
                FastTextList.WordWrapNode.nextN = FastTextList.WordWrapNode.nextN.nextN;
            }
            /*
            Update cursor node
             */
            FastTextList.CursorAndCursorNextSync();
        } else {
                    //System.out.println(FastTextList.TempNode.nextN.prevN.getTextUnit().content);
                    FastTextList.TempNode.nextN.prevN.getTextUnit().updateX(STARTING_CURSOR_X);
                    int currentHeight = FastTextList.TempNode.nextN.prevN.getTextUnit().getY();
                    FastTextList.TempNode.nextN.prevN.getTextUnit().updateY(currentHeight + fontSize);
                    FastTextList.TempNode.nextN.prevN.getTextUnit().DisplayUpdate();
                    updatePos(FastTextList.TempNode);
                //FastTextList.TempNode.nextN = FastTextList.TempNode.nextN.nextN;
           }
            FastTextList.CursorAndCursorNextSync();
        }


    /*
    THis function finds the first Node before white space.
     */
    private static boolean PreviousSpace() {
        //FastTextList.TempNodeCursorUpdate();
        FastTextList.WrapperTempNodeSync();
        //System.out.println(FastTextList.WordWrapNode.nextN.getTextUnit().content);
        while (FastTextList.WordWrapNode.nextN.prevN.getTextUnit().getString().charAt(0) != 32
                || FastTextList.WordWrapNode.nextN.getTextUnit().getX() == STARTING_CURSOR_X){
            if (FastTextList.WordWrapNode.nextN.getTextUnit().getX() == STARTING_CURSOR_X){
                //System.out.println("the whole line!!");
                return false;
            }
            if(FastTextList.WordWrapNode.nextN.prevN == FastTextList.Sentinel
                    || FastTextList.WordWrapNode.nextN.prevN.getTextUnit().getString().equals("NewLine")){
                return false;
            }

            FastTextList.WordWrapNode.nextN = FastTextList.WordWrapNode.nextN.prevN;
            //System.out.println(FastTextList.WordWrapNode.nextN.getTextUnit().getString());
        }
        /*
        System.out.println("got it! it is  " + FastTextList.WordWrapNode.nextN.getTextUnit().content );
        */
        return true;
    }


    public static void WindowResize() {
        System.out.println("Resizing!!");
            FastTextList.TempNode.nextN = FastTextList.Sentinel.nextN;
       while (FastTextList.TempNode.nextN != null) { // this will also update the EndNode, which is awesome
           // update the pointed Text according to the previous one
           updatePos(FastTextList.TempNode);

           // Decide if word wrap is necessary here.
           //System.out.println(FastTextList.TempNode.nextN.prevN.getTextUnit().getX());
           if ((FastTextList.TempNode.nextN.prevN.getTextUnit().getX() +
                   FastTextList.TempNode.nextN.prevN.getTextUnit().getWidth()) > WINDOW_WIDTH - 5) {
               /*
               System.out.println(FastTextList.TempNode.nextN.prevN.getTextUnit().getString() + "  "
                       + FastTextList.TempNode.nextN.prevN.getTextUnit().getX());
                       */
               WordWrap();
           }

           FastTextList.TempNode.nextN = FastTextList.TempNode.nextN.nextN;
       }
    }

    public static void IncreaseFontSize(){
        fontSize += 4;
        WindowResize();
        FastTextList.CursorAndCursorNextSync();

    }

    public static void DecreaseFontSize(){
        fontSize -= 4;
        WindowResize();
        FastTextList.CursorAndCursorNextSync();
    }
}
