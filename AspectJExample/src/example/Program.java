package example;

import java.io.*;
import java.util.*;

public class Program 
{
	public static void main(String[] args)
	{
		System.out.println("请输入棋盘的大小：");
		Scanner stdin = new Scanner(System.in);
		int size = stdin.nextInt();
		System.out.println("分析中。。。");
		Queens queens = new Queens();
		queens.generate(size);
		int count = queens.getResultCount();
		System.out.printf("共有%d种结果。\n", count);
		while(true)
		{
			System.out.println("要查看哪一种（0为退出）：");
			int choice = stdin.nextInt();
			if(choice == 0)
				break;
			int[][] res = queens.getResult(choice - 1);
			for(int i = 0; i < size; i++)
			{
				for(int j = 0; j < size; j++)
				{
					System.out.print(res[i][j] == 1? 'Q': '.');
					System.out.print(' ');
				}
				System.out.println();
			}
		}
	}
	
}
