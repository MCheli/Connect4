import java.util.Random;

public class MyAgent extends Agent
{
    Random r;
    
    int columnCount;
    int rowCount;
    
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
        columnCount = myGame.getColumnCount();
        rowCount = myGame.getRowCount();
    }

    /**
     * The move method is run every time it is this agent's turn in the game.  
     * Will attempt to win the game if possible, then block if possible, 
     * and then will run through a series of best moves.  If all else fails
     * then it will do a random move.
     */
    public void move()
    {
        int iWin = this.canWin(true);
        int theyWin = this.canWin(false);
        int sUp = this.stackUp();
        int sSide = this.stackSide();

        if(iWin > -1) //If I can win
        {
            moveOnColumn(iWin); //Place winning piece
        }
        else if(theyWin > -1) //Else if they can win
        {
            moveOnColumn(theyWin); //Place blocking piece
        }
        else if(sUp > -1) //else if I can stack vertically
        {
            moveOnColumn(sUp);
        }
        else if(sSide > -1) //else if I can streak horizontally
        {
            moveOnColumn(sSide);
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
        for  (int i = 0; i < rowCount; i++)
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
        int i = r.nextInt(columnCount);
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(columnCount);
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
        for(int col = 0; col < columnCount; col++)  //Cycle through each column
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
    public int stackSide()
    {
        for(int col = 0; col < columnCount; col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col)); //The lowest empty spot in the column
            if(col + 1 < columnCount && lowestEmpty != -1)  //If there is a column to the right, and this column is not full
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
        return -1; //There is no possible stackLeft opportunities
    }
    
    /**
     * Returns the column that would allow the agent to win.  Looks for vertical wins first
     * then for wins to the right and finall wins to the left.
     *
     * @param myWin true if the win being checked is for this agent, false if the win being checked is for the opponent.
     * @return the column that would allow the agent to win.  Returns -1 if no such move exists.
     */
    public int canWin(boolean myWin)
    {
        //Vertical look - Checks to see if there is a possible vertical win
        for(int col = 0; col < columnCount; col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col));  //The lowest empty spot in the column
            Connect4Slot top = myGame.getColumn(col).getSlot(lowestEmpty + 1); //The top slot in the column
            Connect4Slot second = myGame.getColumn(col).getSlot(lowestEmpty + 2); //The second to last slot in the column
            Connect4Slot third = myGame.getColumn(col).getSlot(lowestEmpty + 3); //The last slot in the column
            if (iAmRed && myWin || !iAmRed && !myWin) //If i'm the red player
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
        
        //Horizontal look - Checks to see if there is a possible horizontal win to the right then the left
        for(int col = 0; col < columnCount; col++)  //Cycle through each column
        {
            int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col)); //The lowest empty spot in the column
            //Check Right
            if(col + 3 < columnCount && lowestEmpty != -1)  //If there are three columns to the right and this column is not full
            {
                Connect4Slot oneRight = myGame.getColumn(col + 1).getSlot(lowestEmpty); //The first column to the right
                Connect4Slot twoRight = myGame.getColumn(col + 2).getSlot(lowestEmpty); //The second column to the right
                Connect4Slot threeRight = myGame.getColumn(col + 3).getSlot(lowestEmpty); //The third column to the right
                if (iAmRed && myWin || !iAmRed & !myWin)  //If i'm the red player
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
            //Check Left
            if(col - 3 > 0 && lowestEmpty != -1) //If there are three columns to the left and this column is not full
            {
                Connect4Slot oneLeft = myGame.getColumn(col - 1).getSlot(lowestEmpty); //The first column to the left
                Connect4Slot twoLeft = myGame.getColumn(col - 2).getSlot(lowestEmpty); //The second column to the left
                Connect4Slot threeLeft = myGame.getColumn(col - 3).getSlot(lowestEmpty); //The third column to the left
                if (iAmRed && myWin || !iAmRed && !myWin) //If i'm the red player
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
                else //I'm the yellow player
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
        //Diagonal look - Checks to see if there is a possible diagonal win to the left and down, left and up, right and down, then right and up
        for(int col = 0; col < columnCount; col++)  //Cycle through each column
        {
           int lowestEmpty = getLowestEmptyIndex(myGame.getColumn(col)); //The lowest empty spot in the column
           //Diagonal to the left look
           if(col - 3 >= 0 && lowestEmpty != -1) //If there are three columns to the left and this column is not full look to the left
            {
                Connect4Slot oneDownLeft = myGame.getColumn(col - 1).getSlot(lowestEmpty + 1); //The first column to the left and down
                Connect4Slot twoDownLeft = myGame.getColumn(col - 2).getSlot(lowestEmpty + 2); //The second column to the left and down
                Connect4Slot threeDownLeft = myGame.getColumn(col - 3).getSlot(lowestEmpty + 3); //The third column to the left and down
                Connect4Slot oneUpLeft = myGame.getColumn(col - 1).getSlot(lowestEmpty - 1); //The first column to the left and down
                Connect4Slot twoUpLeft = myGame.getColumn(col - 2).getSlot(lowestEmpty - 2); //The second column to the left and down
                Connect4Slot threeUpLeft = myGame.getColumn(col - 3).getSlot(lowestEmpty - 3); //The third column to the left and down
                if (iAmRed && myWin || !iAmRed && !myWin) //If i'm the red player
                {
                    if((oneDownLeft != null //If the three columns to the left and down/up are red and they are not empty
                        &&twoDownLeft != null
                        &&threeDownLeft != null
                        &&oneDownLeft.getIsRed()  
                        &&twoDownLeft.getIsRed()
                        &&threeDownLeft.getIsRed())
                        ||
                        (oneUpLeft != null
                        &&twoUpLeft != null
                        &&threeUpLeft != null
                        &&oneUpLeft.getIsRed()  //If the three columns to the left an up are red and they are not empty
                        &&twoUpLeft.getIsRed()
                        &&threeUpLeft.getIsRed()))
                        
                    {
                        return col; //Return this column
                    }
                }
                else //I'm the yellow player
                {
                    if((oneDownLeft != null //If the three columns to the left and down/up are yellow and they are not empty
                        &&twoDownLeft != null
                        &&threeDownLeft != null
                        &&oneDownLeft.getIsYellow()  
                        &&twoDownLeft.getIsYellow()
                        &&threeDownLeft.getIsYellow())
                        ||
                        (oneUpLeft != null
                        &&twoUpLeft != null
                        &&threeUpLeft != null
                        &&oneUpLeft.getIsYellow()  //If the three columns to the left and down are yellow and they are not empty
                        &&twoUpLeft.getIsYellow()
                        &&threeUpLeft.getIsYellow()))
                    {
                        return col; //Return this column
                    }
                }
           }
           //Diagonal to the right look
           if(col + 3 < columnCount && lowestEmpty != -1) //If there are three columns to the right and this column is not full look to the right
           {
               Connect4Slot oneDownRight = myGame.getColumn(col + 1).getSlot(lowestEmpty + 1); //The first column to the left and down
               Connect4Slot twoDownRight = myGame.getColumn(col + 2).getSlot(lowestEmpty + 2); //The second column to the left and down
               Connect4Slot threeDownRight = myGame.getColumn(col + 3).getSlot(lowestEmpty + 3); //The third column to the left and down
               Connect4Slot oneUpRight = myGame.getColumn(col + 1).getSlot(lowestEmpty - 1); //The third column to the left and down
               Connect4Slot twoUpRight = myGame.getColumn(col + 2).getSlot(lowestEmpty - 2); //The third column to the left and down
               Connect4Slot threeUpRight = myGame.getColumn(col + 3).getSlot(lowestEmpty - 3); //The third column to the left and down
               if (iAmRed && myWin || !iAmRed && !myWin) //If i'm the red player
               {
                   if((oneDownRight != null
                       &&twoDownRight != null
                       &&threeDownRight != null
                       &&oneDownRight.getIsRed()  //If the three columns to the right and down/up are red and they are not empty
                       &&twoDownRight.getIsRed()
                       &&threeDownRight.getIsRed())
                       ||
                       (oneUpRight != null
                       &&twoUpRight != null
                       &&threeUpRight != null
                       &&oneUpRight.getIsRed()  //If the three columns to the right and down/up are red and they are not empty
                       &&twoUpRight.getIsRed()
                       &&threeUpRight.getIsRed()))
                       
                   {
                       return col; //Return this column
                   }
               }
               else //I'm the yellow player
               {
                   if((oneDownRight != null
                       &&twoDownRight != null
                       &&threeDownRight != null
                       &&oneDownRight.getIsYellow()  //If the three columns to the right and down/up are yellow and they are not empty
                       &&twoDownRight.getIsYellow()
                       &&threeDownRight.getIsYellow())
                       ||
                       (oneUpRight != null
                       &&twoUpRight != null
                       &&threeUpRight != null
                       &&oneUpRight.getIsRed()  //If the three columns to the right and down/up are yellow and they are not empty
                       &&twoUpRight.getIsRed()
                       &&threeUpRight.getIsYellow()))
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
