import java.util.*;
public class SchedularAlgo {

public static void main(String[] args){
int[] myarr = {6,8,7,3};
System.out.println("Before scheduling average waiting time is " + waiting_time(myarr));
printGanttChart(myarr);
sjf_scheduler(myarr);
System.out.println("After scheduling average waiting time is " + waiting_time(myarr));
printGanttChart(myarr);

Map<Integer,Integer> jobs = new HashMap<>();
jobs.put(0,8);jobs.put(1,4);jobs.put(2,9);jobs.put(3,5);
    for (int i = 0; i < jobs.size(); i++) {
        System.out.println(jobs.get(i));
    }

}
public static void printGanttChart(int[] arr){
    int totaltime = 0;
    System.out.print(totaltime);
    for(int i = 0;i<arr.length;i++){
         for(int j = 0;j<arr[i];j++){
            System.out.print("   ");
        }
        totaltime += arr[i];
       System.out.print(totaltime);

    }
    System.out.println();
    for(int i = 0;i<arr.length;i++){
        for(int j = 0;j<arr[i];j++){
            System.out.print("---");
        }
    }
    System.out.println();
    for(int i = 0;i<arr.length;i++){
        System.out.print(" | ");
        for(int j = 0;j<arr[i]/2;j++){
            System.out.print("   ");
        }
        System.out.print(arr[i]);
        for(int j = 0;j<arr[i]/2;j++){
            System.out.print("  ");
        } 
    }
    System.out.print(" |");
    System.out.println();


   for(int i = 0;i<arr.length;i++){
        for(int j = 0;j<arr[i];j++){
            System.out.print("---");
        }
    }
    System.out.println();
}

public static double waiting_time(int arr[]){
    double waiting_time = 0; 
    for(int i = 1;i<arr.length;i++){
        for(int j = 0;j<i;j++){
            waiting_time = waiting_time+ arr[j];
        }
    }
    return waiting_time/arr.length;
}
public static int[] sjf_scheduler(int[] arr){
     Arrays.sort(arr);
     return arr;
}



}
