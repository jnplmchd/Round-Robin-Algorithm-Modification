import java.util.LinkedList;
import java.util.Queue;

public class RoundRobinMod {

		public static void roundRobin(int id[], int arrivalTime[], int serviceTime[]) {
			
			int clockTime = 0;
			int contextSwitch = 0;					
			int serviceT[] = new int[serviceTime.length]; 
	        int arrivalT[] = new int[arrivalTime.length]; 
	        boolean flag = false;
	        
	        int startTime[] = new int[id.length];           //clock time at arrival
			int endTime[] = new int[id.length];             //clock time process finishes
			int turnaroundTime[] = new int[id.length];      //endTime - arrival time
			int interarrivalTime[] = new int[id.length];    //start time - previous start time
			int initialwaitTime[] = new int[id.length];     //start time - arrival time
			int totalwaitTime[] = new int[id.length];       //turnaround time - service time
			                                                 //end time - arrival time
			int turnaroundtimeAvg = 0;                      //average of all turnaround times
			double servicetimeAvg  = 0.0;                   //reciprocal of average service rate (num of processes/final end time)
			double totalwaitTimeAvg = 0.0;
			
			//modified dynamic quantum 
			int quantum = 0;            //the average of remaining CPU service times in the queue
			int serviceTSum = 0;        //sum of remaining service times in queue
			int count = 0;              //number of processes in queue
			
			boolean flag2 = true;
	        //copying arrays to not change initial service time
	        for (int i = 0; i < serviceT.length; i++) { 
	            serviceT[i] = serviceTime[i]; 
	            arrivalT[i] = arrivalTime[i]; 
	        } 
	        
	        
	        
	        //to be able to change start time values
	        for (int i = 0; i < id.length; i++) { 
	            startTime[i] = 1; 
	        }
	        
	
	        
			Queue<Integer> queue = new LinkedList<>();
			
			
			
			//makes sure the queue starts with one   (if all of the arrival times were above the initial clock time then this for loop would have to be changed accordingly)			
			while (flag == false) {
				for (int i = 0; i < arrivalT.length; i++) {			
					if (arrivalT[i] <= clockTime) {				
						queue.add(id[i]);             //add process 'i' to the queue    																										
						
						//modified dynamic quantum
						count = count + 1;                         //process added to queue
						serviceTSum = serviceTSum + serviceT[i];   //increase serviceTSum by the remaining 
						                                           //service time of the added process
						quantum = serviceTSum/count;               //the new quantum based on the average 
						                                           //of remaining service time of processes in the queue
						
						//adding first start time
						startTime[i] = clockTime;
						
						//System.out.println("process added in beginning: " + queue);			
					}					
				}
				flag = true;			
			}
		
				
			
			//loops until the queue is empty	
			while (queue.size() != 0) {
					
				//System.out.println("time: " + clockTime);
				
				//increase clock time based on either service time left (if less than quantum) or quantum	
				//decrease service time based on either service time left (if less than quantum) or quantum
				if (serviceT[queue.element()] < quantum) {	
					clockTime = clockTime + serviceT[queue.element()];	         
					clockTime = clockTime + contextSwitch;                       //adding the context switch overhead
					
					serviceT[queue.element()] = serviceT[queue.element()] - serviceT[queue.element()];   	
				}	
				else {	
					clockTime = clockTime + quantum;
					clockTime = clockTime + contextSwitch;
					
					serviceT[queue.element()] = serviceT[queue.element()] - quantum;                     		
				}
				
			
				
				//System.out.println("Time: " + clockTime);	
				//System.out.println("process: " + queue.element() + " has " + serviceT[queue.element()] + " left at time: " + clockTime);	
				//end time is found once service time becomes 0
				if(serviceT[queue.element()] == 0) {
					endTime[queue.element()] = clockTime;
				}
			
				endTime[queue.element()] = clockTime;
				
				count = count - 1;
				serviceTSum = serviceTSum - serviceT[queue.element()];
							
				if(count != 0) {
					quantum = serviceTSum/count;	
				}
				else {
					quantum = 0;
				}
		
				
				//remove first element in queue
				queue.remove();
				//modified dynamic quantum
				
			
				
						
				//System.out.println("after a process is removed: " + queue);		
			
				//In order to include the 100 processes this code was added
				
				//1: add to queue 
				
				//2: while queue is empty and all service times are not 0 add to clock time based on quantum
				
				//3: add to queue again
				
				
				
				//1
				for (int k = 0; k < arrivalT.length; k++) {
					if (arrivalT[k] <= clockTime && queue.contains(k) != true && serviceT[k] != 0) {
						queue.add(id[k]);   
						//modified dynamic quantum
						count = count + 1;
						serviceTSum = serviceTSum + serviceT[k];
						quantum = serviceTSum/count;
						
						if(startTime[k] == 1) {
							startTime[k] = clockTime;
						}
						
						//System.out.println("process added to after a process contributes: " +  queue);	
					}		
				}
				
				
				//checking service time aren't all 0
				for(int i = 0; i < serviceT.length-1; i++) {                                        
					if(serviceT[i] == 0) {
						flag2 = true;
					}
					else {
						flag2 = false;
					}
					
				}
				
				
				//2	
				while(queue.size() == 0 && flag2 == false) {
					clockTime = clockTime + quantum;
					//3
					for (int k = 0; k < arrivalT.length; k++) {
						if (arrivalT[k] <= clockTime && queue.contains(k) != true && serviceT[k] != 0) {
							queue.add(id[k]);    
							//modified dynamic quantum
							count = count + 1;
							serviceTSum = serviceTSum + serviceT[k];
							quantum = serviceTSum/count;
							
							if(startTime[k] == 1) {
								startTime[k] = clockTime;
							}
							
							//System.out.println("process added to after a process contributes: " +  queue);	
						}		
					}
				}
				
			
			}
			
			
			//increase queue by 1. Then add until queue is empty. increase by quantum until an arrival time can be reached and start over
			
			
			
			
			//Calculating wanted values and filling arrays
			
			
			for (int t = 0; t < id.length; t++) {
				turnaroundTime[t] = endTime[t] - arrivalT[t];                    //turn around time
			}
			
			for (int a = 0; a < id.length; a++) {                                //inter-arrival time
				if (a == 0) {
					interarrivalTime[a] = startTime[a];
				}
				else {
					interarrivalTime[a] = startTime[a] - startTime[a-1];
				}			
			}
			
			for (int b = 0; b < id.length; b++) {
				initialwaitTime[b] = startTime[b] - arrivalT[b];                 //initial wait time
			}
		//turnaround time - service time
			for (int w = 0; w < id.length; w++) {
				totalwaitTime[w] = turnaroundTime[w] - serviceTime[w];          //total wait time
			}
			
			for (int c = 0; c < id.length; c++) {
				turnaroundtimeAvg = turnaroundtimeAvg + turnaroundTime[c];                           //Average turn around time
			}		
			turnaroundtimeAvg = turnaroundtimeAvg / id.length;

			
			int maxendTime = 0;                                                     //Average service time
			for (int m = 0; m < endTime.length; m++) {
				if(endTime[m] > maxendTime) {
					maxendTime = endTime[m];
				}
			}
			
			for (int c = 0; c < id.length; c++) {                         //calculating average total wait time
				totalwaitTimeAvg = totalwaitTimeAvg + totalwaitTime[c];                       
			}		
			totalwaitTimeAvg = totalwaitTimeAvg / id.length;
			
			for (int c = 0; c < id.length; c++) {                         //calculating average total wait time
				servicetimeAvg = servicetimeAvg + serviceTime[c];                       
			}		
			servicetimeAvg = servicetimeAvg / id.length;
			
			
			//servicetimeAvg = (1 / ((double)id.length / (double)maxendTime));
			
			for(int d = 0; d < id.length; d++) {
				id[d] = d + 1;
			}
			
			System.out.println("ID\tArrival Time\tService Time\tInitial Wait Time\tTotal Wait Time\t\tEnd Time\tTurnaround Time");
		
			for (int f = 0; f < id.length; f++) {
				System.out.println(id[f] + "\t\t" + arrivalT[f] +  "\t\t" + serviceTime[f] + "\t\t" + initialwaitTime[f] + "\t\t\t" + totalwaitTime[f] + "\t\t\t" + endTime[f] + "\t\t" + turnaroundTime[f]);
			}
			
			System.out.println("Average turnaround Time is: " + turnaroundtimeAvg);
			System.out.println("Average total wait time is: " + totalwaitTimeAvg);
			System.out.println("Average service time is: " + servicetimeAvg);
		}	
		
