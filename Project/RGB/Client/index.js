if(name == null || name ==""){
  window.location.href = "username.html";
}

document.getElementById("nameDisplay").innerHTML = "Player : " + name;

initGame();
startCountdown();
temp =0;
rebuild();
initMonster();

document.addEventListener("click", musicPlay2);


function musicPlay2() {
    document.getElementById("audio2").volume = 0.8;
    document.getElementById("audio2").play();
    document.removeEventListener('click', musicPlay2);
  }
