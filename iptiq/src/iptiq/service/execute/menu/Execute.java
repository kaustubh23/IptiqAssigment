package iptiq.service.execute.menu;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

import iptiq.service.loadbalancer.impl.RoundRobin;
import iptiq.service.providers.ProviderDetails;

public class Execute {

	public static void main(String[] args) {

		RoundRobin roundrobin = new RoundRobin();
		roundrobin.check();
		int choice = 0;

		/*********************************************************/
		do {
			choice = menu();
			switch (choice) {
			case 1:
				System.out.println("Enter server id you want to register");
				Scanner scan = new Scanner(System.in);
				String identifier = scan.next();
				Random rand = new Random();
				int upperbound = 2;
				int int_random = rand.nextInt(upperbound);
				ProviderDetails details = new ProviderDetails(int_random, UUID.randomUUID().toString());
				details.setName(identifier);
				if (roundrobin.registerProvider(identifier, details)) {
					System.out.println("Server Added Successfully");

				} else {
					System.out.println("Server Limit has reached");
				}
				break;
			case 2:
				String response=	roundrobin.get();
				if (response.isEmpty()) {
					System.out.println("No server found");
				} else {
					System.out.println("Server output " + response);
				}

				break;
			case 3:
				System.out.println("Enter server key you want to remove");
				String rmkey = "";
				roundrobin.printList(3);
				try {
					Scanner scanrm = new Scanner(System.in);
					rmkey = scanrm.next();
				} catch (InputMismatchException e) {
					System.out.print("Please enter a valid entry ");
				}
				if (roundrobin.removeServer(rmkey)) {
					System.out.println("Server removed Successfully");
				} else {
					System.out.println("Wrong key");
				}

				break;
			case 4:
				roundrobin.printList(3);

				break;
			case 5:
				System.out.println("Override max concurrent calls limit");
				Scanner limit = new Scanner(System.in);
				int conlimit = limit.nextInt();
				roundrobin.setConcurrentCallsLimit(conlimit);
				System.out.println("Limit is updated successfully");
				break;
			case 6:
				System.out.println("Enter server id of unhealthy server for which you want to change health status");
				String unkey = "";
				roundrobin.printList(2);
				try {
					Scanner scanun = new Scanner(System.in);
					unkey = scanun.next();
					if (roundrobin.changeServerStatus(unkey)) {
						System.out.println("Server removed Successfully");
					} else {
						System.out.println("Wrong key");
					}
				} catch (InputMismatchException e) {
					System.out.print("Please enter a valid entry ");
				}
				

				break;
			case 7:
				break;
			default:
				System.out.println(" The user input an unexpected choice.");
				break;
			}
		} while (choice != 7);
	}

	public static int menu() {

		int selection = 0;

		/***************************************************/

		System.out.println("Choose from these choices");
		System.out.println("-------------------------\n");
		System.out.println("1 - Register a server");
		System.out.println("2 - Call random server");
		System.out.println("3 - Remove server");
		System.out.println("4 - Server List");
		System.out.println("5 - Set concurrent call limit");
		System.out.println("6 - Change health status");
		System.out.println("7 - Quit");
		try {
			Scanner input = new Scanner(System.in);
			selection = input.nextInt();
		} catch (InputMismatchException e) {
			System.out.print("Please enter a valid entry ");
		}
		return selection;
	}
}
