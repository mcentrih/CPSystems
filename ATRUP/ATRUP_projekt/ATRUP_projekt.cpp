// ATRUP_projekt.cpp : This file contains the 'main' function. Program execution begins and ends there.
//

#include <iostream>
#include <fstream>
#include <sstream>
#include <cmath>
#include <chrono>
#include <vector>
#include <algorithm>

using namespace std;

struct lokvanj {
	int id = -1;
	int vrednost = -1;
	int trenutnaVsota = -1;
	int pot = -1;
	int oce = -1;
	string desc = "";
};

vector<int> rezultatiMajn;
vector<int> rezultatiVec;
vector<int> rezultatiIsto;

vector<lokvanj> lokvanji;
int id = 0;

vector<int> muhe;

int trenutniIndex = -1;
int zadnjiSkok;
int stLokvanjev;
int velikostSkoka;
int vsotaMuh = 0;
int vsotaMuhM = 0;
int vsotaMuhI = 0;
int vsotaMuhV = 0;

void readFromFile(string file) {
	ifstream inFile;
	inFile.open(file.c_str());

	inFile >> stLokvanjev;
	inFile >> velikostSkoka;

	int muheSt = 0;

	if (inFile.is_open()) {
		for (int i = 0; i < stLokvanjev; i++) {
			inFile >> muheSt;
			muhe.push_back(muheSt);
		}
		inFile.close();

	}

	for (int i = 0; i < stLokvanjev; i++) {
		cout << muhe[i] << " "; //izpis
	}

	cout << endl;
	cout << stLokvanjev << endl;
	cout << velikostSkoka;
}

void PrviSkok() {
	vsotaMuh = vsotaMuh + muhe.at(velikostSkoka - 1);
	trenutniIndex = velikostSkoka - 1;
	zadnjiSkok = velikostSkoka;
}

void ZgradiGlavnegaOceta() {
	lokvanj lokvanjOce;
	lokvanjOce.id = id;
	id++;
	lokvanjOce.pot = 0;
	lokvanjOce.vrednost = muhe.at(trenutniIndex);
	lokvanjOce.oce = -1;
	lokvanjOce.desc = "Glavni oce";

	lokvanji.push_back(lokvanjOce);
}

void ZgradiDrevo(int trenutni, int zadnji, int trenutnaVM, int trenutnaVI, int trenutnaVV, int oce) {
	lokvanj lokMajn, lokIsto, lokVec;
	int majn = -1, isto = -1, vec = -1;
	int novSkok = trenutni + zadnji;
	int vsotaKonec = 0;
	int size = muhe.size();
	size = size - 1;
	if ((trenutni < size) && (trenutni > -1)) {

		if (novSkok - 1 < muhe.size()) {
			//majn = muhe[novSkok - 1];
			lokMajn.id = id;
			id++;
			lokMajn.pot = (zadnji - 1);
			lokMajn.vrednost = muhe.at(novSkok - 1);
			lokMajn.trenutnaVsota = trenutnaVM + lokMajn.vrednost;
			rezultatiMajn.push_back(lokMajn.trenutnaVsota);
			lokMajn.desc = "MANJ";
			lokMajn.oce = oce;
			//	lokvanji.push_back(lokMajn);
			trenutniIndex = novSkok - 1;
			if (trenutniIndex < size)
				ZgradiDrevo(novSkok - 1, zadnji - 1, lokMajn.trenutnaVsota, lokIsto.trenutnaVsota, lokVec.trenutnaVsota, lokMajn.id);

		}
		if (novSkok < muhe.size()) {
			//majn = muhe[novSkok];
			lokIsto.id = id;
			id++;
			lokIsto.pot = zadnji;
			lokIsto.vrednost = muhe.at(novSkok);
			lokIsto.trenutnaVsota = trenutnaVI + lokIsto.vrednost;
			rezultatiIsto.push_back(lokIsto.trenutnaVsota);
			lokIsto.desc = "ISTO";
			lokIsto.oce = oce;
			//	lokvanji.push_back(lokIsto);
			trenutniIndex = novSkok;
			if (trenutniIndex < size)
				ZgradiDrevo(novSkok, zadnji, lokMajn.trenutnaVsota, lokIsto.trenutnaVsota, lokVec.trenutnaVsota, lokIsto.id);
		}
		if (novSkok + 1 < muhe.size()) {
			//majn = muhe[novSkok + 1];
			lokVec.id = id;
			id++;
			lokVec.pot = (zadnji + 1);
			lokVec.vrednost = muhe.at(novSkok + 1);
			lokVec.trenutnaVsota = trenutnaVV + lokVec.vrednost;
			rezultatiVec.push_back(lokVec.trenutnaVsota);
			lokVec.desc = "VEC";
			lokVec.oce = oce;
			//	lokvanji.push_back(lokVec);
			trenutniIndex = novSkok + 1;
			if (trenutniIndex < size)
				ZgradiDrevo(novSkok + 1, zadnji + 1, lokMajn.trenutnaVsota, lokIsto.trenutnaVsota, lokVec.trenutnaVsota, lokVec.id);
		}
	}
}