		public static int[] randArrivalTimes() {
			int[] arrivalTimes = new int[5];              //creates an array for arrival times of size 100
			int[] interArrivalTimes = new int[5];         //creates an array for interarrival times of size 100
			arrivalTimes[0] = 0;                            //1st arrival time will be 0
			//int count = 0;
			//int count2 = 0;
			
		   // System.out.println("These are the interarrival Times: ");

			for (int i = 0; i < interArrivalTimes.length-1; i++) {                   //there are 100 arrival times so there are 99 interarrival times between them
				interArrivalTimes[i] = (int) (Math.random() * (9 - 4)) + 4;		     //adds a number between 4 and 8 inclusively 
				                                                                     //to each index of interarrival times array
				//count = count + 1;
				//System.out.println("The count: " + count + " and the interarrival time is: " + interArrivalTimes[i] + ", ");
			}
			//System.out.println();
			
		     //System.out.println("These are the arrival Times: ");;
			for (int j = 1; j < arrivalTimes.length; j++) {                         //iterate through the arrival time array starting 
				//count2 = count2 + 1;                                                                    //at index 1 since we know index 0 already
				arrivalTimes[j] = arrivalTimes[j-1] + interArrivalTimes[j-1];		//adds the previous arrival time + interarrival time between 
				                                                                    //the previous and current arrival time to the arrival time array	  						
			//System.out.println("The count: " + count2 + " and the arrival time is: " + arrivalTimes[j] + ", ");
			}
			//System.out.println();
			
			return arrivalTimes;                                                    //return arrival times array
		}

