package edu.iastate.cs228.hw3;
/**
 *  
 * @author Bo Sheng Lee
 *
 */

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class PrimeFactorization implements Iterable<PrimeFactor>
{
	private static final long OVERFLOW = -1;
	private long value; 	// the factored integer 
							// it is set to OVERFLOW when the number is greater than 2^63-1, the
						    // largest number representable by the type long. 
	
	/**
	 * Reference to dummy node at the head.
	 */
	private Node head;
	  
	/**
	 * Reference to dummy node at the tail.
	 */
	private Node tail;
	
	private int size;     	// number of distinct prime factors


	// ------------
	// Constructors 
	// ------------
	
    /**
	 *  Default constructor constructs an empty list to represent the number 1.
	 *  
	 *  Combined with the add() method, it can be used to create a prime factorization.  
	 */
	public PrimeFactorization() 
	{
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		size = 0;
		value =1;
	}

	
	/** 
	 * Obtains the prime factorization of n and creates a doubly linked list to store the result.   
	 * Follows the direct search factorization algorithm in Section 1.2 of the project description. 
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization(long n) throws IllegalArgumentException 
	{
		if (n<1){ throw new IllegalArgumentException();}
		else {
			value = n;
			head = new Node();
			tail = new Node();
			head.next = tail;
			tail.previous = head;
			long temp = n;
			int i =2;
			while(i*i<=temp){
				if (i > 2 && i % 2 == 0) {
					i++;
					continue;
				}
				else {
					int multiplicity = 0;
					if (temp % i == 0) {
						while (temp % i == 0) {
							temp /= i;
							multiplicity++;
						}
						add(i, multiplicity);
					}
					i++;
				}
			}
			if(temp!=1) {
				add((int) temp, 1);
			}
		}
	}
	
	
	/**
	 * Copy constructor. It is unnecessary to verify the primality of the numbers in the list.
	 * 
	 * @param pf
	 */
	public PrimeFactorization(PrimeFactorization pf)
	{
		this();
		Node pfCurrent = pf.head.next;
		while(pfCurrent != pf.tail){
			this.add(pfCurrent.pFactor.prime,pfCurrent.pFactor.multiplicity);
			pfCurrent = pfCurrent.next;
		}
		this.size = pf.size;
		this.value =pf.value;
	}
	
	/**
	 * Constructs a factorization from an array of prime factors.  Useful when the number is 
	 * too large to be represented even as a long integer. 
	 * 
	 * @param pfList
	 */
	public PrimeFactorization (PrimeFactor[] pfList)
	{
		this();
		for(int i=0;i<pfList.length;i++){
			this.add(pfList[i].prime,pfList[i].multiplicity);
		}
		this.size = pfList.length;
		this.updateValue();
	}

	// --------------
	// Primality Test
	// --------------
	
    /**
	 * Test if a number is a prime or not.  Check iteratively from 2 to the largest 
	 * integer not exceeding the square root of n to see if it divides n. 
	 * 
	 *@param n
	 *@return true if n is a prime 
	 * 		  false otherwise 
	 */
    public static boolean isPrime(long n) 
	{
		if (n <= 1) { return false;}

		for (int i = 2; i < n; i++)
			if (n % i == 0) {return false;}

		return true;
	}

   
	// ---------------------------
	// Multiplication and Division 
	// ---------------------------
	
	/**
	 * Multiplies the integer v represented by this object with another number n.  Note that v may 
	 * be too large (in which case this.value == OVERFLOW). You can do this in one loop: Factor n and 
	 * traverse the doubly linked list simultaneously. For details refer to Section 3.1 in the 
	 * project description. Store the prime factorization of the product. Update value and size. 
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public void multiply(long n) throws IllegalArgumentException 
	{
		if(n<1){ throw new IllegalArgumentException();}
		else{
			PrimeFactorization p = new PrimeFactorization(n);
			Node pNode = p.head.next;
			PrimeFactorizationIterator iter = iterator();
			while(iter.hasNext() && pNode != p.tail){
				if(iter.cursor.pFactor.prime>=pNode.pFactor.prime) {
					iter.add(pNode.pFactor);
					pNode = pNode.next;
				}
				else {
					iter.next();
				}
			}
			while (iter.cursor == tail && pNode != p.tail){
				iter.add(pNode.pFactor);
				pNode = pNode.next;
			}
		}
		if(!valueOverflow()) {
			try {
				value = Math.multiplyExact(value, n);
			} catch (ArithmeticException e) {
				value = OVERFLOW;
			}
		}
	}
	
	/**
	 * Multiplies the represented integer v with another number in the factorization form.  Traverse both 
	 * linked lists and store the result in this list object.  See Section 3.1 in the project description 
	 * for details of algorithm. 
	 * 
	 * @param pf 
	 */
	public void multiply(PrimeFactorization pf)
	{
		PrimeFactorizationIterator iter = iterator();
		PrimeFactorizationIterator iterPf = pf.iterator();
		while(iterPf.hasNext()&&iter.hasNext()){
			if(iter.cursor.pFactor.prime>=iterPf.cursor.pFactor.prime){
				iter.add(iterPf.cursor.pFactor);
				iterPf.next();
			}
			else{
				iter.next();
			}
		}
		while (iter.cursor == tail && iterPf.hasNext()) {
			iter.add(iterPf.cursor.pFactor);
			iterPf.next();
		}
		if(!valueOverflow()) {
			try {
				value = Math.multiplyExact(value, pf.value);
			} catch (ArithmeticException e) {
				value = OVERFLOW;
			}
		}
	}
	
	
	/**
	 * Multiplies the integers represented by two PrimeFactorization objects.  
	 * 
	 * @param pf1
	 * @param pf2
	 * @return object of PrimeFactorization to represent the product 
	 */
	public static PrimeFactorization multiply(PrimeFactorization pf1, PrimeFactorization pf2)
	{
		long newValue;
		try {
			newValue = Math.multiplyExact(pf1.value, pf2.value);
		}catch(ArithmeticException e){
			newValue = OVERFLOW;
		}
		return new PrimeFactorization(newValue);
	}

	
	/**
	 * Divides the represented integer v by n.  Make updates to the list, value, size if divisible.  
	 * No update otherwise. Refer to Section 3.2 in the project description for details. 
	 *  
	 * @param n
	 * @return  true if divisible 
	 *          false if not divisible 
	 * @throws IllegalArgumentException if n <= 0
	 */
	public boolean dividedBy(long n) throws IllegalArgumentException
	{
		if(n<=0){ throw new IllegalArgumentException();}
		else if(value !=-1 && value<n){return false;}
		else {
			PrimeFactorization p = new PrimeFactorization(n);
			return dividedBy(p);
		}
	}

	
	/**
	 * Division where the divisor is represented in the factorization form.  Update the linked 
	 * list of this object accordingly by removing those nodes housing prime factors that disappear  
	 * after the division.  No update if this number is not divisible by pf. Algorithm details are 
	 * given in Section 3.2. 
	 * 
	 * @param pf
	 * @return	true if divisible by pf
	 * 			false otherwise
	 */
	public boolean dividedBy(PrimeFactorization pf)
	{
		if ((value != -1 && pf.value != -1 && value < pf.value)){ return false; }
		else if(value != -1 && pf.value == -1) { return false; }
		else if (value == pf.value &&!valueOverflow()) {
			this.clearList();
			value =1;
			return true;
		}
		else{
			PrimeFactorization copy = new PrimeFactorization(this);
			PrimeFactorizationIterator iterCopy = copy.iterator();
			PrimeFactorizationIterator iterPf = pf.iterator();
			while (iterPf.hasNext()) {
				if (!iterCopy.hasNext() && iterPf.hasNext()){ return false; }
				if (iterCopy.cursor.pFactor.prime >= iterPf.cursor.pFactor.prime) {
					if (iterCopy.cursor.pFactor.prime > iterPf.cursor.pFactor.prime) { return false; }
					else if (iterCopy.cursor.pFactor.prime == iterPf.cursor.pFactor.prime && iterCopy.cursor.pFactor.multiplicity < iterPf.cursor.pFactor.multiplicity) { return false; }
					else
					{
						if (iterCopy.cursor.pFactor.multiplicity - iterPf.cursor.pFactor.multiplicity != 0) {
							iterCopy.cursor.pFactor.multiplicity = iterCopy.cursor.pFactor.multiplicity - iterPf.cursor.pFactor.multiplicity;
						} else {
							iterCopy.next();
							iterCopy.remove();
							iterCopy.previous();
							iterCopy.index = iterCopy.previousIndex();
						}
						iterPf.next();
					}
				}
				iterCopy.next();
			}
			this.head = copy.head;
			this.tail = copy.tail;
			this.size = copy.size;
			if(valueOverflow()){
				this.updateValue();
			}
			else{
				this.value /= pf.value;
			}
			return true;
		}
	}

	/**
	 * Divide the integer represented by the object pf1 by that represented by the object pf2. 
	 * Return a new object representing the quotient if divisible. Do not make changes to pf1 and 
	 * pf2. No update if the first number is not divisible by the second one. 
	 *  
	 * @param pf1
	 * @param pf2
	 * @return quotient as a new PrimeFactorization object if divisible
	 *         null otherwise 
	 */
	public static PrimeFactorization dividedBy(PrimeFactorization pf1, PrimeFactorization pf2)
	{
		if(pf1.value%pf2.value!=0){
			return null;
		}
		long newValue = pf1.value/pf2.value;
		return new PrimeFactorization(newValue);
	}

	
	// -----------------------
	// Greatest Common Divisor
	// -----------------------

	/**
	 * Computes the greatest common divisor (gcd) of the represented integer v and an input integer n.
	 * Returns the result as a PrimeFactor object.  Calls the method Euclidean() if 
	 * this.value != OVERFLOW.
	 *     
	 * It is more efficient to factorize the gcd than n, which can be much greater. 
	 *     
	 * @param n
	 * @return prime factorization of gcd
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization gcd(long n) throws IllegalArgumentException
	{
		if(n<1) {throw new IllegalArgumentException();}
		if(!valueOverflow()) {
			long gcd = Euclidean(value,n);
			return new PrimeFactorization(gcd);
		}
		else{
			PrimeFactorization p = new PrimeFactorization(n);
			return gcd(p);
		}
	}
	

	/**
	  * Implements the Euclidean algorithm to compute the gcd of two natural numbers m and n. 
	  * The algorithm is described in Section 4.1 of the project description. 
	  * 
	  * @param m
	  * @param n
	  * @return gcd of m and n. 
	  * @throws IllegalArgumentException if m < 1 or n < 1
	  */
 	public static long Euclidean(long m, long n) throws IllegalArgumentException
	{
		if(m<1 || n<1){ throw new IllegalArgumentException();}
		long large = Math.max(m,n);
		long small = Math.min(m,n);
		while(large%small !=0){
			long remainder = large%small;
			large = small;
			small = remainder;
		}
 		return small;
	}

 	
	/**
	 * Computes the gcd of the values represented by this object and pf by traversing the two lists.  No 
	 * direct computation involving value and pf.value. Refer to Section 4.2 in the project description 
	 * on how to proceed.  
	 * 
	 * @param  pf
	 * @return prime factorization of the gcd
	 */
	public PrimeFactorization gcd(PrimeFactorization pf)
	{
		PrimeFactorization newList = new PrimeFactorization();
		PrimeFactorizationIterator iter = iterator();
		PrimeFactorizationIterator iterPf = pf.iterator();
		while(iter.hasNext()){
			if(iterPf.cursor.pFactor.prime==iter.cursor.pFactor.prime){
				newList.add(iterPf.cursor.pFactor.prime,Math.min(iter.cursor.pFactor.multiplicity,iterPf.cursor.pFactor.multiplicity));
				iterPf.next();
				iter.next();
			}
			else if(iterPf.cursor.pFactor.prime<iter.cursor.pFactor.prime){
				iterPf.next();
			}
			else {
				iter.next();
			}
			if(iter.cursor==tail||iterPf.cursor==pf.tail){
				break;
			}
		}
		if(valueOverflow()||pf.valueOverflow()) {
			try {
				newList.updateValue();
			} catch (ArithmeticException e) {
				value = OVERFLOW;
			}
		}
		else{
			newList.updateValue();
		}
		return newList;
	}
	
	
	/**
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the gcd of two numbers represented by pf1 and pf2
	 */
	public static PrimeFactorization gcd(PrimeFactorization pf1, PrimeFactorization pf2)
	{
		return pf1.gcd(pf2);
	}

	// ------------
	// List Methods
	// ------------
	
	/**
	 * Traverses the list to determine if p is a prime factor. 
	 * 
	 * Precondition: p is a prime. 
	 * 
	 * @param p  
	 * @return true  if p is a prime factor of the number v represented by this linked list
	 *         false otherwise 
	 * @throws IllegalArgumentException if p is not a prime
	 */
	public boolean containsPrimeFactor(int p) throws IllegalArgumentException
	{
		if(!isPrime(p)){ throw new IllegalArgumentException();}
		Node current = head.next;
		while (current != tail)
		{
			if (current.pFactor.prime == p)
			{
				return true;
			}
			current = current.next;
		}
		return false;
	}
	
	// The next two methods ought to be private but are made public for testing purpose. Keep
	// them public 
	
	/**
	 * Adds a prime factor p of multiplicity m.  Search for p in the linked list.  If p is found at 
	 * a node N, add m to N.multiplicity.  Otherwise, create a new node to store p and m. 
	 *  
	 * Precondition: p is a prime. 
	 * 
	 * @param p  prime 
	 * @param m  multiplicity
	 * @return   true  if m >= 1
	 *           false if m < 1   
	 */
    public boolean add(int p, int m)
    {
		if(m<1){ return false; }
		if(PrimeFactorization.isPrime(p)){
			if (containsPrimeFactor(p)) {
				Node current = head.next;
				while (current != tail)
				{
					if (current.pFactor.prime == p)
					{
							current.pFactor = new PrimeFactor(p, m + current.pFactor.multiplicity);
					}
					current = current.next;
				}
			} else {
				Node current = head.next;
				while (current!= tail)
				{
					if(p<current.pFactor.prime)
					{
						link(current.previous,new Node(p,m));
						break;
					}
					current = current.next;
				}
				if(current==tail)
				{
					link(current.previous, new Node(p, m));
				}
				size++;
			}
			return true;
		}
		return false;
    }

	    
    /**
     * Removes m from the multiplicity of a prime p on the linked list.  It starts by searching 
     * for p.  Returns false if p is not found, and true if p is found. In the latter case, let 
     * N be the node that stores p. If N.multiplicity > m, subtracts m from N.multiplicity.  
     * If N.multiplicity <= m, removes the node N.  
     * 
     * Precondition: p is a prime. 
     * 
     * @param p
     * @param m
     * @return true  when p is found. 
     *         false when p is not found. 
     * @throws IllegalArgumentException if m < 1
     */
    public boolean remove(int p, int m) throws IllegalArgumentException
    {
		if(m<1){ throw new IllegalArgumentException();}
		if(!containsPrimeFactor(p)){ return false; }
    	if(PrimeFactorization.isPrime(p)){
    		Node current = head.next;
			while (current!= tail)
			{
				if(p==current.pFactor.prime)
				{
					if(current.pFactor.multiplicity>m){
						current.pFactor = new PrimeFactor(p,current.pFactor.multiplicity-m);
					}
					else{
						unlink(current);
						size--;
					}
				}
				current = current.next;
			}
			return true;
		}
    	return false;
    }


    /**
     * 
     * @return size of the list
     */
	public int size() 
	{
		return size;
	}

	
	/**
	 * Writes out the list as a factorization in the form of a product. Represents exponentiation 
	 * by a caret.  For example, if the number is 5814, the returned string would be printed out 
	 * as "2 * 3^2 * 17 * 19". 
	 */
	@Override 
	public String toString()
	{
		String finalDisplay = "";
		Node current = head.next;
		if(current !=tail) {
			while (current != tail) {
				if(current.next != tail) {
					finalDisplay += current.pFactor.toString() + " * ";
					current = current.next;
				}
				else{
					finalDisplay += current.pFactor;
					current = current.next;
				}
			}
		}
		else{
			finalDisplay += "1";
		}
		return finalDisplay;
	}

	
	// The next three methods are for testing, but you may use them as you like.  

	/**
	 * @return true if this PrimeFactorization is representing a value that is too large to be within 
	 *              long's range. e.g. 999^999. false otherwise.
	 */
	public boolean valueOverflow() {
		return value == OVERFLOW;
	}

	/**
	 * @return value represented by this PrimeFactorization, or -1 if valueOverflow()
	 */
	public long value() {
		return value;
	}

	
	public PrimeFactor[] toArray() {
		PrimeFactor[] arr = new PrimeFactor[size];
		int i = 0;
		for (PrimeFactor pf : this)
			arr[i++] = pf;
		return arr;
	}

	/**
	 * override iterator method
	 * @return iterator object
	 */
	@Override
	public PrimeFactorizationIterator iterator()
	{
	    return new PrimeFactorizationIterator();
	}
	
	/**
	 * Doubly-linked node type for this class.
	 */
    private class Node
    {
		public PrimeFactor pFactor;			// prime factor 
		public Node next;
		public Node previous;
		
		/**
		 * Default constructor for creating a dummy node.
		 */
		public Node()
		{
			pFactor = null;
		}
	    
		/**
		 * Precondition: p is a prime
		 * 
		 * @param p	 prime number 
		 * @param m  multiplicity 
		 * @throws IllegalArgumentException if m < 1 
		 */
		public Node(int p, int m) throws IllegalArgumentException 
		{
			if(m<1) { throw new IllegalArgumentException(); }
			if(PrimeFactorization.isPrime(p)) {
				pFactor = new PrimeFactor(p, m);
			}else{
				throw new IllegalArgumentException();
			}
		}   

		
		/**
		 * Constructs a node over a provided PrimeFactor object. 
		 * 
		 * @param pf
		 * @throws IllegalArgumentException
		 */
		public Node(PrimeFactor pf)  
		{
			pFactor = pf.clone();
		}


		/**
		 * Printed out in the form: prime + "^" + multiplicity.  For instance "2^3". 
		 * Also, deal with the case pFactor == null in which a string "dummy" is 
		 * returned instead.  
		 */
		@Override
		public String toString() 
		{
			String finalDisplay = "";
			if(pFactor!=null) {
				finalDisplay += pFactor.prime;
				if (pFactor.multiplicity != 1) {
					finalDisplay += "^" + pFactor.multiplicity;
				}
			}
			else{
				finalDisplay += "dummy";
			}
			return finalDisplay;
		}
    }

    
    private class PrimeFactorizationIterator implements ListIterator<PrimeFactor>
    {
        // Class invariants: 
        // 1) logical cursor position is always between cursor.previous and cursor
        // 2) after a call to next(), cursor.previous refers to the node just returned 
        // 3) after a call to previous() cursor refers to the node just returned 
        // 4) index is always the logical index of node pointed to by cursor

        private Node cursor = head.next;
        private Node pending = null;    // node pending for removal
        private int index = 0;
      
        /**
    	 * Default constructor positions the cursor before the smallest prime factor.
    	 */
    	public PrimeFactorizationIterator()
    	{
			cursor = head.next;
			pending = null;
			index =0;
    	}

		/**
		 * check if iterator hasNext()
		 * @return index < size
		 */
		@Override
    	public boolean hasNext() { return index < size; }

		/**
		 * check if iterator hasPrevious
		 * @return index > 0
		 */
		@Override
    	public boolean hasPrevious() { return index > 0; }

		/**
		 * iterate cursor to next position
		 * @return cursor before next() is called
		 */
    	@Override 
    	public PrimeFactor next() 
    	{
			if (!hasNext()) throw new NoSuchElementException();
			pending = cursor;
			PrimeFactor temp = cursor.pFactor;
			cursor = cursor.next;
			index++;
			return temp;
    	}

		/**
		 * iterate cursor to previous position
		 * @return cursor before previous() is called
		 */
    	@Override 
    	public PrimeFactor previous() 
    	{
			if (!hasPrevious()) throw new NoSuchElementException();
			cursor = cursor.previous;
			index--;
			pending = cursor;
			return cursor.pFactor;
    	}

   
    	/**
    	 *  Removes the prime factor returned by next() or previous()
    	 *  
    	 *  @throws IllegalStateException if pending == null 
    	 */
    	@Override
    	public void remove() throws IllegalStateException
    	{
			if (pending == null) { throw new IllegalStateException(); }
			else
			{
				unlink(pending);
				--size;
				pending = null;
			}
    	}
 
 
    	/**
    	 * Adds a prime factor at the cursor position.  The cursor is at a wrong position 
    	 * in either of the two situations below: 
    	 * 
    	 *    a) pf.prime < cursor.previous.pFactor.prime if cursor.previous != head. 
    	 *    b) pf.prime > cursor.pFactor.prime if cursor != tail. 
    	 * 
    	 * Take into account the possibility that pf.prime == cursor.pFactor.prime. 
    	 * 
    	 * Precondition: pf.prime is a prime. 
    	 * 
    	 * @param pf  
    	 * @throws IllegalArgumentException if the cursor is at a wrong position. 
    	 */
    	@Override
        public void add(PrimeFactor pf) throws IllegalArgumentException 
        {
        	if(PrimeFactorization.isPrime(pf.prime)) {
				if (cursor != tail && cursor.previous != head) {
					if (pf.prime < cursor.previous.pFactor.prime && cursor.previous != head || pf.prime > cursor.pFactor.prime && cursor != tail) {
						throw new IllegalArgumentException();
					} else if (cursor.pFactor.prime == pf.prime) {
						cursor.pFactor = new PrimeFactor(pf.prime, cursor.pFactor.multiplicity + pf.multiplicity);
					} else {
						Node temp = new Node(pf);
						link(cursor.previous, temp);
						++index;
						++size;
					}
				} else if (cursor.previous == head) {
					if (pf.prime > cursor.pFactor.prime && cursor != tail) {
						throw new IllegalArgumentException();
					} else {
						if (cursor.pFactor.prime == pf.prime) {
							cursor.pFactor = new PrimeFactor(pf.prime, cursor.pFactor.multiplicity + pf.multiplicity);
						} else {
							Node temp = new Node(pf);
							link(cursor.previous, temp);
							++index;
							++size;
						}
					}
				} else {
					if (pf.prime < cursor.previous.pFactor.prime) {
						throw new IllegalArgumentException();
					}
					Node temp = new Node(pf);
					link(cursor.previous, temp);
					++index;
					++size;
				}
			}
        	else{
        		throw new IllegalArgumentException();
			}
        }

		/**
		 * Find the index of the cursor
		 * @return index
		 */
    	@Override
		public int nextIndex() 
		{
			return index;
		}

		/**
		 * find the index in front of cursor
		 * @return index -1
		 */
    	@Override
		public int previousIndex() 
		{
			return index - 1;
		}

		@Deprecated
		@Override
		public void set(PrimeFactor pf) 
		{
			throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support set method");
		}
        
    	// Other methods you may want to add or override that could possibly facilitate 
    	// other operations, for instance, addition, access to the previous element, etc.
    	// 
    	// ...
    	// 
    }

    
    // --------------
    // Helper methods 
    // -------------- 
    
    /**
     * Inserts toAdd into the list after current without updating size.
     * 
     * Precondition: current != null, toAdd != null
     */
    private void link(Node current, Node toAdd)
    {
		toAdd.previous = current;
		toAdd.next = current.next;
		current.next.previous = toAdd;
		current.next = toAdd;
    }

	 
    /**
     * Removes toRemove from the list without updating size.
     */
    private void unlink(Node toRemove)
    {
		toRemove.previous.next = toRemove.next;
		toRemove.next.previous = toRemove.previous;
    }


    /**
	  * Remove all the nodes in the linked list except the two dummy nodes. 
	  * 
	  * Made public for testing purpose.  Ought to be private otherwise. 
	  */
	public void clearList()
	{
		link(head,tail);
		size = 0;
	}	
	
	/**
	 * Multiply the prime factors (with multiplicities) out to obtain the represented integer.  
	 * Use Math.multiply(). If an exception is throw, assign OVERFLOW to the instance variable value.  
	 * Otherwise, assign the multiplication result to the variable. 
	 * 
	 */
	private void updateValue()
	{
		try {
			long temp1 =1;
			long temp2 =1;
			Node current = head.next;
			while (current != tail) {
				for (int i = 0; i < current.pFactor.multiplicity; i++) {
					temp1 = Math.multiplyExact(temp1, current.pFactor.prime);
				}
				temp2 = Math.multiplyExact(temp2,temp1);
				temp1 =1;
				current = current.next;
			}
			value =temp2;
		} catch (ArithmeticException e) {
			value = OVERFLOW;
		}
	}
}
