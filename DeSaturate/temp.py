from __future__ import division

import Image
import colorsys


img = Image.open("TESTING_IMAGE.jpg")
width, height = img.size

newImg = Image.new("RGB", (width, height))


for y in xrange(height):
	for x in xrange(width):
		coords = (x, y)

		print("Getting values for %d, %d..." % coords)
		
		# get the RGB values from the pixel
		RGBTuple = img.getpixel(coords)
		print RGBTuple

		r, g, b = RGBTuple

		print r, g, b

		newColor = int((r + g + b) / 3)
		newRGBTuple = tuple((newColor, newColor, newColor))
		print("\n=========================  AVERAGING  =========================\n")

		print newRGBTuple

		print("Writing to image at (%d, %d)" % coords)

		newImg.putpixel(coords, newRGBTuple)

newImg.save("TESTING_IMAGE_OUT3.jpg", "JPEG")
