package TE;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import static TE.TextBuffer.FastTextList;
import static TE.TextBuffer.removeText;

public class Editor extends Application {

    static int WINDOW_WIDTH = 500;
    static int WINDOW_HEIGHT = 500;
    static final int STARTING_CURSOR_X = 5;
    static Integer CursorX = 5;
    static Integer CursorY = 0;
    static final int STARTING_FONT_SIZE = 25;
    static int fontSize = STARTING_FONT_SIZE;
    static String fontName = "Verdana";
    static String FileName;
    static int fileHeight = 0;
    static int REAL_WINDOW_WIDTH;
    //static TextUnit EndNode = new TextUnit(new Text(), "E", CursorX, CursorY);


    //TextBuffer TB = new TextBuffer();
    //TextRendering TR = new TextRendering();
    static Group root = new Group();
    static Group textRoot = new Group();
    static int LayoutY = 0;
    static ScrollBar scrollBar;


    static Rectangle Cursor = new Rectangle(1, 10);

    /** An EventHandler to handle keys that get pressed. */
    private class KeyEventHandler implements EventHandler<KeyEvent> {

        /** The Text to display on the screen. */

        Group rootCopy;

        KeyEventHandler(final Group root){
            rootCopy = root;
        }

        @Override
        public void handle(KeyEvent keyEvent) {

            System.out.println("ShortCutDownOtherOne");
            if (keyEvent.getEventType() == KeyEvent.KEY_TYPED && !keyEvent.getCode().equals("UNDEFINED")) {
                // Use the KEY_TYPED event rather than KEY_PRESSED for letter keys, because with
                // the KEY_TYPED event, javafx handles the "Shift" key and associated
                // capitalization.
                String characterTyped = keyEvent.getCharacter();
                if (characterTyped.length() > 0 && characterTyped.charAt(0) != 8
                        && characterTyped.charAt(0) != '\r') {
                    //System.out.println("hehe");
                    // Ignore control keys, which have non-zero length, as well as the backspace
                    // key, which is represented as a character of value = 8 on Windows.
                    Text T = new Text();
                    TextBuffer.addText(rootCopy, T, characterTyped);
                    keyEvent.consume();
                    //System.out.println(CursorX + "  " + CursorY);
                    //CursorSet(CursorX, CursorY);
                }
                if(fileHeight < WINDOW_HEIGHT) {}
                        else Editor.scrollBar.setMax(fileHeight - WINDOW_HEIGHT);

            } /* else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                // Arrow keys should be processed using the KEY_PRESSED event, because KEY_PRESSED
                // events have a code that we can check (KEY_TYPED events don't have an associated
                // KeyCode).
                Text T = new Text();
                KeyCode code = keyEvent.getCode();
                if (code == KeyCode.UP) {

                    FastTextList.moveUp();
                }
                if (code == KeyCode.DOWN) {
                    FastTextList.moveDown();
                }
                if (code == KeyCode.LEFT) {
                    FastTextList.moveleft();
                }
                if (code == KeyCode.RIGHT) {
                    FastTextList.moveright();
                }


                if (code == KeyCode.BACK_SPACE) {
                    TextBuffer.removeText(rootCopy);
                }
                if (code == KeyCode.ENTER) {
                    TextBuffer.addNewLine( "NewLine");
               }
                if (keyEvent.isShortcutDown()) {
                     System.out.println("Shortcuti!");
                     if (keyEvent.getCode() == KeyCode.S) {
                         System.out.println("Writing file...");
                         Saver.write(FastTextList.Sentinel);
                     }
                }

                if (keyEvent.isShortcutDown()) {
                    if (keyEvent.getCode() == KeyCode.P) {
                        System.out.println("printing cursor Position  " +
                                FastTextList.CursorNode.getTextUnit().getX() + "   " +
                                FastTextList.CursorNode.getTextUnit().getY() + "   " +
                                FastTextList.CursorNode.nextN.getTextUnit().getString());
                    }
                }

                if (keyEvent.isShortcutDown()) {
                    if (keyEvent.getCode() == KeyCode.MINUS){
                        TextRendering.DecreaseFontSize();
                    }

                    if (keyEvent.getCode() == KeyCode.B) {

                        TextRendering.IncreaseFontSize();
                    }

                    if (keyEvent.getCode() == KeyCode.UP) {
                        LayoutY -= 10;
                        textRoot.setLayoutY(LayoutY);
                        System.out.println(textRoot.getLayoutX() + "   " + textRoot.getLayoutY());
                    }

                    if (keyEvent.getCode() == KeyCode.DOWN) {
                        if (LayoutY < 0) {
                            LayoutY += 10;
                        }
                        textRoot.setLayoutY(LayoutY);
                        System.out.println(textRoot.getLayoutX() + "   " + textRoot.getLayoutY());
                    }
                }
            }*/
        }

    }




    /** An EventHandler to handle changing the color of the rectangle. */
    private class RectangleBlinkEventHandler implements EventHandler<ActionEvent> {
        private int currentColorIndex = 0;
        private Color[] boxColors =
                {Color.BLACK, Color.WHITE};

        RectangleBlinkEventHandler() {
            // Set the color to be the first color in the list.
            changeColor();
        }

        private void changeColor() {
            Cursor.setFill(boxColors[currentColorIndex]);
            currentColorIndex = (currentColorIndex + 1) % boxColors.length;
        }

        @Override
        public void handle(ActionEvent event) {
            changeColor();
        }
    }

