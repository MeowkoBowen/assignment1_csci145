//Authors: Huakai Zeng, Griffin Stofer, Garrett Strawn, Bowen Li

//Date: 15th july 2019


//Description: Hangman_group_assignment.


import java.util.Scanner;

public class Hangman {


    private static final boolean testingMode = true;


    public static void main(String[] args) {


        //onOff switch for while loop 0 for on 1 for off

        int onOff = 0;


        //create a winning word


        RandomWord word = new RandomWord();
        String winningWord = word.newWord();



        String dashes = dash(winningWord);


        char schoice = SelectorLevel();

        //innitial value 1 prevents the while loop to jump to losing immediately
        int numOfAttempts = 1;

        int spaceSize = 0;

        int[] spaces = new int[spaceSize];


        //charArr use to reveal the letter guessed correctly
        char[] charArr = new char[dashes.length()];

        charArr = dashes.toCharArray();


        //set conditions for number of attempts and spaces allocation given the choice of difficulties

        while (onOff == 0) {

            //test to see what is the winningWord
            if(testingMode) {
                System.out.println("Secret word: " + winningWord);
            }


            switch (schoice) {




                case ('e'):

                    numOfAttempts = 15;

                    spaceSize = 4;


                    //update dashes aka unveiling


                    //dashes = runGame(winningWord, numOfAttempts, spaces, dashes, charArr, spaceSize);


                    break;

                case ('i'):


                    numOfAttempts = 12;

                    //create an array of size 3 for space check

                    spaceSize = 3;

                    break;

                case ('h'):

                    numOfAttempts = 10;

                    spaceSize = 2;

                    break;

                default:
                    System.out.println("Invalid difficulty. Try Again...");
                    schoice = SelectorLevel();
                    break;
            }


            while (numOfAttempts > 0 && !dashes.equals(winningWord)  ) {

                spaces = new int[spaceSize];

                Scanner input = new Scanner(System.in);


                //prompt for player to guess character
                System.out.println("please enter a character: ");
                String userGuess = input.nextLine();
                if(userGuess.equals("solve")) {
                    System.out.print("Please solve the word: ");
                    userGuess = input.nextLine();
                    if (userGuess.equals(winningWord)) {
                        dashes = winningWord;
                        break;
                    } else {
                        System.out.println("That is not the secret word.");
                        numOfAttempts--;
                        System.out.println("Guesses Remaining: " + numOfAttempts);
                    }
                } else {
                    //convert to char
                    char userGuessChar = userGuess.charAt(0);

                    //prompt user for space check
                    System.out.print("please select the spaces you want to check (seperated by spaces): ");
                    String num = input.nextLine();
                    num = num.replaceAll(" ", "");


                    if(num.length() == spaceSize) {
                        for (int i = 0; i < num.length(); i++) {
                            int k = Character.getNumericValue(num.charAt(i));
                            spaces[i] = k;
                        }

                        int given = num.length();
                        int givenSpace = spaces.length;

                        if ( validInputCheckGame(given,spaceSize)  ) {

                            //helper variable
                            int checks = 0;


                            //make sure that the index of user's choice is within the length of the winningword
                            for(int i = 0; i< spaceSize; i++) {
                                if (spaces[i] < winningWord.length()) {  
                                    if (winningWord.charAt(spaces[i]) == userGuessChar) {
                                        dashes = reveal(dashes, userGuessChar, spaces[i]);
                                        //helper variable
                                        checks++;

                                    }

                                //print invalid input and reprompt for spaces
                                }  else {
                                    System.out.println("Your input is invalid");
                                    System.out.println("Guesses Remaining " + numOfAttempts);
                                    checks = -1;
                                    break;
                                }
                                //critical############# reveals the guessed word                      
                            }
                            //meaning none of my spaces match
                            if (checks == 0) {
                                System.out.println("Your letter was not found in the spaces you provided.");
                                numOfAttempts--;
                                System.out.println("Guesses Remaining " + numOfAttempts);
                            } else if (checks > 0){
                                System.out.println("The updated word is " + dashes);
                                System.out.println("Guesses Remaining " + numOfAttempts);
                            } else {
                                //do nothing
                            }
                        } else {
                            System.out.println("Your input is not valid. Try again.");
                            System.out.println("Guesses remaining: " + numOfAttempts);
                        }
                    }else{
                        System.out.println("Your input is invalid");
                        System.out.println("Guesses remaining: " + numOfAttempts);
                    } 
                }
            }

            if (numOfAttempts > 0 && dashes.equals(winningWord)) {
                Scanner input = new Scanner(System.in);
                System.out.println("You have guessed the word! Congratulations");
                System.out.println("Would you like to play again? Yes(y) or No(n)");
                String choice = input.nextLine();
                choice = choice.toLowerCase();


                char choiceChar = choice.charAt(0);

                if (choiceChar == 'y') {
                    schoice = SelectorLevel();

                    //generate new randomword

                    winningWord = RandomWord.newWord();

                    dashes = dash(winningWord);


                } else if (choiceChar == 'n') {
                    onOff = 1;
                    System.out.println("Goodbye!");

                    break;
                }

                //player has failed all attempts to guessed the word
            } else if (numOfAttempts == 0 && !dashes.equals(winningWord)) {

                //user
                Scanner input = new Scanner(System.in);
                System.out.println("You have failed to guess the word... :(");
                System.out.println("Would you like to play again? Yes(y) or No(n)");
                String choice = input.nextLine();
                choice = choice.toLowerCase();


                char choiceChar = choice.charAt(0);

                if (choiceChar == 'y') {
                    schoice = SelectorLevel();

                    //generate new randomword

                    winningWord = RandomWord.newWord();



                    dashes = dash(winningWord);





                } else if (choiceChar == 'n') {
                    onOff = 1;
                    System.out.println("Goodbye!");
                }


            }
        }
    }


    //create a dash. Serves as a visual display of dashes with the size of winning word

    public static String dash(String n) {

        StringBuilder kilo = new StringBuilder(n);

        for (int i = 0; i < n.length(); i++) {
            if (n.charAt(i) != '-') {
                kilo.setCharAt(i, '-');
            } else {
                kilo.setCharAt(i, n.charAt(i));
            }
        }

        String result = kilo.toString();

        System.out.println(result);

        return result;

    }


    //reveal winning character. VOID returns nothing just for show
    public static String reveal(String n, char c, int index) {

        StringBuilder kilo = new StringBuilder(n);

        int position = n.indexOf(c);


        kilo.setCharAt(index, c);


        //System.out.println(kilo);

        String result = kilo.toString();
        return result;

    }


    public static char SelectorLevel() {

        Scanner input = new Scanner(System.in);


        System.out.println("please select a valid difficulty level.");


        System.out.println("Please select the difficulty level (E)easy, (I)intermediate, (H)hard: ");


        String selection = input.nextLine();
        //make it case insensitive
        selection = selection.toLowerCase();

        char selectionChar = selection.charAt(0);


        if (selectionChar == 'e') {

            return 'e';


        } else if (selectionChar == 'i') {
            return 'i';


        } else if (selectionChar == 'h') {


            return 'h';


        } else {

            //for invalid selection level

            return 'P';
        }
    }








    //check if user input for spaces is legal or not
    public static boolean validInputCheckGame(int given, int allocated) {
        //if input is less or more than allocated space checks, return false.
        //print invalid input
        if (given != allocated) {
           // System.out.println("Your input is not valid. Try again.");
            return false;
        } else {
            return true;
        }
    }
}

















