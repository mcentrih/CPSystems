// projektnaPIPR.cpp : This file contains the 'main' function. Program execution begins and ends there.
//
#include <iostream>
#include <chrono>
#include <mpi.h>
#include <fstream>
#include <json/value.h>
#include <string>
#include <json/json.h>
#include <vector>

using namespace std;

vector<int> bumps;
unsigned lineCounter = 0;
vector<vector<int>> jsonVector;
int jsonSIZE = 0;
vector<int> vector1;
vector<int> vector2;

void sumFirstHalf(vector<vector<int>> vectorFirst, int first) {
	int vsota1 = 0;
	vector<int> povprecja1;
	int povprecje1 = 0;

	for (int i = 0; i < 3; i++) {
		for (int j = 0; j < vectorFirst[i].size(); j++) {
			vsota1 = vsota1 + vectorFirst[i][j];
		}
		povprecje1 = vsota1 / jsonVector[i].size();
		povprecja1.push_back(povprecje1);
		povprecje1 = 0;
		vsota1 = 0;
	}
	int a = povprecja1.size();
	MPI_Send(&a, 1, MPI_INT, 2, 1, MPI_COMM_WORLD);
	MPI_Send(povprecja1.data(), a, MPI_INT, 2, 2, MPI_COMM_WORLD);

}

void sumSecondHalf(vector<vector<int>> vectorFirst, int second) {
	int vsota2 = 0;
	vector<int> povprecja2;
	int povprecje2 = 0;

	for (int i = 0; i < 3; i++) {
		for (int j = 0; j < vectorFirst[i].size(); j++) {
			vsota2 = vsota2 + vectorFirst[i][j];
		}
		povprecje2 = vsota2 / jsonVector[i].size();
		povprecja2.push_back(povprecje2);
		povprecje2 = 0;
		vsota2 = 0;
	}
	int b = povprecja2.size();
	MPI_Send(&b, 1, MPI_INT, 2, 3, MPI_COMM_WORLD);
	MPI_Send(povprecja2.data(), b, MPI_INT, 2, 4, MPI_COMM_WORLD);
}

void preberiJSON(string file) {
	ifstream ifFile(file.c_str());
	Json::Value actualJson;
	Json::Reader reader;

	reader.parse(ifFile, actualJson);
	jsonSIZE = actualJson["podatki"].size();
	vector<int> rel1;
	vector<int> rel2;
	vector<int> rel3;
	vector<int> rel4;
	vector<int> rel5;
	vector<int> rel6;

	for (int i = 0; i < jsonSIZE; i++) {
		if (actualJson["podatki"][i]["idRelacija"] == "1") {
			rel1.push_back(actualJson["podatki"][i]["bumps"].asInt());
		}
		else if (actualJson["podatki"][i]["idRelacija"] == "2") {
			rel2.push_back(actualJson["podatki"][i]["bumps"].asInt());
		}
		else if (actualJson["podatki"][i]["idRelacija"] == "3") {
			rel3.push_back(actualJson["podatki"][i]["bumps"].asInt());
		}
		else if (actualJson["podatki"][i]["idRelacija"] == "4") {
			rel4.push_back(actualJson["podatki"][i]["bumps"].asInt());
		}
		else if (actualJson["podatki"][i]["idRelacija"] == "5") {
			rel5.push_back(actualJson["podatki"][i]["bumps"].asInt());
		}
		else if (actualJson["podatki"][i]["idRelacija"] == "6") {
			rel6.push_back(actualJson["podatki"][i]["bumps"].asInt());
		}
	}

	jsonVector.push_back(rel1);
	jsonVector.push_back(rel2);
	jsonVector.push_back(rel3);
	jsonVector.push_back(rel4);
	jsonVector.push_back(rel5);
	jsonVector.push_back(rel6);
}

int countLinesInFile(string file) {
	std::ifstream inFile(file.c_str());

	inFile.unsetf(std::ios_base::skipws);

	lineCounter = std::count(
		istream_iterator<char>(inFile),
		istream_iterator<char>(),
		'\n');

	return lineCounter;
}

int main(int argc, char *argv[]) {
	MPI_Init(NULL, NULL);

	string jsonFile = argv[1];
	int firstHalf = 0;
	int secondHalf = 0;
	vector<int> done;

	/*BRANJE .json*/
	preberiJSON(jsonFile);

	size_t const half_size = jsonVector.size() / 2;
	vector<vector<int>> split_low(jsonVector.begin(), jsonVector.begin() + half_size);
	vector<vector<int>> split_high(jsonVector.begin() + (half_size), jsonVector.end());

	int my_rank;
	MPI_Comm_rank(MPI_COMM_WORLD, &my_rank);
	int world_size;
	MPI_Comm_size(MPI_COMM_WORLD, &world_size);
	//prva polovica podatkov
	if (my_rank == 0) {
		sumFirstHalf(split_low, firstHalf);
	}
	//druga polovica podatkov
	else if (my_rank == 1) {
		sumSecondHalf(split_high, secondHalf);
	}
	if (my_rank == 2) {
		int c;
		int d;

		MPI_Recv(&c, 1, MPI_INT, 0, 1, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		vector<int> result1(c);
		MPI_Recv(result1.data(), c, MPI_INT, 0, 2, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		MPI_Recv(&d, 1, MPI_INT, 1, 3, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
		vector<int> result2(d);
		MPI_Recv(result2.data(), d, MPI_INT, 1, 4, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

		done.insert(done.end(), result1.begin(), result1.end());
		done.insert(done.end(), result2.begin(), result2.end());

		cout << endl << " <br> Seznam relacij: <br>" << endl;
		cout << "1 -> Celje - Store <br>" << endl;
		cout << "2 -> Celje - Sentjur <br>" << endl;
		cout << "3 -> Celje - Lasko <br>" << endl;
		cout << "4 -> Celje - Zalec <br>" << endl;
		cout << "5 -> Celje - Maribor <br>" << endl;
		cout << "6 -> Celje - Ljubljana <br>" << endl;

		for (int i = 0; i < done.size(); i++) {
			cout << "Povprecje destinacije " << i + 1 << "-> " << done[i] << "<br>" << endl;
		}
	}
	MPI_Finalize();
}
