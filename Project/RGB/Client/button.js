var color = [
  ["#d10000", "#ff4747", "#ff3838", "#ff2929", "#ff1414", "#ff0505", "#f00000", "#e00000"],//red
  ["#006600", "#00c200", "#00b800", "#00ad00", "#00a800", "#009e00", "#008f00", "#007500"],//green
  ["#0000db", "#4747ff", "#3838ff", "#2e2eff", "#1f1fff", "#0f0fff", "#0000f5", "#0000e6"],//blue
  ["#e6e600", "#ffff7a", "#ffff6b", "#ffff5c", "#ffff42", "#ffff0f", "#ffff05", "#f5f500"],//yellow
  ["#540094", "#8b00f5", "#8200e6", "#7c00db", "#7400cc", "#6b00bd", "#6500b3", "#5c00a3"] //purple
];

difficulty =1;

var globalColorValues = {//Max added these two vars so that the monster can be colored in as the same color as the special button
  globalDifficulty : "",
  globalBaseColor : ""
};


function rebuild(){

  do{
    var baseColor = Math.floor((Math.random() * 5));
  }while(baseColor == temp);
  temp = baseColor;



  var i;
  for (i = 1; i <= 25 ; i++) {
    var but = document.getElementById(String(i));
    but.value = 1;
    but.style.backgroundColor=color[baseColor][0];//normal color
  }
  var x = Math.floor((Math.random() * 25)+1); //random number from 1-9
  diff = document.getElementById(String(x));
  diff.value = 2;
  if(player.level < 35){
    if(player.level%5==0){
      difficulty++;
    }
  }

  diff.style.backgroundColor=color[baseColor][difficulty];// the special color
  globalColorValues.globalBaseColor = baseColor;//Max added this
  globalColorValues.globalDifficulty = difficulty;//Max added this
}

$("input").click(function() {
    var fired_button = $(this).val();
    if(fired_button==1){   //if user select the wrong button

      // var allButton = document.querySelectorAll("[id='1'],[id='2'],[id='3'],[id='4'],[id='5'],[id='6'],[id='7'],[id='8'],[id='9']");

      for (var i = 1; i <=25; i++){
        var shakeButton = document.querySelector("[id='"+i+"']");
        shakeButton.classList.add("apply-shake");
      }

			//decrease hp by 1
			player.hp--;
			checkIsAlive();
			displayAll();

      var incorrectAudio = new Audio('incorrect.wav');
      incorrectAudio.play();

      shakeButton.addEventListener("animationend", (e) => {
      for (var i = 1; i <=25; i++){
          var shakeButton = document.querySelector("[id='"+i+"']");
          shakeButton.classList.remove("apply-shake");
      }
    });

    }
    if(fired_button==2){  //if user select the correct button

			//increase player score by (1/time)*1000 and level by 1
			player.score = player.score + (Math.round((1 / time) * 1000));
			player.score = parseInt(player.score);
			player.level++;

			//every 10 levels +1 hp, but capped at 3
			if(player.level % 10 == 0)
			{
				if(player.hp < 3)
				{
					player.hp++;
				}
			}

			//countdown -20% secs every 5 levels, but minimum is 5 secs
			if(player.level % 5 == 0)
			{
				if(initTime * 0.8 >= 5)
				{
					initTime *= 0.8;
					Math.round(initTime);
					initTime = parseInt(initTime);
				}

				else {
					initTime = 5;
				}
			}

			//set initTime
			time = initTime;
			displayAll();

      var correctAudio = new Audio('correct.wav');
      correctAudio.play();
      rebuild();
      initMonster();//Max added this to make a new monster
    }
});
