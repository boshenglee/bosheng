//initial condition and value of the game
function initGame(){
	displayAll();
}

//to check if player is alive
function checkIsAlive(){
	if(player.hp <= 0)
	{
		//if player hp is 0, change isAlive to false
		player.isAlive = false;

		//prints statement to indicate health is 0
		document.getElementById("hp").innerHTML="Health: 0";
		time = 10000;

		//store the player's final score
		localStorage.setItem("finalScore", player.score);
		//store the player's final level
		localStorage.setItem("finalLevel", player.level);
		window.location.href = "gameover.html";

		//prints message to let player know he/she is out of health
		alert("Out of health! Game over!");
				
		return;
	}
}

//to display the info to the player
function displayAll(){

	var img = document.createElement("img");

	if(player.hp==1){
		img.src = "one_heart.png";
	}
	if(player.hp==2){
		img.src = "two_heart.png";
	}
	if(player.hp==3){
		img.src = "three_heart.png";
	}

	var src = document.getElementById("healthBar");

		if (src.getElementsByTagName('img').length > 0) {
        src.replaceChild(img, src.getElementsByTagName('img')[0]);
    } else {
        // append if no previous image
        src.appendChild(img);
    }

	document.getElementById("score").innerHTML = "Score  &nbsp: " + player.score;
	document.getElementById("level").innerHTML = "Level  &nbsp: " + player.level;
}
