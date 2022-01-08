import sys
import cv2
import numpy as np
from tkinter import *
import speech_recognition as st
import pyttsx3 as ts
from deep_translator import GoogleTranslator

#picture = input("Vnesite pot do željene slike:\n")
#print("Pot do željene slike:")
#print(picture)

#nastavitve izgovorjevalca
engine = ts.init()
voices = engine.getProperty('voices')
engine.setProperty('voice', "HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Speech\Voices\Tokens\MSTTS_V110_slSI_Lado")
rate = engine.getProperty('rate')
engine.setProperty('rate', rate-50)

def posnamiBesedilo(izgovori):
    r = st.Recognizer()
    #zaženi mikrofon in shrani besedilo
    with st.Microphone() as mic:
        print(izgovori)
        engine.say(izgovori)
        engine.runAndWait()
        zvok = r.listen(mic)
    #uporaba Google prepoznave besedilo
    besedilo = ''
    try:
        besedilo = r.recognize_google(zvok, language="sl")
        print('Povedali ste: ' + besedilo)
        engine.say('Povedali ste. ' + besedilo)
        engine.runAndWait()
    except st.UnknownValueError : 
        print("Se opravičujem, nisem razumel!")
        engine.say("Se opravičujem, nisem razumel!")
        engine.runAndWait()
    except st.RequestError :
        print('Preverite internetno povezavo')
    
    return besedilo

msg = posnamiBesedilo('Pozdravljeni! Povejte besedilo, ki ga želite skriti!')
print(msg)

translated = GoogleTranslator(source='auto', target='sl').translate(msg)
print("Prevedeno: " + translated)

engine.say("Vaše končno prevedeno besedilo je " + translated)
engine.runAndWait()

#for voice in voices:
#   engine.setProperty('voice', voice.id)
#   rate = engine.getProperty('rate')
#   engine.setProperty('rate', rate-50)

   #print("Voice: %s" % voice.name)
   #print(" - ID: %s" % voice.id)
   #print(" - Languages: %s" % voice.languages)
   #print(" - Gender: %s" % voice.gender)
   #print(" - Age: %s" % voice.age)
   #print("\n")

#   engine.say(translated)

#engine.runAndWait()

#out = r"C:\Users\centrih\Desktop\out.jpg"
#lsb.hide(picture, message = translated).save(out)
#najdeno = lsb.reveal(out)
#print(najdeno)

def to_bin(data):
    if isinstance(data, str):
        return ''.join([ format(ord(i), "08b") for i in data ])
    elif isinstance(data, bytes) or isinstance(data, np.ndarray):
        return [ format(i, "08b") for i in data ]
    elif isinstance(data, int) or isinstance(data, np.uint8):
        return format(data, "08b")
    else:
        raise TypeError("Uporabljate nepodprt tip datoteke")


def encode(image_name, secret_data):
    image = cv2.imread(image_name)
    #izračun maksimalnega števila bitov za skrivanje
    n_bytes = image.shape[0] * image.shape[1] * 3 // 8
    #print("[*] Maximalno število bitov za kodiranje:", n_bytes)
    if len(secret_data) > n_bytes:
        raise ValueError("[!] Premalo prostora, uporabite večjo sliko ali manjše podatke")
    print("[*] Skrivanje podatkov...")
    # add stopping criteria
    secret_data += "====="
    data_index = 0
    #pretvorba podatkov v binarno obliko
    binary_secret_data = to_bin(secret_data)
    #pridobivanje velikosti podatkov
    data_len = len(binary_secret_data)
    for row in image:
        for pixel in row:
            #pretvorba RGB v binarno
            r, g, b = to_bin(pixel)
            #spremeni LSB če so še podaki
            if data_index < data_len:
                #sprememba R bita
                pixel[0] = int(r[:-1] + binary_secret_data[data_index], 2)
                data_index += 1
            if data_index < data_len:
                #sprememba G bita
                pixel[1] = int(g[:-1] + binary_secret_data[data_index], 2)
                data_index += 1
            if data_index < data_len:
                #sprememba B bita
                pixel[2] = int(b[:-1] + binary_secret_data[data_index], 2)
                data_index += 1
            #izhod iz loopa ob koncu podatkov
            if data_index >= data_len:
                break
    print("[*] Podatki skriti!")
    return image


def decode(image_name):
    print("[+] Dekodiranje podatkov...")
    image = cv2.imread(image_name)
    binary_data = ""
    for row in image:
        for pixel in row:
            r, g, b = to_bin(pixel)
            binary_data += r[-1]
            binary_data += g[-1]
            binary_data += b[-1]
    #razdeli celo sliko na 8 bitov
    all_bytes = [ binary_data[i: i+8] for i in range(0, len(binary_data), 8) ]
    #pretvorba bitov v znake
    decoded_data = ""
    for byte in all_bytes:
        decoded_data += chr(int(byte, 2))
        if decoded_data[-5:] == "=====":
            break
    return decoded_data[:-5]


odg = posnamiBesedilo("Želite vaše sporočilo zakodirati in shraniti v sliko?")
if odg == 'da' or odg == 'ja':
    input_image = "C:\\xampp\\htdocs\\CPSystems\\RV\\cistaSlika.png"
    output_image = "C:\\xampp\\htdocs\\CPSystems\\RV\\zakodiraniPodatki.png"
    #podatki katere zelimo skriti

    with open('C:\\xampp\\htdocs\\CPSystems\\RV\\podatki.txt') as f:
        text = f.readline()


    secret_data = text + "  ##  " + translated
    print(text + "  ##  " + translated)

    engine.say("Podatki, ki se bodo zapisali so: " + text + " " + translated)
    engine.runAndWait()

    #

    encoded_image = encode(image_name=input_image, secret_data=secret_data)
    cv2.imwrite(output_image, encoded_image)
    #decoded_data = decode(output_image)
    #print("[+] Dekodirani podatki:", decoded_data)
    engine.say("Operacija se je izvedla! Podatki so zakodirani!")
    engine.runAndWait()
else:
    print("Operacija se ni izvedla! Nasvidenje!")
    engine.say("Operacija se ni izvedla! Nasvidenje!")
    engine.runAndWait()



