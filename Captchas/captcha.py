import Image
import ImageFont, ImageDraw, ImageOps
import random

def drawHorizLines(img):
	draw = ImageDraw.Draw(img)
	width, height = img.size

	for y in xrange(height):
		if(random.randint(0, 100) <= 8):
			draw.line([(0, y), (width, y)], fill = 25)

	return img


def drawVertLines(img):
	draw = ImageDraw.Draw(img)
	width, height = img.size

	for x in xrange(width):
		if(random.randint(0, 100) <= 8):
			draw.line([(x, 0), (x, height)], fill = 105)

	return img


# first we'll initialize our lists of font paths
fontFiles = ["andlso.ttf", "comic.ttf", "aparajbi.ttf", "aparaji.ttf",
			 "simfang.ttf"]

# and now our first words
firstWords = ["Near", "Past", "Like", "Into", "Since",
			  "Basic", "Dead", "Mass", "Cold", "Deadly"]

# and second words
secondWords = ["hysteria", "death", "me", "fire", "cold",
			   "money", "hookers", "stuff", "eggs", "pig"]

# initialize some misc shit
filename = "captcha_sample"
fontPath = r'C:\\Windows\\Fonts\\'
width = 330
height = 100

# randomize our font size
fontSize = random.randrange(18, 48)

# create the image for our text
textImg = Image.new("RGBA", (width, height), "white")
noiseImg = Image.new("RGBA", (width, height), "white")
finalImg = Image.new("RGBA", (width, height), "white")

# the text and font for our captcha
word1Text = random.choice(firstWords)
word2Text = random.choice(secondWords)

print word1Text + " " + word2Text

# here we'll choose a random font
font1Path = fontPath + random.choice(fontFiles)
font2Path = fontPath + random.choice(fontFiles)

word1Font = ImageFont.truetype(font1Path, fontSize)
word2Font = ImageFont.truetype(font2Path, fontSize)

# the first thing we're going to do is add some random pixels
# to make up the background noise layer of the captcha
iters = 0
maxIters = random.randrange(10000, 15000)

while(iters < maxIters):
	# set random coordinates
	newX = random.randrange(0, width)
	newY = random.randrange(0, height)

	# and set random RGB
	red = random.randrange(50, 255)
	green = random.randrange(50, 255)
	blue = random.randrange(50, 255)

	# wrap them both up in tuples
	coords = (newX, newY)
	color = (red, green, blue)

	noiseImg.putpixel(coords, color)
	finalImg.putpixel(coords, color)

	iters += 1

# add some line noise to the background
noiseImg = drawHorizLines(noiseImg)
finalImg = drawHorizLines(finalImg)

noiseImg = drawVertLines(noiseImg)
finalImg = drawVertLines(finalImg)

# randomize coordinates for the leftmost word
word1X = random.randrange(5, 50)
word1Y = random.randrange(5, 25)

# now coordinates for the right word
word2X = random.randrange(120,165)
word2Y = random.randrange(5, 30)

# 2 draw components so we can get separate images of all of the parts of the captcha
draw = ImageDraw.Draw(textImg)
draw2 = ImageDraw.Draw(finalImg)

# draw the first word
draw.text((word1X, word1Y), word1Text, font = word1Font, fill = 0)
draw2.text((word1X, word1Y), word1Text, font = word1Font, fill = 0)

# and draw the second word
draw.text((word2X, word2Y), word2Text, font = word2Font, fill = 0)
draw2.text((word2X, word2Y), word2Text, font = word2Font, fill = 0)

# textImg = textImg.rotate(angle)
# finalImg.paste(textImg, (word1X, word1Y), textImg)

# save all the files now that we're done with them
noiseImg.save(filename + "_NOISE.jpg")
textImg.save(filename + "_TEXT.jpg")
finalImg.save(filename + "_COMPLETE.jpg")