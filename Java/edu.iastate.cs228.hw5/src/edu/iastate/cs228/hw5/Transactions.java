package edu.iastate.cs228.hw5;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner; 

/**
 *  
 * @author Bo Sheng Lee
 *
 */

/**
 * 
 * The Transactions class simulates video transactions at a video store. 
 *
 */
public class Transactions 
{
	
	/**
	 * The main method generates a simulation of rental and return activities.  
	 *  
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException
	{
		// 
		// 1. Construct a VideoStore object.
		// 2. Simulate transactions as in the example given in Section 4 of the 
		//    the project description.
		VideoStore store = new VideoStore("videoList1.txt");
		Scanner sc = new Scanner(System.in);
		int transaction;
		System.out.println("Transaction at Video Store\nkeys: 1 (rent)\t\t2 (bulk rent)\n\t  3 (return)\t4 (bulk return)\n\t  5 (summary)\t6 (exit)");
		do {
			System.out.print("\nTransaction: ");
			transaction = Integer.parseInt(sc.nextLine());

			if(transaction==1){
				System.out.print("Film to rent: ");
				String filmRent = sc.nextLine();
				String filmName = VideoStore.parseFilmName(filmRent);
				int filmNum = VideoStore.parseNumCopies(filmRent);
				try {
					store.videoRent(filmName,filmNum); }
				catch (IllegalArgumentException e) {
					System.out.println("Film "+filmName+" has an invalid request");
				}
				catch(FilmNotInInventoryException e){
					System.out.println("Film "+filmName+" is not in inventory");
				}
				catch (AllCopiesRentedOutException e) {
					System.out.println("Film "+filmName+" has been rented out");
				}
			}
			else if(transaction==2){
				System.out.print("Video file (rent) : ");
				String videoFileName = sc.nextLine();
				try {
					store.bulkRent(videoFileName);
					}
				catch (IllegalArgumentException e){ System.out.print(e.getMessage()); }
				catch (FilmNotInInventoryException e) {System.out.print(e.getMessage()); }
				catch (AllCopiesRentedOutException e) {System.out.print(e.getMessage()); }

			}
			else if(transaction==3){
				System.out.print("Film to return: ");
				String filmReturn = sc.nextLine();
				String filmName = VideoStore.parseFilmName(filmReturn);
				int filmNum = VideoStore.parseNumCopies(filmReturn);
				try {
					store.videoReturn(filmName,filmNum);
					}
				catch(IllegalArgumentException e){
					System.out.println("Film "+filmName+" has an invalid request");
				}
				catch (FilmNotInInventoryException e){
					System.out.println("Film "+filmName+" is not in inventory");
				}
			}
			else if(transaction==4){
				System.out.print("Video file (return) : ");
				String videoFileName = sc.nextLine();
				try {
					store.bulkReturn(videoFileName);
				}
				catch(IllegalArgumentException e){System.out.print(e.getMessage());}
				catch (FilmNotInInventoryException e) {System.out.print(e.getMessage());}
			}
			else if(transaction==5){
				System.out.print(store.transactionsSummary());
			}
			else{
				transaction=6;
			}
		}while(transaction!=6);
	}
}
