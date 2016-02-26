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
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
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
        else if(this.stackUp() != -1)
        {
            moveOnColumn(stackUp());
        }
        else if(this.stackRight() != -1)
        {
            moveOnColumn(stackRight());
        }

        else if(this.stackLeft() != -1)
        {
            moveOnColumn(stackLeft());
        }
        else
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
     * TODO: documentation
     */
    
    public int stackUp()
    {
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col));
            //TODO: Make color dynamic
            if(lowestEmpty != 5)
            {
                Connect4Slot top = myGame.getColumn(col).getSlot(lowestEmpty + 1);
                if(top.getIsRed() && lowestEmpty > 0 && top != null)
                {
                    return col;
                }
            }
        }    
        return -1;        
    }
    /**
     * TODO: DOcumentation
     */
    public int stackLeft()
    {
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col));
            //TODO: Make color dynamic
            
            if(col + 1 < myGame.getColumnCount() && lowestEmpty != -1)
            {
                Connect4Slot right = myGame.getColumn(col + 1).getSlot(lowestEmpty);
                if(right.getIsRed() && right != null)
                {
                    return col;
                }
            }
        }
        return -1;
    }
    /**
     * TODO: DOcumentation
     */
    public int stackRight()
    {
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col));
            //TODO: Make color dynamic
            
            if(col - 1 > 0 && lowestEmpty != -1)
            {
                Connect4Slot left = myGame.getColumn(col - 1).getSlot(lowestEmpty);
                if(left.getIsRed() && left != null)
                {
                    return col;
                }
            }
        }
        return -1;
    }
    /**
     * Returns the column that would allow the agent to win.
     * 
     * You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implement this method to return what column would
     * allow the agent to win.
     *
     * @return the column that would allow the agent to win.  Returns -1 if no such move exists.
     */
    public int iCanWin()
    {
        //Vertical look - Checks to see if there is a possible vertical win
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col));
            //TODO: Make color dynamic
            Connect4Slot top = myGame.getColumn(col).getSlot(lowestEmpty + 1);
            Connect4Slot second = myGame.getColumn(col).getSlot(lowestEmpty + 2);
            Connect4Slot third = myGame.getColumn(col).getSlot(lowestEmpty + 3);
            if (iAmRed)
            {
                if(lowestEmpty > 0 && top != null && second != null && third != null 
                    && top.getIsRed()
                    && second.getIsRed()
                    && third.getIsRed()) //Ignore any empty, full or columns that have less than 3 elements
                {
                    return col;
                }
            }
            else
            {
                if(lowestEmpty > 0 && top != null && second != null && third != null 
                    && top.getIsYellow()
                    && second.getIsYellow()
                    && third.getIsYellow()) //Ignore any empty, full or columns that have less than 3 elements
                    {
                    return col;
                    }
            }
        }    
        
        //Right look - Checks to see if there is a possible horizontal win to the right
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col));
            //TODO: Make color dynamic
            if(col + 3 < myGame.getColumnCount() && lowestEmpty != -1)
            {
                Connect4Slot oneRight = myGame.getColumn(col + 1).getSlot(lowestEmpty);
                Connect4Slot twoRight = myGame.getColumn(col + 2).getSlot(lowestEmpty);
                Connect4Slot threeRight = myGame.getColumn(col + 3).getSlot(lowestEmpty);
                if (iAmRed)
                {
                    if(oneRight.getIsRed() 
                        &&twoRight.getIsRed()
                        &&threeRight.getIsRed()
                        &&oneRight != null
                        &&twoRight != null
                        &&threeRight != null)
                    {
                        return col;
                    }
                }
                else
                {
                    if(oneRight.getIsYellow() 
                        &&twoRight.getIsYellow()
                        &&threeRight.getIsYellow()
                        &&oneRight != null
                        &&twoRight != null
                        &&threeRight != null)
                    {
                        return col;
                    }
                }
            }
        }
        
        //Left look - Checks to see if there is a possible horizontal win to the left
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col));
            //TODO: Make color dynamic
            if(col - 3 > 0 && lowestEmpty != -1)
            {
                Connect4Slot oneLeft = myGame.getColumn(col - 1).getSlot(lowestEmpty);
                Connect4Slot twoLeft = myGame.getColumn(col - 2).getSlot(lowestEmpty);
                Connect4Slot threeLeft = myGame.getColumn(col - 3).getSlot(lowestEmpty);
                if (iAmRed)
                {
                    if(oneLeft.getIsRed() 
                        &&twoLeft.getIsRed()
                        &&threeLeft.getIsRed()
                        &&oneLeft != null
                        &&twoLeft != null
                        &&threeLeft != null)
                    {
                        return col;
                    }
                }
                else
                {
                    if(oneLeft.getIsYellow() 
                        &&twoLeft.getIsYellow()
                        &&threeLeft.getIsYellow()
                        &&oneLeft != null
                        &&twoLeft != null
                        &&threeLeft != null)
                    {
                        return col;
                    }
                }
           }
        }
       return -1;
    }
        
    /**
     * Returns the column that would allow the opponent to win.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *
     * @return the column that would allow the opponent to win.  Returns -1 if no such move exists.
     */
    public int theyCanWin()
    {
        //Vertical look
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col));
            //TODO: Make color dynamic
            Connect4Slot top = myGame.getColumn(col).getSlot(lowestEmpty + 1);
            Connect4Slot second = myGame.getColumn(col).getSlot(lowestEmpty + 2);
            Connect4Slot third = myGame.getColumn(col).getSlot(lowestEmpty + 3);
            if (iAmRed)
            {
                if(lowestEmpty > 0 && top != null && second != null && third != null 
                    && top.getIsYellow()
                    && second.getIsYellow()
                    && third.getIsYellow()) //Ignore any empty, full or columns that have less than 3 elements
                {
                    return col;
                }
            }
            else
            {
                if(lowestEmpty > 0 && top != null && second != null && third != null 
                    && top.getIsRed()
                    && second.getIsRed()
                    && third.getIsRed()) //Ignore any empty, full or columns that have less than 3 elements
                {
                    return col;
                }
            }
        }    
        
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col));
            //TODO: Make color dynamic
            if(col + 3 < myGame.getColumnCount() && lowestEmpty != -1)
            {
                Connect4Slot oneRight = myGame.getColumn(col + 1).getSlot(lowestEmpty);
                Connect4Slot twoRight = myGame.getColumn(col + 2).getSlot(lowestEmpty);
                Connect4Slot threeRight = myGame.getColumn(col + 3).getSlot(lowestEmpty);
                if (iAmRed)
                {                    
                    if(oneRight.getIsYellow() 
                        &&twoRight.getIsYellow()
                        &&threeRight.getIsYellow()
                        &&oneRight != null
                        &&twoRight != null
                        &&threeRight != null)
                    {
                        return col;
                    }
                }
                else
                {
                    if(oneRight.getIsRed() 
                        &&twoRight.getIsRed()
                        &&threeRight.getIsRed()
                        &&oneRight != null
                        &&twoRight != null
                        &&threeRight != null)
                    {
                        return col;
                    }
                }
            }
        }
        
        for(int col = 0; col < myGame.getColumnCount(); col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col));
            //TODO: Make color dynamic

                if(col - 3 > 0 && lowestEmpty != -1)
                {
                    Connect4Slot oneLeft = myGame.getColumn(col - 1).getSlot(lowestEmpty);
                    Connect4Slot twoLeft = myGame.getColumn(col - 2).getSlot(lowestEmpty);
                    Connect4Slot threeLeft = myGame.getColumn(col - 3).getSlot(lowestEmpty);
                   if (iAmRed)
                   {
                    if(oneLeft.getIsYellow() 
                        &&twoLeft.getIsYellow()
                        &&threeLeft.getIsYellow()
                        &&oneLeft != null
                        &&twoLeft != null
                        &&threeLeft != null)
                    {
                        return col;
                    }
                   }
                   else
                   {
                    if(oneLeft.getIsRed() 
                        &&twoLeft.getIsRed()
                        &&threeLeft.getIsRed()
                        &&oneLeft != null
                        &&twoLeft != null
                        &&threeLeft != null)
                        {
                            return col;
                        }
                    }
                }

        }
        return -1;
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
