#include <iostream>
#include <fstream>
using namespace std;

class listNode {
	friend class linkedListStack;
	
		int data;
		listNode * next;
	
	public:	
		listNode();
		listNode(int data);
		~listNode();
};

listNode::listNode() {
	this->data = 0;
}

listNode::listNode(int data) {
	this->data = data;
}

listNode::~listNode() {
	delete next;
}

class linkedListStack {
		listNode * top;
		
	public:
		linkedListStack();
        	~linkedListStack();
        	void push(int data);
		int pop();
        	bool isEmpty();
};
 
linkedListStack::linkedListStack() {
	this->top = 0;
}

linkedListStack::~linkedListStack() {
	delete top;
}

void linkedListStack::push(int data) {
	listNode * newNode = new listNode(data);
	newNode->next = this->top;
	this->top = newNode;
}

int linkedListStack::pop() {
	int temp = top->data;
	this->top = top->next;
	return temp;	
}

bool linkedListStack::isEmpty() {
	if(this->top == 0)
		return true;
	else 
		return false;
}

int main(int argc, char * argv[]) {
	int data;
	linkedListStack * stack = new linkedListStack();
	ifstream inFile;
        inFile.open(argv[1]);
	
	if(inFile.is_open())
        {
        	while(inFile >> data) 
		{
			cout << data << endl;
			stack->push(data);
        	}
                inFile.close();
        }
	
	cout << endl;

	while(!stack->isEmpty())
		cout << stack->pop() << endl;

	return 0;
}
