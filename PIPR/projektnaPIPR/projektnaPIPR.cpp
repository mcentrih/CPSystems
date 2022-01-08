// projektnaPIPR.cpp : This file contains the 'main' function. Program execution begins and ends there.
//
#include <iostream>
#include <chrono>
#include <mpi.h>
#include <fstream>
#include <json/value.h>

using namespace std;

vector<int> bumps;
unsigned lineCounter = 0;

void sumFirstHalf(vector<int> vectorFirst, int first) {
	int firstHalfSum = 0;

	for (int i = 0; i < first; i++) {
		firstHalfSum = firstHalfSum + vectorFirst[i];
	}
	cout << "First half SUM: " << firstHalfSum << endl;
	MPI_Send(&firstHalfSum, 1, MPI_INT, 2, 0, MPI_COMM_WORLD);

}

void sumSecondHalf(vector<int> vectorFirst, int second) {
	int secondHalfSum = 0;

	for (int i = second; i < (second*2); i++) {
		secondHalfSum = secondHalfSum + vectorFirst[i];
	}
	cout << "Second half SUM: " << secondHalfSum << endl;
	MPI_Send(&secondHalfSum, 1, MPI_INT, 2, 0, MPI_COMM_WORLD);

}

int countLinesInFile(string file) {
	std::ifstream inFile(file.c_str());

	inFile.unsetf(std::ios_base::skipws);

	lineCounter = std::count(
		istream_iterator<char>(inFile),
		istream_iterator<char>(),
		'\n');

	//cout << "Lines: " << lineCounter<< "\n";
	return lineCounter;
}

int main() {
	MPI_Init(NULL, NULL);
	string file = "test.txt";
	int firstHalf = 0;
	int secondHalf = 0;
	int sumFirst = 0;
	int sumSecond = 0;
	int result = 0;

	/*STEVILO VRSTIC V DATOTEKI*/
	lineCounter = countLinesInFile(file.c_str());
	//cout << "VRSTICE: " << lineCounter << endl;

	/*BRANJE DATOTEKE*/
	ifstream inFile;
	inFile.open(file.c_str());
	int trenutniBump = 0;

	//BRANJE IN SHRANJEVANJE
	if (inFile.is_open()) {
		for (int i = 0; i < lineCounter + 1; i++) {
			inFile >> trenutniBump;
			bumps.push_back(trenutniBump);
		}
		inFile.close();
	}

	/*TESTNI IZPIS*/
	//for (int i = 0; i < lineCounter + 1; i++) {
	//	cout << bumps[i] << " ";
	//}
	//cout << endl;
	/*---------------*/

	firstHalf = lineCounter / 2;
	secondHalf = lineCounter - firstHalf;
	firstHalf++;
	//cout << "FIRST: " << firstHalf << endl;
	//cout << "SECOND: " << secondHalf << endl;
	
	int my_rank;
	MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);
	int world_size;
	MPI_Comm_size(MPI_COMM_WORLD, &world_size);
	//prva polovica podatkov
	if (my_rank == 0) {
		sumFirstHalf(bumps, firstHalf);
	}
	//druga polovica podatkov
	else if (my_rank == 1) {
		sumSecondHalf(bumps, secondHalf);
	}
	if (my_rank == 2) {
		MPI_Recv(&sumFirst, 1, MPI_INT, 0, 0, MPI_COMM_WORLD,
			MPI_STATUS_IGNORE);
		MPI_Recv(&sumSecond, 1, MPI_INT, 1, 0, MPI_COMM_WORLD,
			MPI_STATUS_IGNORE);
		result = sumFirst + sumSecond;
		cout << "Rezultat: " << result << endl;	//skupno stevilo bumpov
	}
	MPI_Finalize();

}
