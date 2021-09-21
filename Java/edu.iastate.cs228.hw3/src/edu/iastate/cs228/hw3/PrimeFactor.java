package edu.iastate.cs228.hw3;

/**
 *  
 * @author Bo Sheng Lee
 *
 */

public class PrimeFactor
{
	public static void main(String[] args) {
//		PrimeFactor pf = new PrimeFactor(7, 3);
//		System.out.println(pf.toString());

		//Test iterator and list methods

		PrimeFactorization test = new PrimeFactorization();
		test.add(2, 3);
		System.out.println(test.toString());
		test.clearList();
		System.out.println(test.toString());

//		test.add(7, 4);
//		System.out.println(test.toString());
//
//		test.add(3, 5);
//		System.out.println(test.toString());
//
//		test.add(2, 1);
//		System.out.println(test.toString());
//
//		test.add(5, 7);
//		System.out.println(test.toString());
//		test.add(2, 3);
//		test.add(17,13);
//		test.add(5, 20);
//		test.add(11, 1);
//
//		System.out.println(test.toString());
//
//		test.remove(5, 26);
//		System.out.println(test.toString());
//
//		test.remove(2, 8);
//		System.out.println(test.toString());
//
//		test.remove(3, 5);
//		System.out.println(test.toString());


		//Test Prime Test
		/*
		System.out.println(PrimeFactorization.isPrime(2));
		System.out.println(PrimeFactorization.isPrime(4));
		System.out.println(PrimeFactorization.isPrime(7));
		System.out.println(PrimeFactorization.isPrime(97));
		System.out.println(PrimeFactorization.isPrime(20));
		*/

		//Test Constructors

		/*
		PrimeFactor[] arr = new PrimeFactor[5];
		arr[0] = new PrimeFactor(2, 3);
		arr[1] = new PrimeFactor(3, 4);
		arr[2] = new PrimeFactor(7, 2);
		arr[3] = new PrimeFactor(5, 3);
		arr[4] = new PrimeFactor(2, 1);

		PrimeFactorization dll = new PrimeFactorization(arr);
		System.out.println(dll.toString());

		PrimeFactorization dll2 = new PrimeFactorization(dll);
		System.out.println(dll2.toString());
		dll2.add(11, 2);

		System.out.println(dll2.toString());
		System.out.println(dll.toString());


		long test = 25480;
//		long test = 330;
		PrimeFactorization dll3 = new PrimeFactorization(test);
		System.out.println(dll3.toString());

		*/

		//Test Multipliers
		/*
		PrimeFactor[] arr = new PrimeFactor[4];
		arr[0] = new PrimeFactor(2, 1);
		arr[1] = new PrimeFactor(3, 4);
		arr[2] = new PrimeFactor(11, 2);
		arr[3] = new PrimeFactor(13, 3);

		PrimeFactorization dll = new PrimeFactorization(arr);
		System.out.println(dll.toString());

		//multiplies 2^3 * 5 * 7^2 * 13
		//dll.multiply(25480);
		dll.multiply(13);
		//dll.multiply(7);
		System.out.println(dll.toString());



		//Test third multiplication method
		PrimeFactorization dll1 = new PrimeFactorization(25480);
		System.out.println(dll1.toString());

		PrimeFactorization dll2 = new PrimeFactorization(43465);
		System.out.println(dll2.toString());

		PrimeFactorization dll3 = PrimeFactorization.multiply(dll1, dll2);
		System.out.println(dll3.toString());
		*/


		//Test divide
		/*
		PrimeFactorization dll1 = new PrimeFactorization(25480);
		System.out.println(dll1.toString());

		int test = 40;
		PrimeFactorization dll2 = new PrimeFactorization(test);
		System.out.println(dll2.toString());
		System.out.println(dll1.dividedBy(test));

		System.out.println(dll1.toString());
		*/




		/*
		PrimeFactorization p1 =new PrimeFactorization(2354);
		System.out.println(p1.toString());
		PrimeFactorization p2 =new PrimeFactorization(107 * 13 * 2);
		System.out.println(p2.toString());
		PrimeFactorization p3= PrimeFactorization.dividedBy(p1,p2);
		System.out.println(p3.toString());
		*/

		//Test Euclidean
		//System.out.println(PrimeFactorization.Euclidean(12, 42));

		//PrimeFactorization stuff
		/*
		PrimeFactorization p1 =new PrimeFactorization(2*2 *5*7*17*19);
		System.out.println(p1.toString());
		PrimeFactorization p2 =new PrimeFactorization(2*2*2*7*13*19);
		System.out.println(p2.toString());

		System.out.println(p1.gcd(2*2*2*7*13*19).toString());

		System.out.println(p1.gcd(p2).toString());
		System.out.println(PrimeFactorization.gcd(p1, p2).toString());
	}

		 */
	}
	public int prime; 		 // prime factor
	public int multiplicity; // number of times the prime factor appears in a factorization



	/**
	 * Precondition: p is a prime number.  
	 * 
	 * @param p	 prime
	 * @param m  multiplicity
	 * @throws IllegalArgumentException if m < 1 
	 */
	public PrimeFactor(int p, int m) throws IllegalArgumentException
	{
		if(m<1){ throw new IllegalArgumentException(); }
		if(PrimeFactorization.isPrime(p)) {
			prime = p;
			multiplicity = m;
		}else{
			throw new IllegalArgumentException();
		}
	}

	/**
	 *Clone a PrimeFactor object
	 * @return new clone PrimeFactor object
	 */
	@Override
	public PrimeFactor clone() 
	{
		return new PrimeFactor(prime, multiplicity);
	}

	/**
	 * Prints out, for instance "2^3" if prime == 2 and multiplicity == 3, or "5" if 
	 * prime == 5 and multiplicity == 1.
	 * @return String in the new format
	 */
	@Override
	public String toString() 
	{
		String finalDisplay = "";
		finalDisplay += prime;
		if(multiplicity!=1)
		{
			finalDisplay += "^" + multiplicity;
		}
		return finalDisplay;
	}
}
