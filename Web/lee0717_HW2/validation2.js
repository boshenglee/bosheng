function validate2(){

  valCheck = true;

  var resultValidateEmail = emailValidate(document.forms["contactInformation"]["email"].value);
  var image = getImage(Boolean(resultValidateEmail),"email")
  var labelNotifyEmail = getNotification(Boolean(resultValidateEmail), "email");
  document.getElementById("Email").appendChild(image);
  document.getElementById("Email").appendChild(labelNotifyEmail);

  var resultValidatePhone = phoneValidate(document.forms["contactInformation"]["phone"].value);
  var image1 = getImage(Boolean(resultValidatePhone),"phone")
  var labelNotifyPhone = getNotification(Boolean(resultValidatePhone), "phone");
  document.getElementById("Phone").appendChild(image1);
  document.getElementById("Phone").appendChild(labelNotifyPhone);

  var resultValidateAddress = addressValidate(document.forms["contactInformation"]["address"].value);
  var image2 = getImage(Boolean(resultValidateAddress),"address")
  var labelNotifyAddress = getNotification(Boolean(resultValidateAddress), "address");
  document.getElementById("Address").appendChild(image2);
  document.getElementById("Address").appendChild(labelNotifyAddress);

}

function addressValidate(address){
  var split = address.split(",");
  if(split.length==2 && split[1].length ==2){
    var letter = /^[a-zA-Z]+$/;
    var capitalLetter = /^[A-Z]+$/
    if(split[0].match(letter)&&split[1].match(capitalLetter)){
      valCheck = true;
      return true;
    }
    else{
      valCheck = false;
      return false;
    }
    return true;
  }
  valCheck = false;
  return false;
}

function phoneValidate(phone){
  var split = phone.split('-');
  if(split.length==1 && numCheck(phone) && split[0].length==10){
    return true;
  }
  else if (split.length==3 && numCheck(split[0]) && numCheck(split[1]) && numCheck(split[2]) && split[0].length+split[1].length+split[2].length == 10){
    return true;  
  }
  else{
    valCheck = false;
    return false;
  }
}


function emailValidate(email) {
    var split = email.split('@');
    if (split.length == 2 && alphaNumCheck(split[0])) {
        var periodSplit = split[1].split('.')
        if (periodSplit.length == 2 && alphaNumCheck(periodSplit[0] + periodSplit[1])) {
            return true;
        }
    }
    valCheck = false;
    return false;
}

function getNotification(bool, ID) {
    var label = document.getElementById("labelNotify" + ID);
    if (label == null) {
        label = document.createElement("LABEL");
        label.id = "labelNotify" + ID;
        // label.className = "errorMessage";
        label.setAttribute( 'class', 'errorMessage' );
      }
    if(bool == true){
      label.innerHTML ="";
    } else {
      if(ID == "email"){
        label.innerHTML = "Email should be in form xxx@xxx.xxx, which x should be alphanumeric!";
      }else if(ID == "phone"){
        label.innerHTML = "Phone should be in the form xxx-xxx-xxxx or xxxxxxxxxx, which x should be numeric";
      } else{
        label.innerHTML = "Address should be in the form of city,STATE.";
      }
     } 
    return label;
}

function alphaNumCheck(word) {
    let regex = /^[a-z0-9]+$/i;
    if (word != null && word.match(regex)) {
        return true;
    } else {
        return false;
    }
}

function numCheck(word){
  let regex = /^[0-9]+$/i;
  if(word != null && word.match(regex)){
    return true;
  } else{
      return false;  
  }
}

function getImage(bool, ID) {
    var image = document.getElementById("image" + ID);
    if (image == null) {
        image = new Image(15, 15);
        image.id = "image" + ID;
    }
    image.src = bool ? './correct.png' : './wrong.png';
    return image;
}