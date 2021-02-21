/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author wakura
 * This application draws a coloured sketch-board on the screen
 * The user can sketch various images on the board by dragging the mouse on the 
 * drawing surface. The program also presents the user with a variety of 
 * colours to choose from and use in the drawing.
 */
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.input.MouseEvent;
public class ColouredSketchBoard extends Application{
    
    private final Color[] colorPallete = { Color.RED, Color.ORANGE, Color.YELLOW,
        Color.GREEN, Color.BLUE, Color.INDIGO, Color.VIOLET, Color.WHITE};      //This array stores the various colors used for drawing on the sketchBoard
    private Canvas canvas;      //drawing surface
    private GraphicsContext g;  //object used for drawing in the application
    private double width, height;   //the width and height of the canvas respectively
    private int currentlySelectedColNum;    //The colour currently selected for drawing coded as an index of the colorPallete Array
    private double prevX, prevY;         //The coordinates of the last known location where the MouseEvent occured
    private boolean dragging;       //Set true while dragging operation is in action
    
    
    /**
     *The main() method launches the application
     * @param args main() method arguments
     */
    public static void main(String[] args){
        launch();
    }
    
    /**
     * The start() method sets up the GUI and configures event handling in the application
     * The start() method makes the window visible
     * @param primaryStage The application's primary window
     */
    @Override
    public void start(Stage primaryStage){
        canvas = new Canvas(800, 700);      //construct the canvas object
        clearAndRedraw();
        
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseDragged(e -> mouseDragged(e));
        canvas.setOnMouseReleased(e -> mouseReleased());
        
        BorderPane root = new BorderPane(canvas);   //construct the root element in the scene graph
        primaryStage.setScene(new Scene(root));     //set the scene to be displayed in the applications window
        primaryStage.setTitle("Coloured SketchBoard");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    
    /**
     * This method clears the canvas and redraws it on the screen
     * It is called when the program is first launched and when the user clicks on the "CLEAR Button"
     */
    private void clearAndRedraw(){
        g = canvas.getGraphicsContext2D();
        width = canvas.getWidth();
        height = canvas.getHeight();        
       
        //fill the canvas with a white background
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, width, height);
        
        //Stroke a 3px grey border around the white drawing area
        g.setStroke(Color.GREY);
        g.setLineWidth(3);
        g.strokeRect(0, 0, width - 54, height);
        
        //draw the buttons
        int colSpacing = (int)height/9;             //The distance between the top left corner of the coloured rectangles on the right and the next rectangle's top left corner
                                                    //This is the height of each coloured rectangle
        for(int i = 0; i < 8; i++){
            g.setFill(colorPallete[i]);
            g.fillRect(width-56, i*colSpacing, 50, colSpacing);
            g.setStroke(Color.GREY);
            g.setLineWidth(2);
            g.strokeRect(width-54, i*colSpacing, 50, colSpacing);
        }
        
        //Draw the eraser button
        g.setFill(Color.MAROON);
        g.setFont(Font.font("Papyrus", FontPosture.REGULAR, 8));
        g.fillText("ERASER",width-50, (height-(colSpacing*2))+25);
        g.setStroke(Color.GRAY);
        g.strokeRect(width-54, 8*colSpacing, 50, colSpacing+5);
        g.fillText("CLEAR", width-50, (height-25));  //draw the clear button
    }
    
    private void changeColor(int y){
        int newColor;       //The new drawing color coded as an index of the array colorPallete
        int colorSpacing = (int)height/9;
        newColor  = y/colorSpacing;     //you now have the index of the new stroking color
        
        //check if it's a valid color
        if(newColor < 0 || newColor > 8)
            return;
        
        //clear the highlight on the current stroking color
        g.setStroke(Color.GREY);
        g.strokeRect(width-54,currentlySelectedColNum*colorSpacing, 50, colorSpacing );
        
        //highlight the new color
        currentlySelectedColNum = newColor;
        g.setStroke(Color.WHITE);
        g.strokeRect(width-54,currentlySelectedColNum*colorSpacing, 50, colorSpacing );
    }
    
    //............................Mouse Events Handling.........................
    
    private void mousePressed(MouseEvent evt){
        double startX, startY;      //The coordinates of the location of the mouse event
        
        /*check if we are processing a dragging event
        *If true, it means the user pressed a second mouse button without releasing the first
        *Ignore the second mouse press
        */
        if(dragging)
            return;
        //At this point, we are not processing a drag, either the user clicked on a button or within the sketching area
        startX = evt.getX();
        startY = evt.getY();
        
        if(startX > (width - 54)){      //user pressed on one of the colour buttons
           if(startY < height-54)       //user must have pressed the mouse button on either a color button or erase button
               changeColor((int)startY);
           else
               //user Clicked on clear button
               clearAndRedraw();
        }
        else {      //user pressed the mouse button in the sketching area
            dragging = true;
            prevX = startX;
            prevY = startY;
            
        }           
        
    }
    
    private void mouseDragged(MouseEvent evt){
        double x, y;   //The coordinates of the location of the occurance of the mouse event
        
        //check if we are processing a dragging event
        if(!dragging)
            return;     //we are not processing a dragging event so we return
        x = evt.getX();
        y = evt.getY();
        
        g.setStroke(colorPallete[currentlySelectedColNum]);
        g.setLineWidth(2);          //use 2pixels wide line for stroking
        g.strokeLine(prevX, prevY, x, y); //stroke the line
        
        //update the new values of prevX and prevY
        prevX = x;
        prevY = y;     
    }
    
    private void mouseReleased(){
        if(dragging){
            dragging = false;
        }
    }
}

