# Round-Robin-Algorithm-Modification

The goal of this project was to create a round robin scheduler with the ability to input random service and arrival times. This project was then modified from a fixed time quantum to a dynamic quantum in order to decrease the average turnaround time and average wait time.

If the quantum chosen is too large, the response time of the processes is considered too high. On the other hand, if this quantum is too small, it increases the overhead of the CPU. This is why despite round robin being the most widely used scheduling algorithm it has one of the worst turnaround and waiting times. These can be improved by making the operating system adjust the time quantum according to the service times of the existed set of processes in the ready queue. 
