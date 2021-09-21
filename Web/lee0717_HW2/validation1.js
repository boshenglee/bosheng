function validate(){
  valCheck = true;

  var resultValidatefName = validateName(document.forms["validationForm"]["fName"].value);
  var image = getImage(Boolean(resultValidatefName),"email")
  var labelNotifyFName = getNotification(Boolean(resultValidatefName), "fName") ;
  document.getElementById("FName").appendChild(image);
  document.getElementById("FName").appendChild(labelNotifyFName);

  fNameCheck = valCheck;

  var resultValidatelName = validateName(document.forms["validationForm"]["lName"].value);
  var image1 = getImage(Boolean(resultValidatelName),"lName")
  var labelNotifyLName = getNotification(Boolean(resultValidatelName), "lName");
  document.getElementById("LName").appendChild(image1);
  document.getElementById("LName").appendChild(labelNotifyLName);

  lNameCheck = valCheck;

  var resultValidateGender = validateSelect(document.forms["validationForm"]["gender"].value);
  var image2 = getImage(Boolean(resultValidateGender),"gender")
  var labelNotifyGender = getNotification(Boolean(resultValidateGender), "gender");
  document.getElementById("Gender").appendChild(image2);
  document.getElementById("Gender").appendChild(labelNotifyGender);
  genderCheck = valCheck;


  var resultValidateState = validateSelect(document.forms["validationForm"]["state"].value);
  var image3 = getImage(Boolean(resultValidateState),"state")
  var labelNotifyState = getNotification(Boolean(resultValidateState), "state");
  document.getElementById("State").appendChild(image3);
  document.getElementById("State").appendChild(labelNotifyState);
  stateCheck = valCheck;

  if(fNameCheck&&lNameCheck&&genderCheck&&stateCheck){
    location.href = "validation2.html";
  }
  

}
function validateSelect(select){
  if (select == ""){
    valCheck = false;
    return false;
  }
  else{
    valCheck = true;
    return true;
  }

}


function validateName(name){
    var letterNumber = /^[0-9a-zA-Z ]+$/;
    if(name != null && name.match(letterNumber)){
      valCheck = true;
      return true;
    }
    else{
      valCheck = false;
      return false;
    }
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
      if(ID == "fName"){
        label.innerHTML = "First name must contain only alphabetic or numeric characters.";
      }else if(ID == "lName"){
        label.innerHTML = "Last name must contain only alphabetic or numeric characters.";
      } else if(ID == "gender"){
        label.innerHTML = "Please select one option!";
      }else{
        label.innerHTML = "Please select one option!";
      } 
     } 
    return label;
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

