// /**
// *Monster object
// */
//     var monster = new Object();
//     monster.health = randomInt(power);//add to this number to increase health
//     monster.color;//we should probably set up an object for this
//     monster.points = power;
//     monster.damage = randomInt(power);
//     monster.correctColorID = randomInt(9);//I think that this includes the number 9
//     monster.colors = new Array(9);
//     monster.colors = setColors(colors);

/**
*Monster object
*/
class monster{
  constructor(power){
    var health = randomInt(power);//add to this number to increase health
    var color;//we should probably set up an object for this
    var points = power;
    var damage = randomInt(power);
    var correctColorID = randomInt(9);//I think that this includes the number 9
    var colors = new Array(9);
    colors = setColors(colors);
    var monsterColor = numsToRGBHex(colors[correctColorID-1]);
  }
}

/**
* colors the monster screen in index.html
*/
function colorScreen(color){
  var doc = document.getElementById("plate");
  var context = dc.getContext("2d");
  context.fillStyle = numsToRGBHex(color);
  context.fillRect(50,50,50,50);
}

/**
*converts color object to hex code
*/
function numsToRGBHex(color){
  let rgbValue = "#" + componentToHex(color.red) + componentToHex(color.green) + componentToHex(color.blue);
  return rbgValue;
}

/**
* Sets the color IDs for this monster
*/
function setColors(colorArr){ //removed "[]"
  let i = 0;
  let len = colorArr.length;
  for(i = 0; i < len; i++){
    colorArr[i] = {red: randomInt(255), green: randomInt(255), blue: randomInt(255)};
    //TODO we will need to add a function that makes sure that no two colors are the same.
    //Maybe use a while loop? (RON)
  }
}
/**
* Calculates the color based on the randomInt function
*/
function determineColor(){
  var red = randomInt(255);
  var green = randomInt(255);
  var blue = randomInt(255);
}

/**
* Gives a random int
* the idea for this function is directly based on getRandomInt from
* https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
*/
function randomInt(maxNum){
  return Math.floor(Math.random() * Math.floor(maxNum));
}

module.exports = monster;
