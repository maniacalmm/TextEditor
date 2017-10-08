package TE;

import static TE.Editor.*;

/**
 * Created by TangDexian on 2017/04/01.
 */

/**
 * class used to store the Text elements
 *
 */
public class FastList{
    /*
    Underlaying ListNode
     */
    public class ListNode {
        public ListNode prevN;
        public TextUnit TU;
        public ListNode nextN;

        public ListNode (ListNode prev, TextUnit content, ListNode next){
            prevN = prev;
            TU = content;
            nextN = next;
        }

        public ListNode gimmeAnode() {
            return new ListNode(null, null, null);
        }

        public TextUnit getTextUnit(){
            return TU;
        }
    }

    ListNode CursorNode = new ListNode(null,
            new TextUnit("CursorNode", STARTING_CURSOR_X, 0), null);
    ListNode Sentinel = new ListNode(null,
            new TextUnit("Sentinel", STARTING_CURSOR_X, 0), null);
    ListNode EndNode = new ListNode(null,
            new TextUnit("EndNode", STARTING_CURSOR_X, 0), null);
    ListNode TempNode = new ListNode(null, null, null);

    ListNode WordWrapNode = new ListNode(null, null, null);

    ListNode WindowResizingNode = new ListNode(null, null, null);
    ListNode CursorPlaceHolder = new ListNode(null, null, null);

    /*
    public void NodeInitilizer(ListNode LN, itemType TU){
        LN = new ListNode(null, TU, null);
    }
    */

    private int size;
    /*
    Actually constructor for the FastList;
     */
    public FastList() {
        // CursorNode only point to the TextUnit with it's next pointer;

        CursorNode.nextN = EndNode;
        Sentinel.nextN = EndNode;
        EndNode.prevN = Sentinel;
        TempNode.nextN = EndNode;
        size = 0;
    }

    public void add(TextUnit TU) {
        if (CursorNode.nextN.prevN == Sentinel && CursorNode.nextN.nextN == EndNode) {
            // the very first node;
            ListNode newNode = new ListNode(Sentinel, TU, EndNode);
            Sentinel.nextN = newNode;
            EndNode.prevN = newNode;
            size += 1;
        } else if (CursorNode.nextN == EndNode) {
            // the last node insertion
            ListNode newNode = new ListNode(EndNode.prevN, TU, EndNode);
            EndNode.prevN.nextN = newNode;
            EndNode.prevN = newNode;
            size += 1;
        } else {
            // middle insertion
            ListNode newNode = new ListNode(CursorNode.nextN.prevN, TU, CursorNode.nextN);
            ListNode Temp = CursorNode.nextN.prevN;
            CursorNode.nextN.prevN = newNode;
            Temp.nextN = newNode;
            size += 1;
        }
    }

    public void delete(){
        if (CursorNode.nextN.prevN == Sentinel) {
            System.out.println("Nothing to delete!");
        } else if (CursorNode.nextN == EndNode) {
            // delete from the end
            EndNode.prevN.prevN.nextN = EndNode;
            EndNode.prevN = EndNode.prevN.prevN;
            size -= 1;

        } else {
            // delete from the middle
            CursorNode.nextN.prevN.prevN.nextN= CursorNode.nextN;
            CursorNode.nextN.prevN = CursorNode.nextN.prevN.prevN;
            size -= 1;
        }

    }

    public void CursorAndCursorNextSync() {
        int newX = ((TextUnit)CursorNode.nextN.getTextUnit()).getX();
        int newY = ((TextUnit)CursorNode.nextN.getTextUnit()).getY();
        ((TextUnit)CursorNode.getTextUnit()).updateX(newX);
        ((TextUnit)CursorNode.getTextUnit()).updateY(newY);
        TextRendering.CursorNodeSet(TextBuffer.FastTextList.CursorNode);
    }

    public void moveleft() {
        /*
        maybe also need specially handling for newline character
         */
        if (CursorNode.nextN.prevN != Sentinel) {
            CursorNode.nextN = CursorNode.nextN.prevN;
            CursorAndCursorNextSync();
            //System.out.println(TextBuffer.FastTextList.CursorNode.nextN.getTextUnit().getString());
        } else {
            System.out.println("Nothing more left to move...");
        }

    }

    public void moveright() {
        if (CursorNode.nextN != EndNode) {
            CursorNode.nextN = CursorNode.nextN.nextN;
            CursorAndCursorNextSync();

        }else {
            System.out.println("Reached end of the file");
        }

    }

