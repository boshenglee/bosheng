package coms309;

public class StringOps {


  String capitalize(String s) {
    return s.toUpperCase();
  }

  String reverse(String s) {
    return new StringBuilder().append(s).reverse().toString();
  }

}
