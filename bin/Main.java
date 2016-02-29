/**
 * The main driver of the program. This file will create the game, create the two agents,
 * and create the window for the game. After that, Connect4Frame runs everything.
 * 
 * You should only modify this class to change which agents are playing.
 */
public class Main
{
    public static void main(String[] args)
    {
        Connect4Game game = new Connect4Game(7, 6); // create the game; these sizes can be altered for larger or smaller games
        Agent redPlayer = new BeginnerAgent(game, true); // create the red player, any subclass of Agent
        Agent yellowPlayer = new MyAgent(game, false); // create the yellow player, any subclass of Agent
        
        Connect4Frame mainframe = new Connect4Frame(game, redPlayer, yellowPlayer); // create the game window
        
        //Test multiple times at once
        double winCount = 0;  //The number of wins so far in the test
        double gameCount = 0; //The number of games played so far
        double numTestGames = 100; //The number of games to be played
        char myColor = 'Y'; //The color you want to check wins for
        for(gameCount = 0; gameCount < numTestGames; gameCount++) //Play numTestGames number of games
        {
            mainframe.newGame(); //Create a new game
            if(mainframe.playToEnd() == myColor) //If the outcome is a win for myColor
            {
                winCount++; //Add a win to winCount
            }
        }
        double winRate = winCount / gameCount;  //Calculate average
        System.out.println("In " + gameCount + " games " + "I won " + winRate*100 + "% of the time"); //Output results
    }
}
    