		public static int[] randServiceTimes() {
			int[] serviceTimes = new int[5];                                      //creates an array for service times of size 100

			//System.out.println("These are the Service Times: ");
			for (int i = 0; i < serviceTimes.length; i++) {                         //iterates through the service time array starting at index 0
				
				serviceTimes[i] = (int) (Math.random() * (100 - 10)) + 10;		        //adds a number between 2 and 5 inclusively to each index of service times array 
			//System.out.println("The count: " + i + " and the service time is: " + serviceTimes[i] + ", ");
			}
			System.out.println();
			return serviceTimes;                                                   //returns service time array
		}

		public static int[] id() {
			int[] id = new int[5];                                               //creates an array for id of size 100
			//System.out.print("These are the process IDs: ");;                   
			for (int i = 0; i < id.length; i++) {                                  //iterates through the service time array starting at index 0            
				id[i] = i;		    
			//System.out.print(id[i] + ", ");
			}
			System.out.println();
			return id;                                                            //returns id array
		}


	public static void main(String[] args) {		 
		    
		   //case1 
		int id[] =          { 0, 1,   2,  3, 4,5};            // name of each process            
		int arrivalTime1[] = { 0,4,9,14,20,27};           // arrival time for every process           
		int serviceTime1[] = { 18, 70, 74, 80, 26,60 };          // service time for every process   
	//	
//		case 2
          
		int arrivalTime2[] = { 0, 6, 12, 16,22,29 };           // arrival time for every process           
		int serviceTime2[] = { 55, 19, 30, 87,66,48 };          // service time for every process  
	//	
//		case 3
           
		int arrivalTime3[] = { 0, 6, 13, 21,25,31 };           // arrival time for every process           
		int serviceTime3[] = { 31, 48, 20, 59,88,39 };          // service time for every process  
	//	
//		case 4
         
		int arrivalTime4[] = { 0, 4, 8,14,20,24 };           // arrival time for every process           
		int serviceTime4[] = { 80, 29, 54, 76,30,42 };          // service time for every process  
	//	
		
		//int quantum = 2;                                    // quantum time of each process       

		//randServiceTimes();
		//roundRobin(id, arrivalTime, serviceTime, quantum);
		roundRobin(id, arrivalTime4, serviceTime4);
		//roundRobin(id(), randArrivalTimes(), randServiceTimes());   // call the function for output 

		

	}
	

}