void ZgradiDrevo1(int trenutni, int zadnji, int vsotaVmesM, int vsotaVmesI, int vsotaVmesV) {
	id++;
	int novSkok = trenutni + zadnji;
	int size = muhe.size();
	size = size - 1;
	if ((trenutni < size) && (trenutni > -1)) {

		if (novSkok - 1 < muhe.size()) {

			vsotaVmesM = vsotaMuhM + muhe.at(novSkok - 1);
			if (vsotaVmesM > vsotaMuh)
				vsotaMuhM = vsotaVmesM;

			trenutniIndex = novSkok - 1;
			if (trenutniIndex < size)
				ZgradiDrevo1(novSkok - 1, zadnji - 1, vsotaVmesM, vsotaVmesI, vsotaVmesV);

		}
		if (novSkok < muhe.size()) {
			vsotaVmesI = vsotaMuhI + muhe.at(novSkok);
			if (vsotaVmesI > vsotaMuh)
				vsotaMuhI = vsotaVmesI;

			trenutniIndex = novSkok;
			if (trenutniIndex < size)
				ZgradiDrevo1(novSkok, zadnji, vsotaVmesM, vsotaVmesI, vsotaVmesV);
		}
		if (novSkok + 1 < muhe.size()) {
			vsotaVmesV = vsotaMuhV + muhe.at(novSkok + 1);
			if (vsotaVmesV > vsotaMuh)
				vsotaMuhV = vsotaVmesV;
			trenutniIndex = novSkok + 1;
			if (trenutniIndex < size)
				ZgradiDrevo1(novSkok + 1, zadnji + 1, vsotaVmesM, vsotaVmesI, vsotaVmesV);
		}
	}
}

void OptimalnaResitev() {
	int novSkok = 0;
	int j = 0, i = 0, z = 0;
	bool m = false, is = false, v = false;
	if (muhe.size() == 1)
		vsotaMuh = muhe.at(0);
	else {
		for (int x = 0; ((trenutniIndex + zadnjiSkok) - 1) < muhe.size(); x++) {
			novSkok = trenutniIndex + zadnjiSkok;
			m = false, is = false, v = false;
			j = (novSkok - 1), i = novSkok, z = (novSkok + 1);

			try {
				if (vsotaMuh < (vsotaMuh + muhe.at(j))) {

					m = true;
					is = false;
					v = false;
				}
			}
			catch (std::out_of_range e) {
				cout << endl << x << ": " << "Prevelik index M!" << endl;
			}
			try {
				if ((vsotaMuh + muhe.at(j)) < (vsotaMuh + muhe.at(i))) {

					m = false;
					is = true;
					v = false;
				}
			}
			catch (std::out_of_range e) {
				cout << endl << x << ": " << "Prevelik index I!" << endl;
			}
			try {
				if ((vsotaMuh + muhe.at(i)) < (vsotaMuh + muhe.at(z))) {

					m = false;
					is = false;
					v = true;
				}

			}
			catch (std::out_of_range e) {
				cout << endl << x << ": " << "Prevelik index V!" << endl;
			}



			if (m == true && is == false && v == false) {
				vsotaMuh = vsotaMuh + muhe.at(j);
				trenutniIndex = (novSkok - 1);

				zadnjiSkok--;
			}
			else if (m == false && is == true && v == false) {
				vsotaMuh = vsotaMuh + muhe.at(i);
				trenutniIndex = novSkok;
			}
			else if (m == false && is == false && v == true) {
				vsotaMuh = vsotaMuh + muhe.at(z);
				trenutniIndex = (novSkok + 1);

				zadnjiSkok++;
			}
		}
	}
}

