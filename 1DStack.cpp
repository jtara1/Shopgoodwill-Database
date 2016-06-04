#include <iostream>
#include <fstream>
using namespace std;
 
class Stack {
	int * stackAry, top;

	public:
		Stack(int stackSize);
		~Stack();
		int pop();
		void push(int data);
		bool isEmpty();
};

Stack::Stack(int stackSize) {
	stackAry = new int[stackSize];
	top = 0;
}

Stack::~Stack() {
	delete[] stackAry;
}

int Stack::pop() {
	top--;
	return stackAry[top];
}

void Stack::push(int data) {
	stackAry[top] = data;
	top++;
}

bool Stack::isEmpty() {
	if(top == 0)
		return true;
	else
		return false;
}

int main(int argc, char * argv[]) {
	int data;
	int count = 0;
	Stack * stack;
	ifstream inFile;
	inFile.open(argv[1]);
	
	while(inFile >> data) 
	{
		cout << data << endl;
		count++;
	} 

	cout << endl;

	inFile.close();

	stack = new Stack(count);
	inFile.open(argv[1]);

	while(inFile >> data)
	{
		stack->push(data);
	}
	
	inFile.close();

	while(!stack->isEmpty())
		cout << stack->pop() << endl;

	return 0;
}