    public void moveUp() {

        if (((TextUnit)CursorNode.nextN.getTextUnit()).getY() == 0) {
            /*
            while(!((TextUnit) CursorNode.nextN.prevN.getTextUnit()).getString().equals("Sentinel")) {
                CursorNode.nextN = CursorNode.nextN.prevN;
                CursorAndCursorNextSync();
                */
            CursorNode.nextN = Sentinel.nextN;
            CursorAndCursorNextSync();

        } else {
            int PosX = ((TextUnit) CursorNode.getTextUnit()).getX();
            CursorNode.nextN = CursorNode.nextN.prevN;
            CursorAndCursorNextSync();

            while(Math.abs((((TextUnit) CursorNode.getTextUnit()).getX() - PosX)) > CursorNextWidth()/2) {
                //System.out.println("Cursor Pos   " + ((TextUnit) CursorNode.getTextUnit()).getX());
                //System.out.println("Distance   "+Math.abs((((TextUnit) CursorNode.getTextUnit()).getX() - PosX)));
                    if ((((TextUnit) CursorNode.nextN.getTextUnit()).getString()).equals("NewLine")){
                        if (((TextUnit) CursorNode.nextN.getTextUnit()).getX() < PosX) {
                            CursorAndCursorNextSync();
                            break;
                        }
                    }
                if (CursorNode.nextN.prevN != Sentinel) {
                    CursorNode.nextN = CursorNode.nextN.prevN;
                    CursorAndCursorNextSync();
                } else {
                    CursorAndCursorNextSync();
                    break;
                }
            }
        }
    }


    public void moveDown() {
         if (((TextUnit)CursorNode.nextN.getTextUnit()).getY()
                 == ((TextUnit) EndNode.getTextUnit()).getY()) {
                while(!((TextUnit) CursorNode.nextN.getTextUnit()).getString().equals("EndNode")) {
                CursorNode.nextN = CursorNode.nextN.nextN;
                CursorAndCursorNextSync();
            }
         } else {
            int PosX = ((TextUnit) CursorNode.getTextUnit()).getX();
            System.out.println(PosX);
            CursorNode.nextN = CursorNode.nextN.nextN;
            CursorAndCursorNextSync();

            while(Math.abs((((TextUnit) CursorNode.getTextUnit()).getX() - PosX)) > CursorNextWidth()/2) {
                System.out.println("Cursor Pos   " + ((TextUnit) CursorNode.getTextUnit()).getX());
                //System.out.println("Distance   "+(((TextUnit) CursorNode.getTextUnit()).getX() - PosX));
                    if ((((TextUnit) CursorNode.nextN.getTextUnit()).getString()).equals("NewLine")){
                        System.out.println("Was at new line");
                        if (((TextUnit) CursorNode.nextN.getTextUnit()).getX() < PosX) {
                            CursorAndCursorNextSync();
                            break;
                        }
                    }

                    if ((((TextUnit) CursorNode.nextN.getTextUnit()).getString()).equals("EndNode")){
                        System.out.println("Stopped at EndNode!!");
                        CursorAndCursorNextSync();
                        break;
                    }
                CursorNode.nextN = CursorNode.nextN.nextN;
                CursorAndCursorNextSync();
            }

         }

    }

     public int CursorNextWidth(){

        if (((TextUnit) CursorNode.nextN.getTextUnit()).getString().equals("EndNode")) {
            return ((TextUnit) CursorNode.nextN.prevN.getTextUnit()).getWidth();
        }
        if (((TextUnit) CursorNode.nextN.getTextUnit()).getString().equals("NewLine")) {
            return ((TextUnit) CursorNode.nextN.prevN.getTextUnit()).getWidth();
        } else {
            return ((TextUnit) CursorNode.nextN.getTextUnit()).getWidth();
        }
    }

    /*
    public int CursorNextWidth(FastList.ListNode P){

        if (((TextUnit) P.nextN.getTextUnit()).getString().equals("EndNode")) {
            return ((TextUnit) P.nextN.prevN.getTextUnit()).getWidth();
        }
        if (((TextUnit) P.nextN.getTextUnit()).getString().equals("NewLine")) {
            return ((TextUnit) P.nextN.prevN.getTextUnit()).getWidth();
        } else {
            return ((TextUnit) P.nextN.getTextUnit()).getWidth();
        }
    }
    */

    public int size() {
        return size;
    }

    public void TempNodeCursorUpdate() {
        TextUnit newTU = new TextUnit(((TextUnit)CursorNode.getTextUnit()).getString(),
                ((TextUnit)CursorNode.getTextUnit()).getX(), ((TextUnit)CursorNode.getTextUnit()).getY());
        TempNode.TU = newTU;
        TempNode.nextN = CursorNode.nextN;
    }

    public void TempNodeSentinelUpdate() {
        TextUnit newTU = new TextUnit(((TextUnit)Sentinel.getTextUnit()).getString(),
                ((TextUnit)Sentinel.getTextUnit()).getX(), ((TextUnit)Sentinel.getTextUnit()).getY());
        TempNode.TU = newTU;
        TempNode.nextN = Sentinel.nextN;
    }

    public void WordWrapNodeCursorUpdate(){
        WordWrapNode.nextN = CursorNode.nextN;
    }


    public void CursortoBegin(){
        while(CursorNode.nextN.prevN != Sentinel){
            CursorNode.nextN = CursorNode.nextN.prevN;
        }
        CursorAndCursorNextSync();
    }

    public void WrapperTempNodeSync(){
       WordWrapNode.nextN = TempNode.nextN;
    }

    /*
    public static void main(String[] args) {
        FastList<Integer> FL = new FastList<>();
        for (int i = 0; i < 5; i += 1) {
            FL.add(i);
        }

        for (int i = 0; i < 3; i += 1) {
            FL.moveleft();
        }

        //FL.delete();
        //FL.delete();
        FL.delete();

        FL.delete();
    }
    */
}
