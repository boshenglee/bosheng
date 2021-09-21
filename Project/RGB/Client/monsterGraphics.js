/**
* A lot of this code was based on the example given here: https://codepen.io/w3devcampus/pen/PpLLKY
*/

var canvas, context, monsterWidth, monsterHeight;
var xMon = 20;//make sure this isn't incorrect
var yMon =20;//Come back later
var speed = 0.4;//Come back later
var monColor = "#FFFFFF";//Change this to the correct color of the button

//window.onload =
function initMonster(){
  canvas = document.getElementById("plate");//If there is an issue here, try using canvas = document.querySelector("#plate"); Note: CSS may be needed if this happens
  monsterWidth = canvas.width;
  monsterHeight = canvas.height;

  //Draw here
  context = canvas.getContext('2d');

  monsterLoop();
}

function monsterLoop(){
  //clear canvas
  context.clearRect(0,0,monsterWidth, monsterHeight);

  //draw monster
  drawMonster(xMon, yMon);

  //Change monster position
  xMon+=speed;

  if(((xMon+100) > monsterWidth) || (xMon < 0)){
    speed = -speed;
  }

  requestAnimationFrame(monsterLoop);
}

function drawMonster(x,y){
  context.save();

  context.translate(x,y);
  context.fillStyle = color[globalColorValues.globalBaseColor][globalColorValues.globalDifficulty];

  //0,0 is top left corner of monster
  context.strokeRect(0, 0, 100, 100);
  context.strokeStyle = color[globalColorValues.globalBaseColor][globalColorValues.globalDifficulty];
  //eyes
  context.fillRect(20, 20, 10, 10);
  context.fillRect(65, 20, 10, 10);

  //nose
  context.strokeRect(45,40,10,40);

  //mouth
  context.strokeRect(35, 84, 30, 10);

  context.restore();
}
