x = 0;
y =300;

snake = [
  {x:0,y:300}
  ];
direction = "east";

function start(){

  if(toggle.value=="Start"){

    context = document.getElementById("plate").getContext("2d");
    context.fillStyle = "#ff0000";

    toggle.value = "Stop";

    timer = setInterval(function(){
      increment();
      check(timer,toggle);
      context.rect(x,y,5,5);
      context.fill();
      snake.push({x:x,y:y});
    },25);
  }
  else{
    toggle.value = "Start";
    clearInterval(timer);
  }
}

function left(){

  clearInterval(timer);
  direction = updateLeft(direction);
  toggle.value = "Stop";
  timer =setInterval(function(){

    increment();
    check(timer,toggle);
    context.rect(x,y,5,5);
    snake.push({x:x,y:y});
    context.fill();
  },25);
}

function right(){

  clearInterval(timer);
  direction = updateRight(direction);
  toggle.value = "Stop";
  timer =setInterval(function(){
    increment();
    check(timer,toggle);
    context.rect(x,y,5,5);
    snake.push({x:x,y:y});
    context.fill();
  },25);
}

function updateLeft(direction){
  if (direction=="east") {return "north";}
  if (direction=="north") {return"west";}
  if (direction=="west") {return "south";}
  if (direction=="south") {return "east";}
}

function updateRight(direction){
  if (direction=="east") {return "south";}
  if (direction=="south") {return"west";}
  if (direction=="west") {return "north";}
  if (direction=="north") {return "east";}
}

function increment(){
  if (direction=="east") {x++;}
  if (direction=="north") {y--;}
  if (direction=="west") {x--;}
  if (direction=="south") {y++;}
}

function check(timer,toggle){

  for(let i =0; i<snake.length; i++)
  {
    const collied = snake[i].x === x && snake[i].y ===y
    if(collied){
      clearInterval(timer);
      toggle.value="Start";
    }
  }
  if(x >=1000|| x<0){
    clearInterval(timer);
    toggle.value="Start";
  }
  if(y >=600|| y<0) {
    clearInterval(timer);
    toggle.value="Start";
  }
}
