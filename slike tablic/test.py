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
from tkinter import filedialog
from scipy import ndimage
from numba import jit
import imutils
import easyocr
import pytesseract

filetypes = [("Slikovne datoteke", ".jpg .png .jpeg .jpe .tif .gif .jfif")]
filepath = filedialog.askopenfilename(title="Odpri sliko", filetypes=filetypes)
img = cv2.imread(filepath)
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
cv2.imshow("siva", gray)
cv2.waitKey(0)
brezSumov = cv2.bilateralFilter(gray, 11, 17, 17) #Noise reduction
cv2.imshow("brez sumov", brezSumov)
cv2.waitKey(0)

edged = cv2.Canny(brezSumov, 30, 200) #Edge detection
cv2.imshow("robovi", edged)
cv2.waitKey(0)

keypoints = cv2.findContours(edged.copy(), cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
izocrtje = imutils.grab_contours(keypoints)
izocrtje = sorted(izocrtje, key=cv2.contourArea, reverse=True)[:10]

location = None
for izocrta in izocrtje:
    approx = cv2.approxPolyDP(izocrta, 10, True)
    if len(approx) == 4:
        location = approx
        break


mask = np.zeros(gray.shape, np.uint8)
new_image = cv2.drawContours(mask, [location], 0,255, -1)
new_image = cv2.bitwise_and(img, img, mask=mask)

cv2.imshow("nova", new_image)
cv2.waitKey(0)

(x,y) = np.where(mask==255)
(x1, y1) = (np.min(x), np.min(y))
(x2, y2) = (np.max(x), np.max(y))
cropped_image = gray[x1:x2+1, y1:y2+1]

cv2.imshow("obrezano", cropped_image)
cv2.waitKey(0)
#reader = easyocr.Reader(['en', 'sl'])
#result = reader.readtext(cropped_image)

pytesseract.pytesseract.tesseract_cmd = r'C:\Users\Matjaz CENTRIH\AppData\Local\Programs\Tesseract-OCR\tesseract.exe'
text = pytesseract.image_to_string(cropped_image)
a = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','-']
filterd = list(filter(lambda x : x in a,text))
print(filterd)
str1 = ''.join(filterd)
font = cv2.FONT_HERSHEY_SIMPLEX
res = cv2.putText(img, text=str1, org=(approx[0][0][0], approx[1][0][1]+70), fontFace=font, fontScale=1, color=(0,255,0), thickness=2, lineType=cv2.LINE_AA)
res = cv2.rectangle(img, tuple(approx[0][0]), tuple(approx[2][0]), (0,255,0),3)
cv2.imshow("res", res)
cv2.waitKey(0)
print(text)