import java.io.*;
import java.util.Scanner;

class listNode {
	int data;
	listNode next;

	listNode() 
	{
		this.data = 0;
	}

	listNode(int data) 
	{
		this.data = data;
	}	
}

public class linkedListStack {
	listNode top;

	linkedListStack()
	{
		this.top = null;
	}

	void push(int data) 
	{
		listNode newNode = new listNode(data);
		newNode.next = this.top;
		this.top = newNode;
	}

	int pop() 
	{
		int temp = top.data;
		this.top = top.next;
		return temp;	
	}

	boolean isEmpty() 
	{
		if(this.top == null)
			return true;
		else 
			return false;
	}

	public static void main(String[] args) 
	{	
		try 
		{
			Scanner inFile = new Scanner(new FileReader(args[0]));
			int data;
			linkedListStack stack = new linkedListStack();

   			while(inFile.hasNextInt()) 
			{
				data = inFile.nextInt();
				stack.push(data);
				System.out.println(data);
			}

			System.out.println();

			while(!stack.isEmpty()) 
				System.out.println(stack.pop());
		}
		catch(FileNotFoundException e) 
		{
			System.out.println(e);
		}
	}
}
