#include <iostream>
#include <fstream>
using namespace std;

class listNode {
	friend class linkedList;		
	int data;
	listNode * next;
		
	public:	
		listNode();
		listNode(int data);
		~listNode();
};

listNode::listNode() {
	this->data = -9999;
	this->next = NULL;	
}

listNode::listNode(int data) {
	this->data = data;
	this->next = NULL;
}

listNode::~listNode() {
	delete next;
}

class linkedList {
	listNode * listHead;

	public:
		linkedList();
		bool isEmpty();
		listNode * findSpot(int data);
		void listInsert(int data);
		void listDelete(int data);
		void printList(ofstream &outFile);
};

linkedList::linkedList() {
	this->listHead = new listNode();
}

bool linkedList::isEmpty() {
	if(this->listHead->next == NULL)
		return true;
	else
		return false;
}

listNode * linkedList::findSpot(int data) {
	listNode * spot = this->listHead;

	while(true) 
	{
		if(spot->next != NULL) 
		{	
			if(spot->next->data < data) 
                        	spot = spot->next;
			else if(spot->next->data == data)
				return NULL;
			else if(spot->next->data > data)
				return spot;
		} else 
			return spot;
        }
}

void linkedList::listInsert(int data) {
	listNode * newNode = new listNode(data);
	listNode * spot = this->findSpot(data);
	listNode * temp = spot->next;
	spot->next = newNode;
	newNode->next = temp;	
}

void linkedList::listDelete(int data) {
	listNode * node = this->listHead;
	
	while(node->next != NULL) 
	{
		if(node->next->data == data) 
		{
			node->next = node->next->next;
			break;
		} else		
			node = node->next;		
	}	
}

void linkedList::printList(ofstream &outFile) {
	listNode * spot = this->listHead;

	outFile << "listHead";
	
	while(spot->next != NULL)      
        {
                outFile << "-->(" << spot->data << ", " << spot->next->data << ")";
                spot = spot->next;
        }

	outFile << "-->(" << spot->data << ", -1)";

	outFile << endl;
}

int main(int argc, char * argv[]) {
	int data;
	listNode * spot;
	linkedList * list = new linkedList();
	ifstream inFile;
	inFile.open(argv[1]);
	ofstream outFile;
	outFile.open(argv[2]);
	
	if(inFile.is_open() && outFile.is_open())
        {
	   	while(inFile >> data) 
		{
			outFile << "Insert data " << data << ": ";
			spot = list->findSpot(data);
			if(spot == NULL) 
				outFile << data << " is already in the list\n";
			else
			{
				list->listInsert(data);
				list->printList(outFile);
			}
		}
	        inFile.close();
		outFile.close();
	}
	
	return 0;
}
