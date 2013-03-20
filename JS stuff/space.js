var starList = [];
var numStars = 400;
var canvas, ctxt;
var cWidth, cHeight;
var theShip;

var SpaceShip = function()
{
	this.x = 80;
	this.y = cHeight / 2;
	this.img = new Image();
	this.img.src = "ship.png";
	this.ySpeed = 15;

	this.isDodging = false; // flag that's set to true if we're currently dodging something
	this.dodgeY = null; // if we're dodging, this is where we're going

	console.log("Ship initialized at height " + this.y);
};

SpaceShip.prototype = 
{
	move: function(keyCode)
	{
		if(keyCode == 38) // up
		{
			this.y -= this.ySpeed;
			console.log("Moving ship up: " + this.y);
		} else // down
		{
			this.y += this.ySpeed;
			console.log("Moving ship down: " + this.y);
		}
	},

	// the dodging functionality is the next step to implement.
	// basically the checkPath() function will assess whether there
	// is an obstacle in the way of the ship, and if there is it will
	// set the isDodging flag to true. then it will determine the dodging
	// direction and a destination position for the ship. the dodge()
	// function will run as long as the isDodging() flag is set, and 
	// will animate the ship towards the destination point and when it 
	// is reached, it will reset the flag to false.
	dodge: function()
	{

	},

	checkPath: function()
	{

	}
};

var Star = function()
{
	this.y = Math.floor(Math.random() * cHeight); // random height between 0 and cHeight
	this.speed = Math.floor(Math.random() * 13) + 1; // random speed
	this.x = Math.floor(Math.random() * cWidth); // random starting x
	this.radius = Math.ceil(this.speed * .60);
};

Star.prototype = 
{
	move: function()
	{
		this.x -= this.speed; // just move the star

		if(this.x <= 0)
		{
			this.reset(); // if we run off the edge, just initialize the star again
		}
	},

	// resets the star
	// I think this is kind of elegant because it essentially makes space generation
	// infinite while only using X number of stars because we just reset every one of them
	// that leaves the screen
	reset: function()
	{
		this.x = cWidth;
		this.y = Math.floor(Math.random() * cHeight); // random height between 0 and cHeight
		this.speed = Math.floor(Math.random() * 13) + 1; // random speed
		this.radius = Math.ceil(this.speed * .60);
	}
};

var init = function()
{
	// get the canvas and its context
	canvas = document.getElementById("space");
	ctxt = canvas.getContext("2d");

	// this could be cleaner, but we extract some information for dimensions
	ctxt.canvas.width = window.innerWidth - 20;
	ctxt.canvas.height = window.innerHeight - 20;
	cWidth = ctxt.canvas.width;
	cHeight = ctxt.canvas.height;

	// initialize it black
	ctxt.fillStyle = "#000000";
	ctxt.fillRect(0, 0, ctxt.canvas.width, ctxt.canvas.height);

	console.log("canvas initialized");
	console.log(cWidth, cHeight);

	// create our spaceship
	theShip = new SpaceShip();

	// add listeners for the up and down keys to move the ship
	document.addEventListener('keydown', function(event) 
	{
	    if(event.keyCode == 38 || event.keyCode == 40) 
	    {
	        // console.log("Pressed up");
	        theShip.move(event.keyCode);	        
	    }
	    
	});

	// create all of our stars
	for(var i = 0; i < numStars; i++)
	{
		var myStar = new Star();
		starList.push(myStar);
	}

	// animate all this shit yo
	setInterval(drawBackground, 15);
	setInterval(drawForeground, 15)
};

// function for animating space and all that shit
var drawBackground = function()
{
	// clear the canvas for animation
	ctxt.fillStyle = "#000000";
	ctxt.fillRect(0, 0, cWidth, cHeight);

	// loop through the array of our stars
	for(var i = 0; i < numStars; i++)
	{
		starList[i].move(); // move the star

		// display the star at its new position
		ctxt.beginPath();
		{
			ctxt.fillStyle = "#FFFFFF";
			ctxt.arc(starList[i].x, starList[i].y, starList[i].radius, 0, Math.PI * 2);
		}
		ctxt.fill();
	}
};

// draws the foreground, which includes teh spaceship and shit for it to dodge
var drawForeground = function()
{
	ctxt.drawImage(theShip.img, theShip.x, theShip.y, theShip.img.width * .75, theShip.img.height * .75);
};