//creates player with 3 health points, 0 score, and level 1
player = {
	hp: 3,
	score: 0,
	level: 1,
	alive: true
};

//initial condition and value of the game
function initGame(){
	displayAll();
}

//to display the info to the player
function displayAll(){
	document.getElementById("hp").innerHTML = "Health: " + player.hp;
	document.getElementById("score").innerHTML = "Score: " + player.score;
	document.getElementById("level").innerHTML = "Level: " + player.level;
}

//increases player hp
player.hp++;

//decreases player hp by 1
player.hp = player.hp - 2;

//increase player score by 1
player.score++;

//increase player score by 9
player.score = player.score + 9;


module.exports = player;
