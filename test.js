var nameFine = false;
var numFine = false;

function checkName(textbox) {
  if( (textbox.value.length) >= 2){
    nameFine = true;
  }
  else
    nameFine = false;
  checkValid();
}

function checkNumber(textbox) {
  if( (textbox.value.length >= 8 ) &&  (textbox.value.length <= 11) ){
    numFine = true;
  }
  else
    numFine = false;
  checkValid();
}

function checkValid(){
  if(nameFine == true && numFine == true){
    document.getElementById('submitButton').enabled = true;
  }
  else
    document.getElementById('submitButton').enabled = false;
}
