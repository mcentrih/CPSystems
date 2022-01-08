import sys
import cv2
import numpy as np
import os
import logging
import threading
import time
import math
import matplotlib.pyplot as plt
import matplotlib.image as mplimg
from tkinter import *
from scipy import ndimage
from numba import jit

from enum import Enum
from PIL import Image

picture = input("Vnesite pot do željene slike:\n")
print("Pot do željene slike:")
print(picture)

#C:\Users\centrih\Desktop\osebna.jpg

class Status(Enum):
    hidden = 1
    zeros = 2

def HideText(picture, text):
  #  pic1 = Image.open(picture)
  #  pic.show()

    #pic = Image.open(picture)
   # Image.open(picture).save("osebna.bmp")
    pic = Image.open(picture)
    
    status = Status.hidden
    h, w = pic.size
    index = 0
    indexVrednost = 0
    pixelIndex = 0
    nicle = 0
    R = 0
    G = 0
    B = 0

   # color = pic.getpixel((100,100))
   # print(color)
    for i in range(0,h):
        for j in range(0,w):
            [pixColorR, pixColorG, pixColorB] = pic.getpixel((j,i))
         #   print(R)
         #   print(G)
         #   print(B)
            R = pixColorR - (pixColorR % 2)
            G = pixColorG - (pixColorG % 2)
            B = pixColorB - (pixColorB % 2)

            for n in range(0, 3):
                if pixelIndex % 8 == 0:
                    if status == Status.zeros and nicle == 8:
                        if (pixelIndex - 1) % 3 < 2:
                            pic.putpixel((j,i), (R,G,B))
                            return pic
                    if index >= len(text):
                        status = Status.zeros
                    else:
                        indexVrednost = ord(text[index])
                        index = index + 1
                if pixelIndex % 3 == 0 and status == Status.hidden:
                    R = R + indexVrednost % 2
                    indexVrednost = indexVrednost / 2
                elif pixelIndex % 3 == 1 and status == Status.hidden:
                    G = G + indexVrednost % 2
                    indexVrednost = indexVrednost / 2
                elif pixelIndex % 3 == 2:
                    if status == Status.hidden:
                        B = B + indexVrednost % 2
                        indexVrednost = indexVrednost / 2
                 #   print(R)
                 #   print(G)
                 #   print(B)
                    pic.putpixel((j,i), (int(R),int(G),int(B)))
                pixelIndex = pixelIndex + 1

                if status == Status.zeros:
                    nicle = nicle +1
    return pic

def ChangeBits(number):
    res = 0
    for y in range(0, 8):
        res = (res * 2) + (number % 2)
        number = number / 2
    return res

def FindText(picture):
    pic = Image.open(picture)
    h, w = pic.size
    indexValue = 0
    colorIndex = 0
    foundText = ""

    for i in range(0, h):
        for j in range(0, w):
            [pixColorR, pixColorG, pixColorB] = pic.getpixel((j,i))
            for n in range(0, 3):
                if colorIndex % 3 == 0:
                    indexValue = indexValue * 2 + pixColorR % 2
                elif colorIndex % 3 == 1:
                    indexValue = indexValue * 2 + pixColorG % 2
                elif colorIndex % 3 == 2:
                    indexValue = indexValue * 2 + pixColorB % 2
                
                colorIndex = colorIndex + 1

                if colorIndex & 8 == 0:
                    indexValue = ChangeBits(indexValue)
                    if indexValue == 0:
                        return foundText
                    charFromText = indexValue
                    foundText = foundText + charFromText


#slika = HideText(picture, "Moje ime je matjaz")
#slika.show()
#Image.open(slika).save("hidden.jpg")
slika = HideText(picture, "serbus")
slika.save("hidden.jpg")

tekst = FindText("hidden.jpg")
print(tekst)
print("Konec!")