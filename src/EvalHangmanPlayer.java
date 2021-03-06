import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.text.DecimalFormat;
import java.lang.management.*;

/*

  Author: Taher Patanwala
  Email: tpatanwala2016@my.fit.edu
  Pseudocode: Philip Chan

  Usage: EvalHangmanPlayer wordFile hiddenWordFile 

  Description:

  The goal is to evaluate HangmanPlayer.  For each hidden word in the
  hiddenWordFile, the program asks HangmanPlayer for guesses for
  letters in the hidden word.  As in the Hangman game, the maximum
  number of incorrect guesses is 6 (body parts).  HangmanPlayer is
  provided with a list of known English words in wordFile for
  initialization.

  The performance of HangmanPlayer is measured by:

  a.  accuracy: average accuracy over the hidden words;
           the accuracy of each hidden words is 
	        [ 1 - (number of incorrect guesses/6) ] * 100%.
  b.  speed: average time to guess a letter
  c.  space consumption: memory consumption
  d.  overall score--(accuracy * accuracy)/sqrt(time * memory)  


  --------Pseudocode for evaluating HangmanPlayer---------------

     HangmanPlayer player = new HangmanPlayer(wordFile) // a list of English words

     while not end of hiddenWordFile
        read a hiddenWord
	currentWord = blank word with the same length as hiddenWord

	while not maxGuesses of 6 is not reached and currentWord is not hiddenWord
	   correctGuess = false
           guess = player.guess(currenWord) 
	   if guess is in hiddenWord
              correctGuess = true
	      update currentWord
	   player.feedback(correctGuess, currentWord)

     report performance
 */