void NajdaljsaPot() {
	/*	for (int i = 0; i < lokvanji.size(); i++) {
			if (vsotaMuh < lokvanji[i].trenutnaVsota)
				vsotaMuh = lokvanji[i].trenutnaVsota;
		}*/
	int sizeM = rezultatiMajn.size(), sizeI = rezultatiIsto.size(), sizeV = rezultatiVec.size();
	int rezM = 0, rezI = 0, rezV = 0;
	for (int i = 0; i < sizeM; i++) {
		if (rezM < rezultatiMajn.at(i))
			rezM = rezultatiMajn.at(i);
	}
	for (int i = 0; i < sizeI; i++) {
		if (rezI < rezultatiIsto.at(i))
			rezI = rezultatiMajn.at(i);
	}
	for (int i = 0; i < sizeV; i++) {
		if (rezV < rezultatiVec.at(i))
			rezV = rezultatiVec.at(i);
	}
	vector<int> rez;
	rez.push_back(rezM);
	rez.push_back(rezI);
	rez.push_back(rezV);
	int biggest = 0;
	for (int i = 0; i < rez.size(); i++) {
		if (biggest < rez[i])
			biggest = rez[i];
	}
	cout << endl << "Biggest: " << biggest << endl << "Trenutna vsota: " << vsotaMuh << endl;
	vsotaMuh = biggest;
	cout << endl << "Vsota muh M: " << rezM << endl;
	cout << endl << "Vsota muh I: " << rezI << endl;
	cout << endl << "Vsota muh V: " << rezV << endl;
}

int main(int argc, char** argv) {
	if (argc == 2) {
		cout << argv[1] << endl;
		auto start = chrono::high_resolution_clock::now();
		readFromFile(argv[1]);
		PrviSkok();

		//Optimalna resitev
		OptimalnaResitev();

		//2.opcija BRUTE FORCE
		/*	ZgradiGlavnegaOceta();
			ZgradiDrevo1(trenutniIndex, zadnjiSkok, 0, 0, 0);
			cout << endl << "Vsota muh M: " << vsotaMuhM + vsotaMuh << endl;
			cout << endl << "Vsota muh I: " << vsotaMuhI + vsotaMuh << endl;
			cout << endl << "Vsota muh V: " << vsotaMuhV + vsotaMuh << endl;
			cout << endl << "Vsota muh M: " << vsotaMuhM << endl;
			cout << endl << "Vsota muh I: " << vsotaMuhI << endl;
			cout << endl << "Vsota muh V: " << vsotaMuhV << endl;
			cout << endl << "Vsota muh: " << vsotaMuh << endl;
			*/

			//1.opcija BRUTE FORCE
		/* ZgradiDrevo(trenutniIndex, zadnjiSkok, lokvanji.at(0).vrednost, lokvanji.at(0).vrednost, lokvanji.at(0).vrednost, 0);
			NajdaljsaPot();
			*/
		cout << endl << "Zaba je padla v vodo in si poplaknila grlo!!!" << endl;
		cout << argv[1] << endl;
		cout << "Vsota muh: " << vsotaMuh << endl;

		auto end = std::chrono::high_resolution_clock::now();
		int milisekunde = chrono::duration_cast<std::chrono::milliseconds>(end - start).count();
		int sekunde = milisekunde / 1000;
		std::cout << "Cas izvajanja v sekundah(s): " << sekunde << "," << milisekunde % 1000 << std::endl;
		return 1;

	}
}

// Run program: Ctrl + F5 or Debug > Start Without Debugging menu
// Debug program: F5 or Debug > Start Debugging menu

// Tips for Getting Started: 
//   1. Use the Solution Explorer window to add/manage files
//   2. Use the Team Explorer window to connect to source control
//   3. Use the Output window to see build output and other messages
//   4. Use the Error List window to view errors
//   5. Go to Project > Add New Item to create new code files, or Project > Add Existing Item to add existing code files to the project
//   6. In the future, to open this project again, go to File > Open > Project and select the .sln file
