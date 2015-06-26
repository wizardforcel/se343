package example;

import java.util.*;

public class Queens 
{
	private ArrayList<int[][]> result
	  = new ArrayList<>();
	  
	private int size;
	
	private int[][] board;
	
	public int[][] getResult(int i)
	{
		return result.get(i);
	}
	
	public int getResultCount()
	{
		return result.size();
	}
	
	public int getSize()
	{
		return size;
	}
	
	public void generate(int size)
	{
		this.size = size;
		result.clear();
		
		board = new int[size][];
		for(int i = 0; i < size; i++)
			board[i] = new int[size];
		
		rGenerate(0);
	}
	
	private void rGenerate(int index)
	{
		if(index == size)
		{
			result.add(copyBoard());
			return;
		}
		
		for(int i = 0; i < size; i++)
		{
			if(canSet(index, i))
			{
				board[index][i] = 1;
				rGenerate(index + 1);
				board[index][i] = 0;
			}
		}
					
	}
	
	private int[][] copyBoard()
	{
		int[][] newBoard = new int[size][];
		for(int i = 0; i < size; i++)
		{
			newBoard[i] = new int[size];
			for(int j = 0; j < size; j++)
				newBoard[i][j] = board[i][j];
		}
		return newBoard;
	}
	
	private boolean canSet(int row, int col)
	{
		//up
		for(int i = row - 1; i >= 0; i--)
		{
			if(board[i][col] != 0)
				return false;
		}
		
		//upLeft
		for(int i = row - 1, j = col - 1;
			i >= 0 && j >= 0;
			i--, j--)
		{
			if(board[i][j] != 0)
				return false;
		}
		
		//upRight
		for(int i = row - 1, j = col + 1;
			i >= 0 && j < size;
			i--, j++)
		{
			if(board[i][j] != 0)
				return false;
		}
		
		return true;
	}

}