public class EvalHangmanPlayer
{
    public static void main(String[]args) throws IOException{

	if (args.length != 2) 
        {
            System.err.println("Usage: EvalHangmanPlayer wordFile hiddenWordFile");
            System.exit(-1);
        }

	// for getting cpu time
	ThreadMXBean bean = ManagementFactory.getThreadMXBean();        
	if (!bean.isCurrentThreadCpuTimeSupported())
	    {
		System.err.println("cpu time not supported, use wall-clock time:");
                System.err.println("Use System.nanoTime() instead of bean.getCurrentThreadCpuTime()");
		System.exit(-1);
	    }
	    
        //Preprocessing in HangmanPlayer
	System.out.println("Preprocessing in HangmanPlayer...");
        long startPreProcTime = bean.getCurrentThreadCpuTime();
        HangmanPlayer player = new HangmanPlayer(args[0]);
        long endPreProcTime = bean.getCurrentThreadCpuTime();
        
        //Stop if program runs for more than 15 minutes.
        double processingTimeInSec = (endPreProcTime - startPreProcTime)/1E9;
        if(processingTimeInSec > 900){
            System.err.println("Preprocessing time \""+ processingTimeInSec +" sec\" is too long...");
            System.exit(-1);
        }
            
	// report time and memory spent on preprocessing
        DecimalFormat df = new DecimalFormat("0.####E0");
	System.out.println("Preprocessing in seconds (not part of score): "  + 
			   df.format(processingTimeInSec));
        Runtime runtime = Runtime.getRuntime();
	runtime.gc();
        System.out.println("Used memory after preprocessing in bytes (not part of score): " + (runtime.totalMemory() - runtime.freeMemory()));


        File hiddenWordFile = new File(args[1]);
        Scanner input = new Scanner(hiddenWordFile);
        
        double totalWords = 0.0;
        double totalGuessess = 0.0;
        double totalElapsedTime = 0.0;
        
	System.out.println("HangmanPlayer is guessing...");
        //Perform operations for each line in the file
        double accuracySum = 0.0;
        while(input.hasNextLine()){
            //Read a word from the hidden test file
            String hiddenWord = input.nextLine().trim().toLowerCase();
            //Count the total hidden words
            totalWords++;
            //Create an empty string as the same size as the hidden word
            StringBuilder wordWithGuessedLetters = new StringBuilder(hiddenWord.length());
            //Fill the empty string with spaces
            for(int i=0;i<hiddenWord.length();i++){
                wordWithGuessedLetters.append(" ");
            }
            //To indicate that your program now has to guess a new hidden word
            boolean newWord = true;
            //To count the number of incorrect guesses
            int numIncorrectGuesses = 0;
            boolean correctGuess = false;
            //While correct word is not guessed and the number of incorrect guesses is less than 6,
            //Your program will keep trying to guess
            while(numIncorrectGuesses < 6 && !correctGuess){
                totalGuessess++;
                //Record start time of the guess
                long startTime = bean.getCurrentThreadCpuTime();
                //Pass the partial word to the HangmanPlayer program to guess a letter
                char guessedLetter = player.guess(wordWithGuessedLetters.toString(), newWord);
                //To calculate the time taken for each guess operation
                long endTime = bean.getCurrentThreadCpuTime();
                totalElapsedTime = totalElapsedTime + (endTime - startTime);

                newWord = false;
                int i=0;
                //Check if guessLetter was not guessed before
                if(wordWithGuessedLetters.indexOf(String.valueOf(guessedLetter)) == -1){
                    //Try to see if the guessed letter is correct, 
                    //If Yes, then find the first position of the letter in the word
                    while(i<hiddenWord.length() && hiddenWord.charAt(i) != guessedLetter){
                        i++;
                    }
                }
                else{
                    i = hiddenWord.length();
                }
                //This means that the guess was incorrect
                if(i == hiddenWord.length()){
                    //Increment the number of incorrect guesses
                    numIncorrectGuesses++;
                    //Calculate the time taken to process the feedback
                    startTime = bean.getCurrentThreadCpuTime();
                    //Send feedback that the guess was wrong
                    player.feedback(false, wordWithGuessedLetters.toString());
		    endTime = bean.getCurrentThreadCpuTime();
		    totalElapsedTime = totalElapsedTime + (endTime - startTime);
                }
                //This means that the guess was correct
                else{
                    //Find other positions of the guessed character in the hidden word
                    while(i<hiddenWord.length()){
                        //The guessed letter is revealed in its correct positions in the word
                        if(hiddenWord.charAt(i) == guessedLetter){
                            wordWithGuessedLetters.setCharAt(i, guessedLetter);
                        }
                        i++;
                    }
                    
                    //If all letters are guessed of the hidden word, then proceed with the next hidden word
                    if(wordWithGuessedLetters.indexOf(" ") == -1)
                        correctGuess = true;
                    //Calculate the time taken to process the feedback
                    startTime = bean.getCurrentThreadCpuTime();
                    //Send feedback that correct letter was guessed
                    player.feedback(true, wordWithGuessedLetters.toString());
		    endTime = bean.getCurrentThreadCpuTime();
		    totalElapsedTime = totalElapsedTime + (endTime - startTime);
                }
            }
            //Total accuracy is calculated before moving to the next hidden word
            accuracySum += (1.0 - numIncorrectGuesses/6.0);
        }
	input.close();
	
        //Calculate the accuracy
        double avgAccuracy = (accuracySum * 100.0) / totalWords;
        System.out.printf("Accuracy: %.4f\n",avgAccuracy);
        
        //Convert elapsed time into seconds, and calculate the Average time
        double avgTime = (totalElapsedTime/1.0E9)/totalGuessess;
        
        //To format the Average time upto 4 decimal places.
        //DecimalFormat df = new DecimalFormat("0.####E0"); // moved to near initialization
        System.out.println("Average time per guess in seconds: " + df.format(avgTime));
        
        // Get the Java runtime
        // Runtime runtime = Runtime.getRuntime();  // moved to near initialization
        // Run the garbage collector
        runtime.gc();
        // Calculate the used memory
        long memory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory in bytes: " + memory);
        //OverAll Score
        System.out.printf("Overall Score: %.4f\n",(avgAccuracy * avgAccuracy)/Math.sqrt(avgTime * memory));

	HangmanPlayer player2 = player;  // keep player used to avoid garbage collection of player
    }
}