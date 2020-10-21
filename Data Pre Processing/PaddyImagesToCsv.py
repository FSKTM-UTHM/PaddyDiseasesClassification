import numpy as np
import cv2
import os

IMG_DIR = 'C:/Users/MakAlienware/Desktop/TestData'

for img in os.listdir(IMG_DIR):
    img_array = cv2.imread(os.path.join(IMG_DIR,img), cv2.IMREAD_GRAYSCALE)
    img_array = (img_array.flatten())
    img_array  = img_array.reshape(-1, 1).T

    #img_array.insert(len(img_array), img)
    #newArray = np.append (img_array, [img])

    print(img_array)
    with open('testData.csv', 'ab') as f:
        np.savetxt(f, img_array, delimiter=",")
