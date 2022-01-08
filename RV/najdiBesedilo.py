import sys
import cv2
import numpy as np
from tkinter import *
import speech_recognition as st
import pyttsx3 as ts
from deep_translator import GoogleTranslator

engine = ts.init()
voices = engine.getProperty('voices')
engine.setProperty('voice', "HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Speech\Voices\Tokens\MSTTS_V110_slSI_Lado")
rate = engine.getProperty('rate')
engine.setProperty('rate', rate-50)

def to_bin(data):
    if isinstance(data, str):
        return ''.join([ format(ord(i), "08b") for i in data ])
    elif isinstance(data, bytes) or isinstance(data, np.ndarray):
        return [ format(i, "08b") for i in data ]
    elif isinstance(data, int) or isinstance(data, np.uint8):
        return format(data, "08b")
    else:
        raise TypeError("Uporabljate nepodprt tip datoteke")

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

odg = posnamiBesedilo("Želite vaše sporočilo odkodirati in jih zapisati v datoteko?")
if odg == 'da' or odg == 'ja':
    output_image = "C:\\xampp\\htdocs\\CPSystems\\RV\\zakodiraniPodatki.png"
    decoded_data = decode(output_image)
    print("[+] Dekodirani podatki:", decoded_data)
    with open('C:\\xampp\\htdocs\\CPSystems\\RV\\odkodiraniPodatki.txt', 'w') as f:
        f.write(decoded_data)
    engine.say("Operacija se je izvedla! Dekodirani podatki so: " +  decoded_data)
    engine.runAndWait()
else:
    print("Operacija se ni izvedla! Nasvidenje!")
    engine.say("Operacija se ni izvedla! Nasvidenje!")
    engine.runAndWait()
