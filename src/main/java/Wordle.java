import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import javafx.scene.control.Alert;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Wordle {
    //Global Variables
    String guessWord;
    boolean isWin;
    int count;
    int currentRow = 0;
    
    
    
    String[] words = readArray("src/main/resources/wordList.txt");
    Random rand = new Random();
    int randomIndex = rand.nextInt(words.length); // generate a random word between 0 and end of list

    String correctWord = words[randomIndex]; // get the word at the random spot
    String[] guessArray = new String[5];
    char[] correctArray = correctWord.toCharArray();
    

    @FXML
    Label Label1,Label2,Label3,Label4,Label5;
 
    @FXML
    VBox root;
    
    @FXML
    AnchorPane Pane;

    @FXML
    Text Title;

    @FXML
    HBox row1,row2,row3,row4,row5;


    HBox[] row = new HBox[5];

    @FXML
    
    //Sets up the game
    private void initialize()  {
        root.setPrefSize(700, 750);
        Pane.setPrefSize(700, 750);
        //String[] words = readArray("src/main/resources/wordList.txt");
        isWin = false;
        System.out.println(correctWord); 
        
        char[] ch = new char[correctWord.length()];

        for (int i = 0; i < correctWord.length(); i++) {
            ch[i] = correctWord.charAt(i);
        }
        for (int i = 0; i< 5;i++ ){ 
            System.out.println(ch[i]);
        }

        row[0] = row1;
        row[1] = row2;
        row[2] = row3;
        row[3] = row4;
        row[4] = row5;
        //row[5] = row5;
  
      

    }

    @FXML
    /**
     * Handles keyboard input for the game, setting the text of the label to the pressed key, checking if the entered word is valid,
     * and checking if the row is full. If the row is full and the entered word is valid, the guess is checked and the current row is increased.
     * If the user presses "backspace", all labels in the current row are cleared. If the user enters "enter" and the game is won, the
     * gameWon() method is called. 
     * @param keyEvent the KeyEvent for the pressed key
     * @throws IOException if there is an error reading the word file
     */
    public void keyHandler(KeyEvent keyEvent) throws IOException{
        
        String key = keyEvent.getCode().getName().toLowerCase();
        System.out.println(key);  
        
        //check key inputted by user
        
        if (("enter".equals(key)) && (wordValid() == true) && (rowFull()== true)&&(currentRow<=5)) {

            guessCheck();
            //System.out.println(correctArray);
            currentRow++;
            System.out.println(currentRow);
            }else{

                if (("backspace".equals(key))&& (currentRow<=4)) {
                    for (Node child: row[currentRow].getChildren()){
                        Label temp = (Label)child;
                        temp.setText("");
                        
                }
            }
        System.out.println("correctWord: " + correctWord + ", guessWord: " + guessWord);


        if (currentRow<=5){
            setText(key);
        }
         wordValid();
         //rowFull();
        }
    }

 


    /**
     * Method to check the user's guess and update the UI with correct and incorrect letters in different colors.
     * It first retrieves the current guess from the UI and compares each letter with the correct word.
     * If a letter matches and is in the correct position, the label's background color is set to green.
     * If a letter matches but is not in the correct position, the label's background color is set to yellow.
     * If the entire guess matches the correct word, the game is won and the isWin variable is set to true.
     */
    public void guessCheck(){
        
        StringBuilder sb = new StringBuilder();
        for (Node child : row[currentRow].getChildren()) {
            Label label = (Label) child;
            sb.append(label.getText());
        }

        String guessWord = sb.toString();
        char[] wordChars = correctWord.toCharArray();
        char[] guessChars = guessWord.toCharArray();

        for (int i = 0; i < 5; i++) {
            
            if (wordChars[i] == guessChars[i]) {
                
                Label label = (Label)row[currentRow].getChildren().get(i);
                label.setStyle("-fx-background-color: green;");
                wordChars[i] = '#';
                guessChars[i] = '*';
                
            } 
        }

        String word2 = String.valueOf(wordChars);
        String guess2 = String.valueOf(guessChars);

        for (int i =0; i < 5; i++){
            if (word2.contains(guess2.substring(i, i+1))){
                int temp = correctWord.indexOf(guess2.substring(i, i+1));

                wordChars = word2.toCharArray();
                guessChars = guess2.toCharArray();

                wordChars[temp]='#';
                guessChars[i] = '*';

                Label label = (Label)row[currentRow].getChildren().get(i);
                label.setStyle("-fx-background-color: yellow;");

                word2 = String.valueOf(wordChars);
                guess2 = String.valueOf(guessChars);
       
            }
            System.out.println("correctWord: " + correctWord + ", guessWord: " + guessWord);
            if (correctWord.equals(guessWord)) {
                displayWinnerPopup();
            }
        }

        
    

        }




    /**
    Sets the text of an empty Label child node in the current row of the Hbox
    with the provided key string.
    @param key a string to set as the text of an empty Label child node
    */
    private void setText(String key){
       if (currentRow<=4){
        for (Node child: row[currentRow].getChildren()){
            Label temp = (Label)child;
            //Check if key is a letter
            if ((key.matches ("[a-zA-Z]"))&&(currentRow<=4)){
                if (temp.getText().isBlank()){
                    temp.setText(key);
                    break;
            }  
        }
    }
    }
    }
    /**
     * Checks if the current row is full by iterating through all the children nodes of the current row 
     * and checking if their corresponding Label objects have non-blank text. 
     * 
     * @return true if the row is full (all labels are filled with text), otherwise false 
     * @throws IOException if an I/O error occurs while accessing the Label objects
     */
    private boolean rowFull() throws IOException {
        boolean rowFull = false;
        if (currentRow<5){
        for (Node child: row[currentRow].getChildren()){
            Label temp = (Label)child;
                if (temp.getText().isBlank()){
                    rowFull = false;
                    
                } else {
                    rowFull = true;
                    
                }
    }
        
    }   return rowFull; 
    } 
    /**
     * Displays a pop-up window notifying the user that they have won the game.
     * The pop-up window contains a title, a header text, and a message.
     * This method creates an Alert object of type INFORMATION and sets the title .
     * it shows the pop-up window telling user to finish game.
     */
    public void displayWinnerPopup() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Winner!");
        alert.setHeaderText(null);
        alert.setContentText("Congratulations, you guessed the word!");
        alert.showAndWait();
    }

    /**
    Checks whether the current row in the game board is completely filled with letters or not.
    Also Checks to see if the word exists in the word list, this is what creates a valid word.
    If the row is completely filled, and the word is in the list it returns true. Otherwise, it returns false.
    @return boolean - true if the current row is completely filled with letters, false otherwise.
    @throws IOException - If an I/O error occurs.
    */
    public boolean wordValid() {
    //use stringbuilder to create string
        StringBuilder sb = new StringBuilder();
        Boolean isValid = false;
            
        if(currentRow<5){
        for (Node child: row[currentRow].getChildren()){
            sb.append(((Label) child).getText());
        }
        String guess = sb.toString().toLowerCase();
        
        if (Arrays.asList(words).contains(guess)){
            isValid = true;
           
        } else {
            isValid = false;
           
        }
           return isValid;

    }return isValid;
    }

    /**
     * Reads a text file and returns its content as an array of Strings, where each line of the file
     * corresponds to an element of the array.
     * @param file the name/path of the file
     * @return an array of Strings containing the content of the file(WordList)
     */
    public static String[] readArray(String file) {
        int ctr = 0;
        try{
            Scanner s1 = new Scanner(new File(file));
            while(s1.hasNextLine()){
                ctr++;
                s1.nextLine();
            }
            String[] words = new String[ctr];
    
            Scanner s2 = new Scanner(new File(file));
            for ( int i = 0; i < ctr; i++){
                words[i] = s2.nextLine();
            }
            
            return words;
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    





}
