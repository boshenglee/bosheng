var rs = require('readline-sync');

var fNum1 = rs.question('1st Number: ');
var fNum2 = rs.question('2nd Number: ');
var fNum3 = rs.question('3rd Number: ');
var fNum4 = rs.question('4th Number: ');


function factorial(){
  var total = 1;
  for(let i =1;i<=fNum1;i++){
     total = total*i;
  }
  return total;
}

function sumDigit(){
  var sum =0;
  for(let i =0;i<fNum2.length;i++){
    sum = sum + +fNum2.charAt(i);
  }
  return sum;
}

function reverse(){
  var str="";
  for(let i=fNum3.length-1;i>=0;i--){
    str = str + fNum3.charAt(i);
  }
  return str;
}

function palindrome(){
  var result = "True"
  for (let i = 0; i < fNum4.length/2; i++) {
  if (fNum4[i] !== fNum4[fNum4.length - 1 - i]) {
    result = "False";
  }
 }
 return result;
}
var ans1 = factorial();
var ans2 = sumDigit();
var ans3 = reverse();
var ans4 = palindrome();

console.log("Factorial of the 1st number is = "+ ans1);
console.log("The sum of all digits of the 2nd number is = "+ans2);
console.log("The reverse of the 3rd number is = "+ans3);
console.log("Is the 4th number a palindrome (True/False)? = "+ans4);
