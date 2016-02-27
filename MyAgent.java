import java.util.Random;

public class MyAgent extends Agent
{
    Random r;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game.  
     * Will attempt to win the game if possible, then block if possible, 
     * and then will run through a series of best moves.  If all else fails
     * then it will do a random move.
     */
    public void move()
    {
        if(this.iCanWin() != -1) //If I can win
        {
            moveOnColumn(iCanWin()); //Place winning piece
        }
        else if(this.theyCanWin() != -1) //Else if they can win
        {
            moveOnColumn(theyCanWin()); //Place blocking piece
        }
        else if(this.stackUp() != -1) //else if I can stack vertically
        {
            moveOnColumn(stackUp());
        }
        else if(this.stackRight() != -1) //else if I can streak horizontally right
        {
            moveOnColumn(stackRight());
        }

        else if(this.stackLeft() != -1) //else if I can streak horizontally leftt
        {
            moveOnColumn(stackLeft());
        }
        else //else make a random move
        {
            moveOnColumn(randomMove());
        }
    }

    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                          // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }
    
    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;
    }
    
    /**
     * Returns the index of a column which can be used to stack on top of.
     * Used for strategically building streaks of colors.
     * 
     * @return the column which a move would result in a vertical streak.
     */
    public int stackUp()
    {
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col)); //The lowest empty spot in the column
            if(lowestEmpty != 5) //Skip if the lowest spot is the bottom
            {
                Connect4Slot top = myGame.getColumn(col).getSlot(lowestEmpty + 1); //The top piece in the column
                if (iAmRed) //If i'm the red player
                {
                    if(top.getIsRed() && lowestEmpty > 0 && top != null) //If the top piece is red, the column is not full and there is a top piece
                    {
                        return col; //Return this column
                    }
                }
                else //I'm the yellow player
                {
                    if(top.getIsYellow() && lowestEmpty > 0 && top != null) //If the top piece is yellow, the column is not full and there is a top piece
                    {
                        return col; //Return this column
                    }
                }
            }
        }    
        return -1;  //There is no possible stackUp opportunities
    }
    
    /**
     * Returns the index of a column which can be used to stack to the left of.
     * Used for strategically building streaks of colors.
     * 
     * @return the column which a move would result in a horizontal streak
     */
    public int stackLeft()
    {
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col)); //The lowest empty spot in the column
            if(col + 1 < myGame.getColumnCount() && lowestEmpty != -1)  //If there is a column to the right, and this column is not full
            {
                Connect4Slot right = myGame.getColumn(col + 1).getSlot(lowestEmpty); //The slot to the right
                if (iAmRed) //If i'm the red player
                {
                    if(right.getIsRed() && right != null) //The slot to the right is red and right is not empty
                    {
                        return col; //return this column
                    }
                }
                else //I'm the yellow player
                {
                    if(right.getIsYellow() && right != null) //the slot to the right is yellow and right is not empty
                    {
                        return col; //return this column
                    }
                }
            }
        }
        return -1; //There is no possible stackLeft opportunities
    }
    
    /**
     * Returns the index of a column which can be used to stack to the right of.
     * Used for strategically building streaks of colors.
     * 
     * @return the column which a move would result in a horizontal streak
     */
    public int stackRight()
    {
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col)); //The lowest empty spot in the column 
            if(col - 1 > 0 && lowestEmpty != -1) //If there is a column to the left, and this column is not full
            {
                Connect4Slot left = myGame.getColumn(col - 1).getSlot(lowestEmpty); //The slot to the left
                if (iAmRed) //If i'm the red player
                {
                    if(left.getIsRed() && left != null) //The slot to the les is red and left is not empty
                    {
                        return col; //return this column
                    }
                }
                else //I'm the yellow player
                {
                    if(left.getIsYellow() && left != null) //The slot to the left is red and left is not empty
                    {
                        return col; //return this column
                    }
                }
            }
        }
        return -1; //There is no possible stackRight opportunities
    }
    
    /**
     * Returns the column that would allow the agent to win.  Looks for vertical wins first
     * then for wins to the right and finall wins to the left.
     *
     * @return the column that would allow the agent to win.  Returns -1 if no such move exists.
     */
    public int iCanWin()
    {
        //Vertical look - Checks to see if there is a possible vertical win
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col));  //The lowest empty spot in the column
            Connect4Slot top = myGame.getColumn(col).getSlot(lowestEmpty + 1); //The top slot in the column
            Connect4Slot second = myGame.getColumn(col).getSlot(lowestEmpty + 2); //The second to last slot in the column
            Connect4Slot third = myGame.getColumn(col).getSlot(lowestEmpty + 3); //The last slot in the column
            if (iAmRed) //If i'm the red player
            {
                if(lowestEmpty > 0 && top != null && second != null && third != null //If the column is not empty, the top, second, and third values are not empty and the top three slots are red
                    && top.getIsRed()
                    && second.getIsRed()
                    && third.getIsRed())
                {
                    return col;  //Return this column
                }
            }  
            else //I'm the yellow player
            {
                if(lowestEmpty > 0 && top != null && second != null && third != null //If the column is not empty, the top, second, and third values are not empty and the top three slots are yellow
                    && top.getIsYellow()
                    && second.getIsYellow()
                    && third.getIsYellow())
                    {
                    return col; //Return this column
                    }
            }
        }    
        
        //Right look - Checks to see if there is a possible horizontal win to the right
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col)); //The lowest empty spot in the column
            if(col + 3 < myGame.getColumnCount() && lowestEmpty != -1)  //If there are three columns to the right and this column is not full
            {
                Connect4Slot oneRight = myGame.getColumn(col + 1).getSlot(lowestEmpty); //The first column to the right
                Connect4Slot twoRight = myGame.getColumn(col + 2).getSlot(lowestEmpty); //The second column to the right
                Connect4Slot threeRight = myGame.getColumn(col + 3).getSlot(lowestEmpty); //The third column to the right
                if (iAmRed)  //If i'm the red player
                {
                    if(oneRight.getIsRed() //If the three columns to the right are red and not empty
                        &&twoRight.getIsRed()
                        &&threeRight.getIsRed()
                        &&oneRight != null
                        &&twoRight != null
                        &&threeRight != null)
                    {
                        return col; //Return this column
                    }
                }
                else //I'm the yellow player
                {
                    if(oneRight.getIsYellow() //If the three columns to the right are yellow and not empty
                        &&twoRight.getIsYellow()
                        &&threeRight.getIsYellow()
                        &&oneRight != null
                        &&twoRight != null
                        &&threeRight != null)
                    {
                        return col; //Return this column
                    }
                }
            }
        }
        
        //Left look - Checks to see if there is a possible horizontal win to the left
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col)); //The lowest empty spot in the column
            if(col - 3 > 0 && lowestEmpty != -1) //If there are three columns to the left and this column is not full
            {
                Connect4Slot oneLeft = myGame.getColumn(col - 1).getSlot(lowestEmpty); //The first column to the left
                Connect4Slot twoLeft = myGame.getColumn(col - 2).getSlot(lowestEmpty); //The second column to the left
                Connect4Slot threeLeft = myGame.getColumn(col - 3).getSlot(lowestEmpty); //The third column to the left
                if (iAmRed) //If i'm the red player
                {
                    if(oneLeft.getIsRed()  //If the three columns to the left are red and they are not empty
                        &&twoLeft.getIsRed()
                        &&threeLeft.getIsRed()
                        &&oneLeft != null
                        &&twoLeft != null
                        &&threeLeft != null)
                    {
                        return col; //Return this column
                    }
                }
                else
                {
                    if(oneLeft.getIsYellow() //If the three columns to the left are yellow and they are not empty
                        &&twoLeft.getIsYellow()
                        &&threeLeft.getIsYellow()
                        &&oneLeft != null
                        &&twoLeft != null
                        &&threeLeft != null)
                    {
                        return col; //Return this column
                    }
                }
           }
        }
       return -1; //There are no possible wins at this time
    }
        
    /**
     * Returns the column that would allow the opponent to win.  Looks for vertical wins first
     * then for wins to the right and finall wins to the left.
     *
     * @return the column that would allow the opponent to win.  Returns -1 if no such move exists.
     */
    public int theyCanWin()
    {
        //Vertical look - Checks to see if there is a possible vertical win
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col)); //The lowest empty spot in the column
            Connect4Slot top = myGame.getColumn(col).getSlot(lowestEmpty + 1); //The top column that is filled
            Connect4Slot second = myGame.getColumn(col).getSlot(lowestEmpty + 2); //the second from the top column that is filled
            Connect4Slot third = myGame.getColumn(col).getSlot(lowestEmpty + 3); //the third from the top column that is filled
            if (iAmRed) //If i'm the yellow player
            {
                if(lowestEmpty > 0 && top != null && second != null && third != null //If the column is not empty, the top, second, and third values are not empty and the top three slots are yellow
                    && top.getIsYellow()
                    && second.getIsYellow()
                    && third.getIsYellow())
                {
                    return col; //Return this column
                }
            }
            else //I'm the red player
            {
                if(lowestEmpty > 0 && top != null && second != null && third != null //If the column is not empty, the top, second, and third values are not empty and the top three slots are red
                    && top.getIsRed()
                    && second.getIsRed()
                    && third.getIsRed())
                {
                    return col; //Return this column
                }
            }
        }    
        //Right look - Checks to see if there is a possible horizontal win to the right
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col)); //The lowest empty spot in the column
            if(col + 3 < myGame.getColumnCount() && lowestEmpty != -1) //If there are three columns to the right and this column is not full
            {
                Connect4Slot oneRight = myGame.getColumn(col + 1).getSlot(lowestEmpty); //The first column to the right
                Connect4Slot twoRight = myGame.getColumn(col + 2).getSlot(lowestEmpty); //The second column to the right
                Connect4Slot threeRight = myGame.getColumn(col + 3).getSlot(lowestEmpty); //The third column to the right
                if (iAmRed)  //If i'm the yellow player
                {                    
                    if(oneRight.getIsYellow() //If the three columns to the right are yellow and not empty
                        &&twoRight.getIsYellow()
                        &&threeRight.getIsYellow()
                        &&oneRight != null
                        &&twoRight != null
                        &&threeRight != null)
                    {
                        return col; //Return this column
                    }
                }
                else //I'm the red player
                {
                    if(oneRight.getIsRed() //If the three columns to the right are red and not empty
                        &&twoRight.getIsRed()
                        &&threeRight.getIsRed()
                        &&oneRight != null
                        &&twoRight != null
                        &&threeRight != null)
                    {
                        return col; //Return this column
                    }
                }
            }
        }
        //Left look - Checks to see if there is a possible horizontal win to the left
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col));  //The lowest empty spot in the column
                if(col - 3 > 0 && lowestEmpty != -1)  //If there are three columns to the left and this column is not full
                {
                    Connect4Slot oneLeft = myGame.getColumn(col - 1).getSlot(lowestEmpty); //The first column to the left
                    Connect4Slot twoLeft = myGame.getColumn(col - 2).getSlot(lowestEmpty); //The second column to the left
                    Connect4Slot threeLeft = myGame.getColumn(col - 3).getSlot(lowestEmpty); //The third column to the left
                   if (iAmRed) //If i'm the yellow player
                   {
                    if(oneLeft.getIsYellow() //If the three columns to the left are yellow and not empty
                        &&twoLeft.getIsYellow()
                        &&threeLeft.getIsYellow()
                        &&oneLeft != null
                        &&twoLeft != null
                        &&threeLeft != null)
                    {
                        return col; //Return this column
                    }
                   }
                   else //I'm the yellow player
                   {
                    if(oneLeft.getIsRed()  //If the three columns to the left are red and not empty
                        &&twoLeft.getIsRed()
                        &&threeLeft.getIsRed()
                        &&oneLeft != null
                        &&twoLeft != null
                        &&threeLeft != null)
                        {
                            return col; //Return this column
                        }
                    }
                }

        }
        return -1; //There are no possible wins at this time
       }
    
    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "Cheli's Extra-Awesome Super Advanced Agent";
    }
}
