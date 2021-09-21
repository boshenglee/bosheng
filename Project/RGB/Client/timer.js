initTime = 30;

time = initTime;	//random value, might have a specific value later on

function startCountdown()
{
	document.getElementById("time").innerHTML="Time left: " + time;
	countdown = setInterval(function()
	{
		if(time < 0)
		{
			player.isAlive = false;
			document.getElementById("time").innerHTML="Time left: 0";
			window.location = "gameover.html";
			alert("Time's up! Game over!");

			return;
		}

		document.getElementById("time").innerHTML="Time left: " + time--;

	},1000);

}
