using System;
using System.IO;
using System.IO.Ports;
using System.Net;
using System.Text;

namespace NRSVmesnik
{
    class Program
    {
        static void Zapisi(string json, string novo)
        {
            string fileName = @"C:\xampp\htdocs\CPSystems\NRSVmesnik\prebrano.json";

            try
            {
                if (novo.ToLower().Equals("y"))
                {
                    if (File.Exists(fileName))
                    {
                        File.Delete(fileName);
                    }

                    using (FileStream fs = File.Create(fileName))
                    {
                        Byte[] byteJson = new UTF8Encoding(true).GetBytes("[" + json + "]");
                        fs.Write(byteJson, 0, byteJson.Length);
                    }
                }
                else
                {
                    string text;
                    using (StreamReader streamReader = File.OpenText(fileName))
                    {
                        text = streamReader.ReadToEnd();
                        Console.WriteLine(text + "," + json + "]");
                    }
                    text = text.Remove(text.Length - 1);
                    using (FileStream fs = File.Create(fileName))
                    {
                        Byte[] byteJson = new UTF8Encoding(true).GetBytes(text + "," + json + "]");
                        fs.Write(byteJson, 0, byteJson.Length);
                    }
                }

            }
            catch (Exception Ex)
            {
                Console.WriteLine(Ex.ToString());
            }
        }

        static void Main(string[] args)
        {
            //cesta
            char[] buffer = new char[100];
            string name;
            string st;
            string novo;
            string pravi;
            try
            {
                st = "";
                SerialPort mySerialPort = new SerialPort("COM5", 9600);
                mySerialPort.Open();
                Console.WriteLine("Začni branje!");
                Console.ReadKey();
                Console.WriteLine("Čakam podatke...");
                Console.WriteLine("Potrdi poslane podatke!");
                Console.ReadKey();
                mySerialPort.Read(buffer, 0, 100);
                Console.WriteLine("Podatki prejeti!");

                Console.WriteLine("Prejeti podatki:");
                Console.WriteLine();

                for (int i = 0; i < 3; i++)
                {
                    Console.Write(buffer[i]);
                    st += buffer[i];
                }
                Console.WriteLine();
                Console.Write("Pravi podatki?(y/n) ");
                pravi = Console.ReadLine();
                if (pravi.ToLower().Equals("y"))
                {
                    Console.WriteLine("Vpišite ime poti: ");
                    name = Console.ReadLine();
                    string json = "{\"name\":\"" + name + "\"," +
                                  "\"bumps\":\"" + Convert.ToInt32(st) + "\"}";

                    Console.Write("Ustvarimo nov dokument ali dodamo podatke?(y/n) ");
                    novo = Console.ReadLine();
                    Zapisi(json, novo);
                }
                else
                    Console.WriteLine("Končujem!");
            }
            catch (IOException ex)
            {
                Console.WriteLine(ex.Message);
            }
            Console.ReadKey();
        }

    }
}
