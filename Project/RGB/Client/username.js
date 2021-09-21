//creates player with 3 health points, 0 score, and level 1
window.player = {
  hp: 3,
  score: 0,
  level: 1,
  alive: true
};

document.addEventListener('click', musicPlay);
function musicPlay() {
    document.getElementById('audio1').volume = 0.8;
    document.getElementById('audio1').play();
    document.removeEventListener('click', musicPlay);
}

//to validate info entered by the user
function validate(){

  if(document.forms["userForm"]["userInput"].value==null||document.forms["userForm"]["userInput"].value==""){

    var image = document.getElementById("image" + "userInput");
    if (image == null) {
        image = new Image(15, 15);
        image.id = "image" + "userInput";
    }
    image.src ='./wrong.png';
    document.getElementById("username").appendChild(image);
  }

  if(document.forms["userForm"]["userInput"].value!=null&&document.forms["userForm"]["userInput"].value!=""){
    name = document.forms["userForm"]["userInput"].value;

    window.location.href = "index.html"
		
  }
}