    /** Makes the text bounding box change color periodically. */
    public void makeCursorColorChange() {
        // Create a Timeline that will call the "handle" function of RectangleBlinkEventHandler
        // every 1 second.
        final Timeline timeline = new Timeline();
        // The rectangle should continue blinking forever.
        timeline.setCycleCount(Timeline.INDEFINITE);
        RectangleBlinkEventHandler cursorChange = new RectangleBlinkEventHandler();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), cursorChange);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    /**
     * class for mouse Event
     */
    private class MouseClickEvent implements EventHandler<MouseEvent> {
        private Scene SceneCopy;
        MouseClickEvent (Scene S) {
            SceneCopy = S;
        }
        @Override
        public void handle(MouseEvent event){
           int curX = (int) event.getSceneX();
           int curY = (int) event.getSceneY();
           System.out.println(SceneCopy.getWidth());
           System.out.println(curX + "   " + curY);
           //here it actually need to search for the position within TextBuffer
        }
    }


    // maybe event class cannot be separated from this editor class
    @Override
    public void start(Stage primaryStage) {
        scrollBar = new ScrollBar();
        scrollBar.setOrientation(Orientation.VERTICAL);
        scrollBar.setPrefHeight(WINDOW_HEIGHT);
        scrollBar.setMin(0);
        scrollBar.setMax(fileHeight);
        REAL_WINDOW_WIDTH = WINDOW_WIDTH + (int) Math.round(
                scrollBar.getLayoutBounds().getWidth());

        Scene scene = new Scene(root, REAL_WINDOW_WIDTH, WINDOW_HEIGHT, Color.WHITE);
        root.getChildren().add(textRoot);
        scrollBar.setLayoutX(WINDOW_WIDTH);

        /*
        ScrollBar
        */
        root.getChildren().add(scrollBar);

        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                long placeHolder = Math.round((double) newValue);
                int temp = (int) placeHolder;
                    System.out.println(temp);
                textRoot.setLayoutY(-temp);
            }
        });

        EventHandler<MouseEvent> MouseClickEvent =
                new Editor.MouseClickEvent(scene);

        scene.setOnMouseClicked(MouseClickEvent);
        EventHandler<KeyEvent> keyEventHandler =
                new Editor.KeyEventHandler(textRoot);
        // Register the event handler to be called for all KEY_PRESSED and KEY_TYPED events.
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                Text T = new Text();
                KeyCode code = keyEvent.getCode();
                if (code == KeyCode.UP) {

                    FastTextList.moveUp();
                }
                if (code == KeyCode.DOWN) {
                    FastTextList.moveDown();
                }
                if (code == KeyCode.LEFT) {
                    FastTextList.moveleft();
                }
                if (code == KeyCode.RIGHT) {
                    FastTextList.moveright();
                }
                if (code == KeyCode.BACK_SPACE) {
                    removeText(textRoot);
                }
                if (code == KeyCode.ENTER) {
                    TextBuffer.addNewLine( "NewLine");
               }

                if (keyEvent.isShortcutDown()) {
                    System.out.println("ShortCutDOwn");
                    if (keyEvent.getCode() == KeyCode.MINUS){
                        TextRendering.DecreaseFontSize();
                    }
                    if (keyEvent.getCode() == KeyCode.S) {
                         System.out.println("Writing file...");
                         Saver.write(FastTextList.Sentinel);
                     }
                    if (keyEvent.getCode() == KeyCode.P) {
                        System.out.println("printing cursor Position  " +
                                FastTextList.CursorNode.getTextUnit().getX() + "   " +
                                FastTextList.CursorNode.getTextUnit().getY() + "   " +
                                FastTextList.CursorNode.nextN.getTextUnit().getString());
                    }
                    if (keyEvent.getCode() == KeyCode.B) {

                        TextRendering.IncreaseFontSize();
                    }

                    if (keyEvent.getCode() == KeyCode.UP) {
                        LayoutY -= 10;
                        textRoot.setLayoutY(LayoutY);
                        System.out.println(textRoot.getLayoutX() + "   " + textRoot.getLayoutY());
                    }

                    if (keyEvent.getCode() == KeyCode.DOWN) {
                        if (LayoutY < 0) {
                            LayoutY += 10;
                        }
                        textRoot.setLayoutY(LayoutY);
                        System.out.println(textRoot.getLayoutX() + "   " + textRoot.getLayoutY());
                    }
                }
                        if(fileHeight < WINDOW_HEIGHT) {}
                        else Editor.scrollBar.setMax(fileHeight - WINDOW_HEIGHT);

            }
        });
        //scene.setOnKeyPressed(keyEventHandler);
        primaryStage.setTitle("TE");

        textRoot.getChildren().add(Cursor);
        makeCursorColorChange();
        /**
         * set initial Cursor display // Finally
         */
        Cursor.setX(CursorX);
        Cursor.setY(CursorY);
        Cursor.setHeight(STARTING_FONT_SIZE);

        primaryStage.setScene(scene);
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
               long placeHolder = Math.round((double) newValue);
               REAL_WINDOW_WIDTH = (int) placeHolder;
               WINDOW_WIDTH = REAL_WINDOW_WIDTH - (int) Math.round(
                scrollBar.getLayoutBounds().getWidth());
               scrollBar.setLayoutX(WINDOW_WIDTH);
               TE.TextRendering.WindowResize();
            }
        });

        primaryStage.show();

    }

    // This is boilerplate, necessary to setup the window where things are displayed.
    public static void main(String[] args) {
        TE.Opener.open(args, textRoot);
        launch(args);
    }
}