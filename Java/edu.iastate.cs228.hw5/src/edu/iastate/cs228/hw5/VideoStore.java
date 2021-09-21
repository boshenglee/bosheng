package edu.iastate.cs228.hw5;

import sun.lwawt.macosx.CSystemTray;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 
 * @author Bo Sheng Lee
 *
 */

public class VideoStore 
{
	protected SplayTree<Video> inventory;     // all the videos at the store
	
	// ------------
	// Constructors 
	// ------------
	
    /**
     * Default constructor sets inventory to an empty tree. 
     */
    public VideoStore()
    {
    	// no need to implement. 
    }
    
    
	/**
	 * Constructor accepts a video file to create its inventory.  Refer to Section 3.2 of  
	 * the project description for details regarding the format of a video file. 
	 * 
	 * Calls setUpInventory(). 
	 * 
	 * @param videoFile  no format checking on the file
	 * @throws FileNotFoundException
	 */
    public VideoStore(String videoFile) throws FileNotFoundException  
    {
		inventory = new SplayTree<Video>();
		setUpInventory(videoFile);
    }
    
    
   /**
     * Accepts a video file to initialize the splay tree inventory.  To be efficient, 
     * add videos to the inventory by calling the addBST() method, which does not splay. 
     * 
     * Refer to Section 3.2 for the format of video file. 
     * 
     * @param  videoFile  correctly formated if exists
     * @throws FileNotFoundException 
     */
    public void setUpInventory(String videoFile) throws FileNotFoundException
    {
		inventory.root = null;
		bulkImport(videoFile);
    }
	
    
    // ------------------
    // Inventory Addition
    // ------------------
    
    /**
     * Find a Video object by film title. 
     * 
     * @param film
     * @return the object have the same name with parameter film
     */
	public Video findVideo(String film) 
	{
		return inventory.findElement(new Video(film));
	}


	/**
	 * Updates the splay tree inventory by adding a number of video copies of the film.  
	 * (Splaying is justified as new videos are more likely to be rented.) 
	 * 
	 * Calls the add() method of SplayTree to add the video object.  
	 * 
	 *     a) If true is returned, the film was not on the inventory before, and has been added.  
	 *     b) If false is returned, the film is already on the inventory. 
	 *     
	 * The root of the splay tree must store the corresponding Video object for the film. Update 
	 * the number of copies for the film.  
	 * 
	 * @param film  title of the film
	 * @param n     number of video copies 
	 */
	public void addVideo(String film, int n)  
	{
		boolean result = inventory.add(new Video(film,n));
		if(!result){
			Video v = inventory.getRoot();
			v.addNumCopies(n);
		}
	}
	

	/**
	 * Add one video copy of the film. 
	 * 
	 * @param film  title of the film
	 */
	public void addVideo(String film)
	{
		boolean result = inventory.add(new Video(film,1));
		if(!result){
			Video v = inventory.getRoot();
			v.addNumCopies(1);
		}
	}
	

	/**
     * Update the splay trees inventory by adding videos.  Perform binary search additions by 
     * calling addBST() without splaying.
	 *
     * The videoFile format is given in Section 3.2 of the project description. 
     * 
     * @param videoFile  correctly formated if exists 
     * @throws FileNotFoundException
     */
    public void bulkImport(String videoFile) throws FileNotFoundException
    {
		File openFile = new File(videoFile);
		Scanner sc = new Scanner(openFile);
		while(sc.hasNextLine()){
			String line =sc.nextLine();
			String filmName = parseFilmName(line);
			int filmNum = parseNumCopies(line);
			Video v = new Video(filmName,filmNum);
			inventory.addBST(v);
		}
    }

    
    // ----------------------------
    // Video Query, Rental & Return 
    // ----------------------------
    
	/**
	 * Search the splay tree inventory to determine if a video is available. 
	 * 
	 * @param  film
	 * @return true if available
	 */
	public boolean available(String film)
	{
		Video v = findVideo(film);
		if(v.getNumAvailableCopies()>0){ return true;}
		return false ;
	}

	
	
