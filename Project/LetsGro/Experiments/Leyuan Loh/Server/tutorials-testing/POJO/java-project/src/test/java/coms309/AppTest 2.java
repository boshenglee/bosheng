package coms309;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class AppTest
{


    @Test
    public void testDoSomeOperations_Capitalize() {
      App a = new App();

      // MOCK THE STRINGOPS
      StringOps sops = mock(StringOps.class);
      when(sops.capitalize("hello")).thenReturn("HELLO");


      // TEST THE SUT by calling it
      String result = a.doSomeOperations("hello", "capitalize", sops);
      assertEquals("HELL", result);
    }

    @Test
    public void testDoSomeOperations_CapitalizeNumbers() {
      App a = new App();

      // MOCK THE STRINGOPS
      StringOps sops = mock(StringOps.class);
      when(sops.capitalize("hello")).thenReturn("HELLO123");


      // TEST THE SUT by calling it
      String result = a.doSomeOperations("hello123", "capitalize", sops);
      assertEquals("HELLO123", result);
    }

    @Test
    public void testDoSomeOperations_Reverse() {
      App a = new App();

      // MOCK THE STRINGOPS
      StringOps sops = mock(StringOps.class);
      when(sops.reverse("hello")).thenReturn("olle");

      // TEST THE SUT by calling it
      String result = a.doSomeOperations("hello", "reverse", sops);
      assertEquals("olleh", result);
    }

    @Test
    public void testDoSomeOperations_ReverseNumbers() {
      App a = new App();

      // MOCK THE STRINGOPS
      StringOps sops = mock(StringOps.class);
      when(sops.reverse("hello123")).thenReturn("321olleh");

      // TEST THE SUT by calling it
      String result = a.doSomeOperations("hello123", "reverse", sops);
      assertEquals("321olleh", result);

      // verify if the correct method was called!
      verify(sops, times(1)).reverse("hello");
    }
}
