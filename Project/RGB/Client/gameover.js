//variables to store the player score and player level
var finalScore = localStorage.getItem("finalScore");
var finalLevel = localStorage.getItem("finalLevel");


//prints out game over message

//eg: Hey Ron,
document.getElementById("name").innerHTML = "Hey " + name + ",";

//eg: Your final score was 69 points!
document.getElementById("score").innerHTML = "Your final score was " + finalScore + " points!";

//eg: You reached level 420!
document.getElementById("level").innerHTML = "You reached level " + finalLevel+ "!";