	/**
     * Update inventory. 
     * 
     * Search if the film is in inventory by calling findElement(new Video(film, 1)). 
     * 
     * If the film is not in inventory, prints the message "Film <film> is not 
     * in inventory", where <film> shall be replaced with the string that is the value 
     * of the parameter film.  If the film is in inventory with no copy left, prints
     * the message "Film <film> has been rented out".
     * 
     * If there is at least one available copy but n is greater than the number of 
     * such copies, rent all available copies. In this case, no AllCopiesRentedOutException
     * is thrown.  
     * 
     * @param film   
     * @param n 
     * @throws IllegalArgumentException      if n <= 0 or film == null or film.isEmpty()
	 * @throws FilmNotInInventoryException   if film is not in the inventory
	 * @throws AllCopiesRentedOutException   if there is zero available copy for the film.
	 */
	public void videoRent(String film, int n) throws IllegalArgumentException, FilmNotInInventoryException,  
									     			 AllCopiesRentedOutException 
	{
		Video v = findVideo(film);
		if(n<=0||film == null||film.isEmpty()){
			throw new IllegalArgumentException();}
		if(v==null){
			throw new FilmNotInInventoryException();
		}
		if(!available(film)){
			throw new AllCopiesRentedOutException();
		}
		v.rentCopies(n);
	}

	
	/**
	 * Update inventory.
	 * 
	 *    1. Calls videoRent() repeatedly for every video listed in the file.  
	 *    2. For each requested video, do the following: 
	 *       a) If it is not in inventory or is rented out, an exception will be 
	 *          thrown from videoRent().  Based on the exception, prints out the following 
	 *          message: "Film <film> is not in inventory" or "Film <film> 
	 *          has been rented out." In the message, <film> shall be replaced with 
	 *          the name of the video. 
	 *       b) Otherwise, update the video record in the inventory.
	 * 
	 * For details on handling of multiple exceptions and message printing, please read Section 3.4 
	 * of the project description. 
	 *       
	 * @param videoFile  correctly formatted if exists
	 * @throws FileNotFoundException
     * @throws IllegalArgumentException     if the number of copies of any film is <= 0
	 * @throws FilmNotInInventoryException  if any film from the videoFile is not in the inventory 
	 * @throws AllCopiesRentedOutException  if there is zero available copy for some film in videoFile
	 */
	public void bulkRent(String videoFile) throws FileNotFoundException, IllegalArgumentException, 
												  FilmNotInInventoryException, AllCopiesRentedOutException 
	{
		File openFile = new File(videoFile);
		Scanner sc = new Scanner(openFile);
		String message = "\n";
		boolean[] exception = {false,false,false,false}; 	//exception[0] = FileNotFoundException
															//exception[1] = IllegalArgumentException
															//exception[2] = FilmNotInInventoryException
															//exception[3] = AllCopiesRentedOutException
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String filmName = parseFilmName(line);
			int filmNum = parseNumCopies(line);

			try{videoRent(filmName, filmNum);}
			catch(IllegalArgumentException e){
				message+= "Film "+filmName+" has an invalid request\n";
				exception[1] = true;}
			catch( FilmNotInInventoryException e){
				message +="Film "+filmName+" is not in inventory\n";
				exception[2] = true;}
			catch( AllCopiesRentedOutException e){
				message += "Film "+filmName+" has been rented out\n";
				exception[3] = true;}
		}
		for(int i =0;i<exception.length;i++){
			if(exception[i]){
				if(i==1){ throw new IllegalArgumentException(message);}
				if(i==2){ throw new FilmNotInInventoryException(message);}
				if(i==3){ throw new AllCopiesRentedOutException(message);}
			}
		}
	}

	
	/**
	 * Update inventory.
	 * 
	 * If n exceeds the number of rented video copies, accepts up to that number of rented copies
	 * while ignoring the extra copies. 
	 * 
	 * @param film
	 * @param n
	 * @throws IllegalArgumentException     if n <= 0 or film == null or film.isEmpty()
	 * @throws FilmNotInInventoryException  if film is not in the inventory
	 */
	public void videoReturn(String film, int n) throws IllegalArgumentException, FilmNotInInventoryException 
	{
		Video v = findVideo(film);
		if(n<=0||film==null||film.isEmpty()){ throw new IllegalArgumentException();}
		if(v==null){ throw new FilmNotInInventoryException();}
		v.returnCopies(n);

	}
	
	
	/**
	 * Update inventory. 
	 * 
	 * Handles excessive returned copies of a film in the same way as videoReturn() does.  See Section 
	 * 3.4 of the project description on how to handle multiple exceptions. 
	 * 
	 * @param videoFile
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException    if the number of return copies of any film is <= 0
	 * @throws FilmNotInInventoryException if a film from videoFile is not in inventory
	 */
	public void bulkReturn(String videoFile) throws FileNotFoundException, IllegalArgumentException,
													FilmNotInInventoryException
	{
		File openFile = new File(videoFile);
		Scanner sc = new Scanner(openFile);
		String message = "\n";
		boolean[] exception = {false,false,false,false};

		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String filmName = parseFilmName(line);
			int filmNum = parseNumCopies(line);
			try {
				videoReturn(filmName, filmNum);
			}catch(IllegalArgumentException e){
				message+= "Film "+filmName+" has an invalid request\n";
				exception[1] = true;
			}catch(FilmNotInInventoryException e){
				message +="Film "+filmName+" is not in inventory\n";
				exception[2] = true;
			}
		}
		for(int i =0;i<exception.length;i++){
			if(exception[i]){
				if(i==1){ throw new IllegalArgumentException(message);}
				if(i==2){ throw new FilmNotInInventoryException(message);}
			}
		}
	}

		
	

	// ------------------------
	// Methods without Splaying
	// ------------------------
		
	/**
	 * Performs inorder traversal on the splay tree inventory to list all the videos by film 
	 * title, whether rented or not.  Below is a sample string if printed out: 
	 * 
	 * 
	 * Films in inventory: 
	 * 
	 * A Streetcar Named Desire (1) 
	 * Brokeback Mountain (1) 
	 * Forrest Gump (1)
	 * Psycho (1) 
	 * Singin' in the Rain (2)
	 * Slumdog Millionaire (5) 
	 * Taxi Driver (1) 
	 * The Godfather (1) 
	 * 
	 * 
	 * @return the element in inventory
	 */
	public String inventoryList()
	{
		String finalDisplay ="Films in inventory:\n\n";
		Iterator<Video> i = inventory.iterator();
		while(i.hasNext()){
			Video v = i.next();
			finalDisplay += v.getFilm()+" ("+v.getNumCopies()+")\n";
		}
		return finalDisplay;
	}

	
	/**
	 * Calls rentedVideosList() and unrentedVideosList() sequentially.  For the string format, 
	 * see Transaction 5 in the sample simulation in Section 4 of the project description. 
	 *   
	 * @return the list of rented and unrented list
	 */
	public String transactionsSummary()
	{
		return rentedVideosList()+"\n"+unrentedVideosList();
	}	
	
	/**
	 * Performs inorder traversal on the splay tree inventory.  Use a splay tree iterator.
	 * 
	 * Below is a sample return string when printed out:
	 * 
	 * Rented films: 
	 * 
	 * Brokeback Mountain (1)
	 * Forrest Gump (1) 
	 * Singin' in the Rain (2)
	 * The Godfather (1)
	 * 
	 * 
	 * @return a string of rented flm
	 */
	private String rentedVideosList()
	{
		String finalDisplay = "Rented films:\n\n";
		Iterator<Video> i = inventory.iterator();
		while(i.hasNext()){
			Video v = i.next();
			if(v.getNumRentedCopies()>0){
				finalDisplay +=v.getFilm()+" ("+v.getNumRentedCopies()+")\n";
			}
		}
		return finalDisplay;
	}

	
	/**
	 * Performs inorder traversal on the splay tree inventory.  Use a splay tree iterator.
	 * Prints only the films that have unrented copies. 
	 * 
	 * Below is a sample return string when printed out:
	 * 
	 * 
	 * Films remaining in inventory:
	 * 
	 * A Streetcar Named Desire (1) 
	 * Forrest Gump (1)
	 * Psycho (1) 
	 * Slumdog Millionaire (4) 
	 * Taxi Driver (1) 
	 * 
	 * 
	 * @return a string of unrented film
	 */
	private String unrentedVideosList()
	{
		String finalDisplay = "Films Remaining in inventory:\n\n";
		Iterator<Video> i = inventory.iterator();
		while(i.hasNext()){
			Video v = i.next();
			if(v.getNumAvailableCopies()>0){
				finalDisplay +=v.getFilm()+" ("+v.getNumAvailableCopies()+")\n";
			}
		}
		return finalDisplay;
	}	

	
	/**
	 * Parse the film name from an input line. 
	 * 
	 * @param line
	 * @return get the file name form the input line
	 */
	public static String parseFilmName(String line) 
	{
		if (line.charAt(line.length() - 1) != ')') {
			return line.trim();
		}else{
			String[] splited = line.split("\\(");
			return splited[0].trim();
		}
	}
	
	
	/**
	 * Parse the number of copies from an input line. 
	 * 
	 * @param line
	 * @return geth the number of copies of file of the input line
	 */
	public static int parseNumCopies(String line) 
	{
		if (line.charAt(line.length() - 1) != ')') {
			return 1;
		}else{
			String[] splited = line.split("\\(");
			String temp = splited[1];
			int rentNumber = Integer.parseInt(temp.substring(0, temp.length() - 1));
			if (rentNumber < 0) {
				rentNumber = 0;
			}
			return rentNumber;
		}
	}
}
