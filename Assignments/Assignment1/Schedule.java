
import java.io.*;
import java.util.*;
import java.util.List;

public class Schedule {
    //Input and output file paths variables.
    public static String file_name_input;
    public static final String file_name_output = "output.txt";
    //The number of task-line in the file.
    public static int line;
    //Input and output file objects.
    public static File input_file;
    public static File output_file;

    //Tasks are in the map which keys are String and values are ArrayList.
    public static Map<String, ArrayList<Integer>> tasks;

    public static void main(String[] args) throws IOException {
        /*
        Reading arguments from the command line which has to be 2 in this case
        */
        String command = "";
        if (args.length == 2) {
            command = args[0];
            file_name_input = args[1];
        } else
            System.out.println("Please enter valid number of arguments which is two in this order(algorithm's name,file's name)");

        /*
        Parse and specify the command and call the suitable method.
        Otherwise print a message to the user if the algorithm's name is not valid.
        */
        switch (command) {
            case "fcfs":
                fcfs();
                break;
            case "sjf":
                sjf();
                break;

            case "pri":
                pri();
                break;

            case "rr":
                rr();
                break;

            case "pri-rr":
                pri_rr();
                break;
            default:
                if (args.length == 2)
                    System.out.println("Please enter a valid command from here[fcfs,sjf,pri,rr,pri-rr]");
                break;
        }


    }


    /**
     * Schedule the task on their arrival time.
     *
     * @throws IOException
     */
    public static void fcfs() throws IOException {
        double waiting_time;
        double turn_around_time;
        output_file = new File(file_name_output);
        FileWriter fileWriter = new FileWriter(output_file);//File writer object.
        line = 0;//Just to be sure we are beginning of the file.
        reader();
        tasks = sortByColumn(tasks, 0, 'a');
        //Write the expected output to the file.
        fileWriter.write("First Come First Serve Scheduling\n\n");
        for (String taskName : tasks.keySet()) {
            fileWriter.write("Will run Name: " + taskName + "\n");
            fileWriter.write("Priority: " + tasks.get(taskName).get(1) + "\n");
            fileWriter.write("Burst Time: " + tasks.get(taskName).get(2) + "\n");
            fileWriter.write("Task " + taskName + " Finished\n\n");
        }
        ArrayList<ArrayList<Integer>> task_for_time_calc = new ArrayList<ArrayList<Integer>>();
        for (String taskName : tasks.keySet()) {
                task_for_time_calc.add(tasks.get(taskName));

        }
        waiting_time = (double)task_for_time_calc.get(0).get(0);

        for (int i = 0; i < line; i++) {
            waiting_time+=(double) task_for_time_calc.get(0).get(0);
            for (int j = 0; j <i ; j++) {
                waiting_time+= (double)task_for_time_calc.get(j).get(2);

            }
            waiting_time-=(double)task_for_time_calc.get(i).get(0);

        }
        turn_around_time=task_for_time_calc.get(0).get(0);
        for (int i = 0; i < line; i++) {
            turn_around_time+= (double)task_for_time_calc.get(i).get(1);
            for (int j = 0; j <=i ; j++) {
                turn_around_time+= (double)task_for_time_calc.get(j).get(2);

            }
            turn_around_time-=(double)task_for_time_calc.get(i).get(0);

        }




        fileWriter.write("Average waiting time : "+waiting_time/line +" ms.\n\n");
        fileWriter.write("Average response time : "+waiting_time/line +" ms.\n\n"); //Since the algorithm is non-preemptive.
        fileWriter.write("Average turn around time : "+turn_around_time/line +" ms.\n\n");
        fileWriter.close();

    }

    /**
     * Schedule the task on their burst time.
     *
     * @throws IOException
     */
    public static void sjf() throws IOException {
        int workstime = 0;
        double waiting_time,turn_around_time;
        output_file = new File(file_name_output);
        FileWriter fileWriter = new FileWriter(output_file);
        fileWriter.write("Shortest Job First Scheduling\n\n");
        line = 0;
        reader();
        Map<String, ArrayList<Integer>> firstTask = firstTaskFinder(tasks);
        Map<String, ArrayList<Integer>> newbies = new LinkedHashMap<>();
        newbies.put("temp", new ArrayList<>(Arrays.asList(0, 0, 0)));
        String firstTaskName="";
        for (String name :firstTask.keySet()) {
            firstTaskName=name;
            fileWriter.write("Will run Name: " + firstTaskName + "\n");
            fileWriter.write("Priority: " + firstTask.get(firstTaskName).get(1) + "\n");
            fileWriter.write("Burst Time: " + firstTask.get(firstTaskName).get(2) + "\n");
            fileWriter.write("Task " + firstTaskName + " Finished\n\n");
        }
        Map<String,ArrayList<Integer>> copyoftask = new LinkedHashMap<>();
        for (String name: tasks.keySet()) {
            copyoftask.put(name,tasks.get(name));
        }
        Map<String, ArrayList<Integer>> currentTask = firstTask;
        workstime = firstTask.get(firstTaskName).get(2);
        tasks.remove(firstTaskName);
        int writetimes = 0;
        int tasksizes = tasks.size();
        while (writetimes<tasksizes) {
            boolean flag = true;
            Stack<String> keyListStack1  = new Stack<>();
            Stack<String> keyListStack2  = new Stack<>();
            List<String> keyList = new ArrayList<>(tasks.keySet());
            for (int i = 0; i <keyList.size() ; i++) {
                keyListStack1.push(keyList.get(i));
            }
            String currentTaskName = "";
            while (!keyListStack1.isEmpty()&&flag){
                String key;

                key = keyListStack1.pop();
                keyListStack2.push(key);
                currentTaskName = currentTask.keySet().toString().replace("[", "").replace("]", "");

                if (tasks.get(key).get(0) <= workstime) {
                    if(key.equals(firstTaskName)){
                        //do nothing just ignore the first task we did it before.
                    }
                    else{
                        newbies.put(key, tasks.get(key));
                        newbies.remove("temp");
                        tasks.remove(key);
                        keyListStack2.pop();
                    }
                }else{
                    tasks = sortByColumn(tasks,0,'a');
                    for (String name :tasks.keySet()) {
                        newbies.put(name,tasks.get(name));
                        tasks.remove(name);
                        keyListStack1.remove(name);
                        newbies.remove("temp");
                        flag = false;
                        break;
                    }

                }
            }

            keyListStack1 = keyListStack2;

            currentTask.remove(currentTaskName);

            newbies = sortByColumn(newbies, 2, 'a');

            if (tasks.isEmpty()) {
                int index = 0;
                List<String> keyl = new ArrayList<>(newbies.keySet());
                while(!newbies.isEmpty()){
                    String key = keyl.get(index);
                    fileWriter.write("Will run Name: " + key + "\n");
                    fileWriter.write("Priority: " + newbies.get(key).get(1) + "\n");
                    fileWriter.write("Burst Time: " + newbies.get(key).get(2) + "\n");
                    fileWriter.write("Task " + key + " Finished\n\n");
                    newbies.remove(key);
                    index++;
                    writetimes++;
                }

            } else {

                for (String name : newbies.keySet()) {
                    fileWriter.write("Will run Name: " + name + "\n");
                    fileWriter.write("Priority: " + newbies.get(name).get(1) + "\n");
                    fileWriter.write("Burst Time: " + newbies.get(name).get(2) + "\n");
                    fileWriter.write("Task " + name + " Finished\n\n");

                    currentTask.put(name, newbies.get(name));
                    newbies.remove(name);
                    workstime += currentTask.get(name).get(2);
                    writetimes++;
                    break;
                }
            }


        }

        copyoftask = sortByColumn(copyoftask,2,'a');
        ArrayList<ArrayList<Integer>> task_for_time_calc = new ArrayList<ArrayList<Integer>>();
        for (String taskName : copyoftask.keySet()) {
            task_for_time_calc.add(copyoftask.get(taskName));

        }
        waiting_time = (double)task_for_time_calc.get(0).get(0);

        for (int i = 0; i < line; i++) {
            waiting_time+=(double) task_for_time_calc.get(0).get(0);
            for (int j = 0; j <i ; j++) {
                waiting_time+= (double)task_for_time_calc.get(j).get(2);

            }
            waiting_time-=(double)task_for_time_calc.get(i).get(0);

        }
        turn_around_time=task_for_time_calc.get(0).get(0);
        for (int i = 0; i < line; i++) {
            turn_around_time+= (double)task_for_time_calc.get(i).get(1);
            for (int j = 0; j <=i ; j++) {
                turn_around_time+= (double)task_for_time_calc.get(j).get(2);

            }
            turn_around_time-=(double)task_for_time_calc.get(i).get(0);

        }

        fileWriter.write("Average waiting time : "+waiting_time/line +" ms.\n\n");
        fileWriter.write("Average response time : "+waiting_time/line +" ms.\n\n"); //Since the algorithm is non-preemptive.
        fileWriter.write("Average turn around time : "+turn_around_time/line +" ms.\n\n");
        fileWriter.close();

    }
    /**
     * Schedule the task on their prirorities.
     * Greater number has higher priority.
     *
     * @throws IOException
     */
    public static void pri() throws IOException {
        int workstime = 0;
        double waiting_time,turn_around_time;
        output_file = new File(file_name_output);
        FileWriter fileWriter = new FileWriter(output_file);
        fileWriter.write("Priority Scheduling\n\n");
        line = 0;
        reader();
        Map<String, ArrayList<Integer>> firstTask = firstTaskFinder(tasks);
        Map<String, ArrayList<Integer>> newbies = new LinkedHashMap<>();
        newbies.put("temp", new ArrayList<>(Arrays.asList(0, 0, 0)));
        String firstTaskName = "";
        for (String name : firstTask.keySet()) {
            firstTaskName = name;
            fileWriter.write("Will run Name: " + firstTaskName + "\n");
            fileWriter.write("Priority: " + firstTask.get(firstTaskName).get(1) + "\n");
            fileWriter.write("Burst Time: " + firstTask.get(firstTaskName).get(2) + "\n");
            fileWriter.write("Task " + firstTaskName + " Finished\n\n");
        }
        Map<String,ArrayList<Integer>> copyoftask = new LinkedHashMap<>();
        for (String name: tasks.keySet()) {
            copyoftask.put(name,tasks.get(name));
        }

        Map<String, ArrayList<Integer>> currentTask = firstTask;
        workstime = firstTask.get(firstTaskName).get(2);
        tasks.remove(firstTaskName);
        int writetimes = 0;
        int tasksizes = tasks.size();
        while (writetimes<tasksizes) {
            boolean flag = true;

            Stack<String> keyListStack1 = new Stack<>();
            Stack<String> keyListStack2 = new Stack<>();
            List<String> keyList = new ArrayList<>(tasks.keySet());
            for (int i = 0; i < keyList.size(); i++) {
                keyListStack1.push(keyList.get(i));
            }
            String currentTaskName = "";
            int i = 0;
            while (!keyListStack1.isEmpty()&&flag) {
                String key;

                key = keyListStack1.pop();
                keyListStack2.push(key);
                currentTaskName = currentTask.keySet().toString().replace("[", "").replace("]", "");

                if (tasks.get(key).get(0) <= workstime) {
                    if (key.equals(firstTaskName)) {
                        //do nothing just ignore the first task we did it before.
                    } else {


                        newbies.put(key, tasks.get(key));
                        newbies.remove("temp");
                        tasks.remove(key);

                        keyListStack2.pop();
                    }
                }else{
                    tasks = sortByColumn(tasks,0,'a');
                    for (String name :tasks.keySet()) {
                        newbies.put(name,tasks.get(name));
                        tasks.remove(name);
                        keyListStack1.remove(name);
                        newbies.remove("temp");
                        flag = false;
                        break;
                    }
                }
            }

            keyListStack1 = keyListStack2;

            currentTask.remove(currentTaskName);

            newbies = sortByColumn(newbies, 1, 'd');

            if (tasks.isEmpty()) {
                int index = 0;
                List<String> keyl = new ArrayList<>(newbies.keySet());

                while (!newbies.isEmpty()) {
                    String key = keyl.get(index);
                    fileWriter.write("Will run Name: " + key + "\n");
                    fileWriter.write("Priority: " + newbies.get(key).get(1) + "\n");
                    fileWriter.write("Burst Time: " + newbies.get(key).get(2) + "\n");
                    fileWriter.write("Task " + key + " Finished\n\n");
                    newbies.remove(key);
                    index++;
                    writetimes++;
                }

            } else {

                for (String name : newbies.keySet()) {
                    fileWriter.write("Will run Name: " + name + "\n");
                    fileWriter.write("Priority: " + newbies.get(name).get(1) + "\n");
                    fileWriter.write("Burst Time: " + newbies.get(name).get(2) + "\n");
                    fileWriter.write("Task " + name + " Finished\n\n");

                    currentTask.put(name, newbies.get(name));
                    newbies.remove(name);
                    workstime += currentTask.get(name).get(2);
                    writetimes++;
                    break;
                }
            }



        }

        copyoftask = sortByColumn(copyoftask,2,'a');
        ArrayList<ArrayList<Integer>> task_for_time_calc = new ArrayList<ArrayList<Integer>>();
        for (String taskName : copyoftask.keySet()) {
            task_for_time_calc.add(copyoftask.get(taskName));

        }
        waiting_time = (double)task_for_time_calc.get(0).get(0);

        for (int i = 0; i < line; i++) {
            waiting_time+=(double) task_for_time_calc.get(0).get(0);
            for (int j = 0; j <i ; j++) {
                waiting_time+= (double)task_for_time_calc.get(j).get(2);

            }
            waiting_time-=(double)task_for_time_calc.get(i).get(0);

        }
        turn_around_time=task_for_time_calc.get(0).get(0);
        for (int i = 0; i < line; i++) {
            turn_around_time+= (double)task_for_time_calc.get(i).get(1);
            for (int j = 0; j <=i ; j++) {
                turn_around_time+= (double)task_for_time_calc.get(j).get(2);

            }
            turn_around_time-=(double)task_for_time_calc.get(i).get(0);

        }

        fileWriter.write("Average waiting time : "+waiting_time/line +" ms.\n\n");
        fileWriter.write("Average response time : "+waiting_time/line +" ms.\n\n"); //Since the algorithm is non-preemptive.
        fileWriter.write("Average turn around time : "+turn_around_time/line +" ms.\n\n");
        fileWriter.close();
    }

    /**
     * Schedule the task on their arrival time. And use time quantum to context switching.
     *
     * @throws IOException
     */
    public static void rr() throws IOException {
        final Integer quantum = 10;

        output_file = new File(file_name_output);
        FileWriter fileWriter = new FileWriter(output_file);
        fileWriter.write("Round-Robin Scheduling\n\n");
        line = 0;
        reader();
        Map<String, ArrayList<Integer>> firstTask = firstTaskFinder(tasks);
        String firstTaskName = "";
        for (String name : firstTask.keySet()) {
            firstTaskName = name;
            Integer firsTaskBurstTime = firstTask.get(firstTaskName).get(2);
            fileWriter.write("Will run Name: " + firstTaskName + "\n");
            fileWriter.write("Priority: " + firstTask.get(firstTaskName).get(1) + "\n");
            fileWriter.write("Burst Time: " + firstTask.get(firstTaskName).get(2) + "\n");
            if (firsTaskBurstTime <= quantum) {
                fileWriter.write("Task " + firstTaskName + " Finished\n\n");
            } else {
                fileWriter.write("Remaining Burst Time: " + (firstTask.get(firstTaskName).get(2)-quantum) + "\n");
                fileWriter.write("Context is switching...\n\n");
                ArrayList<Integer> temp = new ArrayList<>(Arrays.asList(firstTask.get(firstTaskName).get(0), firstTask.get(firstTaskName).get(1),
                        firsTaskBurstTime - quantum));
                tasks.put(firstTaskName, temp);

            }
        }
        tasks = sortByColumn(tasks, 0, 'a');
        Queue<String> taskName = new LinkedList<>(tasks.keySet());
        Map<String, Integer> times = new LinkedHashMap<>();
        for (String key : tasks.keySet()) {
            times.put(key, tasks.get(key).get(2));
        }
        int firstBurst = times.get(firstTaskName);
        times.put(firstTaskName, firstBurst + 10);
        taskName.remove();
        taskName.add(firstTaskName);
        int totalworkstime = quantum;
        int cnt = 0;
        int size = tasks.size();
        while (!tasks.isEmpty()) {
            String currentTaskName = taskName.remove();
            Integer currentTaskBurstTime = tasks.get(currentTaskName).get(2);
            Integer currentArrivalTime = tasks.get(currentTaskName).get(0);
            if (currentArrivalTime <= totalworkstime) {
                cnt = 0;
                fileWriter.write("Will run Name: " + currentTaskName + "\n");
                fileWriter.write("Priority: " + tasks.get(currentTaskName).get(1) + "\n");
                fileWriter.write("Burst Time: " + times.get(currentTaskName) + "\n");
                if (currentTaskBurstTime <= quantum) {
                    //fileWriter.write("Remaining Burst Time: " + tasks.get(currentTaskName).get(2) + "\n");
                    fileWriter.write("Task " + currentTaskName + " Finished\n\n");
                    tasks.remove(currentTaskName);
                } else {
                    fileWriter.write("Remaining Burst Time: " + (tasks.get(currentTaskName).get(2) -quantum)+ "\n");
                    fileWriter.write("...Context is switching...\n\n");
                    ArrayList<Integer> temp = new ArrayList<>(Arrays.asList(tasks.get(currentTaskName).get(0), tasks.get(currentTaskName).get(1),
                            currentTaskBurstTime - quantum));
                    tasks.put(currentTaskName, temp);
                    taskName.add(currentTaskName);
                }
                totalworkstime+= quantum;
            }
            else{
                taskName.add(currentTaskName);
            }
            if(cnt >= size){
                totalworkstime++;
                tasks = sortByColumn(tasks,0,'a');
                taskName = new LinkedList<>(tasks.keySet());
            }
                cnt++;
        }


        fileWriter.close();
    }

    /**
     * Schedule the task on their priorities. And use time quantum to context switching.
     *
     * @throws IOException
     */
    public static void pri_rr() throws IOException {
        int numberofequalpriorities =0,updatednumberofequalpriorities=0;
        Stack<String> equalpriority = new Stack<>();
        final Integer quantum = 10;
        int cnt = 0;
        boolean secflag= false;
        output_file = new File(file_name_output);
        FileWriter fileWriter = new FileWriter(output_file);
        fileWriter.write("Priority Round-Robin Scheduling\n\n");
        line = 0;
        reader();
        Map<String, ArrayList<Integer>> firstTask = firstTaskFinder(tasks);

        Integer firsTaskBurstTime = 0;
        String firstTaskName = "";
        for (String name : firstTask.keySet()) {
            firstTaskName = name;
            firsTaskBurstTime = firstTask.get(firstTaskName).get(2);

            if (firsTaskBurstTime <= quantum) {
                fileWriter.write("Will run Name: " + firstTaskName + "\n");
                fileWriter.write("Priority: " + firstTask.get(firstTaskName).get(1) + "\n");
                fileWriter.write("Burst Time: " + firstTask.get(firstTaskName).get(2) + "\n");
                fileWriter.write("Task " + firstTaskName + " Finished\n\n");
            } else {
                fileWriter.write("Will run Name: " + firstTaskName + "\n");
                fileWriter.write("Priority: " + firstTask.get(firstTaskName).get(1) + "\n");
                fileWriter.write("Burst Time: " + firstTask.get(firstTaskName).get(2) + "\n");
                fileWriter.write("Remaining Burst Time: " + (firstTask.get(firstTaskName).get(2) -quantum)+ "\n");
                fileWriter.write("...Context is switching...\n\n");
                ArrayList<Integer> temp = new ArrayList<>(Arrays.asList(firstTask.get(firstTaskName).get(0), firstTask.get(firstTaskName).get(1),
                        firsTaskBurstTime - quantum));
                tasks.put(firstTaskName, temp);

            }
        }
        Map<String, ArrayList<Integer>> newbies = new LinkedHashMap<>();
        Map<String, ArrayList<Integer>> copyoftasks = new LinkedHashMap<>();
        for(String name : tasks.keySet()){
            copyoftasks.put(name,tasks.get(name));
        }

        Queue<String> taskName = new LinkedList<>(tasks.keySet());


        Map<String, Integer> times = new LinkedHashMap<>();
        Map<String, Integer> updatedTimes = new LinkedHashMap<>();
        for (String key : tasks.keySet()) {
            times.put(key, tasks.get(key).get(2));
            updatedTimes.put(key,tasks.get(key).get(2));
        }

        times.put(firstTaskName, firsTaskBurstTime);
        updatedTimes.put(firstTaskName, firsTaskBurstTime);
        taskName.remove();
        taskName.add(firstTaskName);
        newbies.put("temp", new ArrayList<>(Arrays.asList(0, 0, 0)));
        int workstimes = quantum;
        Queue<String> tempTaskName =new LinkedList<>(tasks.keySet());
        while (!tempTaskName.isEmpty()){
            String currentTaskName = tempTaskName.remove();
            Integer currentPriority = tasks.get(currentTaskName).get(1);
            for (String name :tasks.keySet()) {
                if(tasks.get(name).get(1).equals(currentPriority)&&!(currentTaskName.equals(name))){
                    numberofequalpriorities++;
                    break;
                }

            }
        }
        updatednumberofequalpriorities = numberofequalpriorities;


        int cyc=0;
        while (!copyoftasks.isEmpty()) {
            String currentTaskName;
            int counter = 0;
            int sizeoftaskname = taskName.size();
            while (!taskName.isEmpty() && counter < sizeoftaskname) {
                String name = taskName.remove();
                if (tasks.get(name).get(0) <= workstimes) {
                    newbies.put(name, tasks.get(name));

                    tasks.remove(name);
                    newbies.remove("temp");
                    taskName.remove(name);
                } else {
                    taskName.add(name);

                }
                counter++;
            }
            if(!newbies.isEmpty()){
            newbies = sortByColumn(newbies, 1, 'd');
            Queue<String> newbiesTasks = new LinkedList<>(newbies.keySet());

            currentTaskName = newbiesTasks.remove();

            Integer currentPriority = newbies.get(currentTaskName).get(1);

            if (equalpriority.size()!=0){
                currentTaskName = equalpriority.pop();

            }

            //System.out.println(numberofequalpriorities);
            for(String name : newbies.keySet()){
                if(newbies.get(name).get(1).equals(currentPriority)&&!(currentTaskName.equals(name))){

                    if(cyc%numberofequalpriorities==0){
                            equalpriority.push(currentTaskName);
                            cnt++;

                        break;
                    }else{
                        if(copyoftasks.size()!=4){


                        int temp = cyc%numberofequalpriorities;
                        for(int i = 0;i<temp;i++){
                            newbiesTasks.add(currentTaskName);
                            currentTaskName = newbiesTasks.remove();
                        }
                        cnt++;
                        break;
                        }
                    }


                    /*
                    newbiesTasks.add(currentTaskName);
                    currentTaskName = newbiesTasks.remove();
*/


                }
            }

            fileWriter.write("Will run Name: " + currentTaskName + "\n");
            fileWriter.write("Priority: " + newbies.get(currentTaskName).get(1) + "\n");
            fileWriter.write("Burst Time: " + times.get(currentTaskName) + "\n");

            boolean flag = true;
            for(String name : newbies.keySet()) {

                if (newbies.get(name).get(1).equals(currentPriority) && !(currentTaskName.equals(name))){
                    flag = false;
                    break;
                }

            }


            if (newbies.get(currentTaskName).get(2) <=quantum || flag) {
                fileWriter.write("Task " + currentTaskName + " Finished\n\n");
                newbies.remove(currentTaskName);
                copyoftasks.remove(currentTaskName);


            } else {

                fileWriter.write("Remaining Burst Time: " + (newbies.get(currentTaskName).get(2) -quantum)+ "\n");
                fileWriter.write("...Context is switching...\n\n");
                ArrayList<Integer> temp = new ArrayList<>(Arrays.asList(newbies.get(currentTaskName).get(0), newbies.get(currentTaskName).get(1),
                        newbies.get(currentTaskName).get(2) - quantum));
                newbies.put(currentTaskName, temp);
                newbiesTasks.add(currentTaskName);
                updatedTimes.put(currentTaskName,updatedTimes.get(currentTaskName)-quantum);

                cyc++;



            }

                workstimes+=quantum;

            }else {
                workstimes++;
            }


        }
        fileWriter.close();
    }

    /**
     * Sorts map based matrix by their selected column.
     * @param map          matrix based map.
     * @param columnNumber selected column number from the Arraylist.
     * @param order        sort ascending or descending order.('a','d').
     * @return sorted map.
     */
    public static Map<String, ArrayList<Integer>> sortByColumn(Map<String, ArrayList<Integer>> map, int columnNumber, char order) {
        List<Map.Entry<String, ArrayList<Integer>>> task = new LinkedList<>(map.entrySet());
        //Sort the tasks by column.
        Collections.sort(task, new Comparator<>() {
            public int compare(Map.Entry<String, ArrayList<Integer>> o1, Map.Entry<String, ArrayList<Integer>> o2) {
                return order == 'a' ? -((o2.getValue().get(columnNumber)).compareTo(o1.getValue().get(columnNumber)))
                        : ((o2.getValue().get(columnNumber)).compareTo(o1.getValue().get(columnNumber)));
            }
        });
        Map<String, ArrayList<Integer>> result = new LinkedHashMap<>();
        for (Map.Entry<String, ArrayList<Integer>> entry : task) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * Finds the task that comes first.
     *
     * @param map matrix based map.
     * @return result map.
     */

    public static Map<String, ArrayList<Integer>> firstTaskFinder(Map<String, ArrayList<Integer>> map) {
        Map<String, ArrayList<Integer>> temp = sortByColumn(map, 0, 'a');
        Map<String, ArrayList<Integer>> result = new LinkedHashMap<>();
        for (String name : temp.keySet()) {
            result.put(name, temp.get(name));
            break;
        }

        return result;
    }

    /**
     * Open and read the file line by line than put them into the tasks map.
     *
     * @throws FileNotFoundException
     */
    public static void reader() throws FileNotFoundException {
        input_file = new File(file_name_input);
        Scanner fileReader = new Scanner(input_file);//Reader object.
        while (fileReader.hasNextLine()) {
            String emptyline = fileReader.nextLine();
            if (!emptyline.isEmpty()) {
              line++;
            }

        }//Determine the number of lines in the file.
        fileReader.close();//Close because the cursor at the end of the file.
        fileReader = new Scanner(input_file);
        tasks = new LinkedHashMap<>();
        while (fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            if (line.isEmpty()) {
                continue;
            }
            String[] temp;
            temp = line.split(",");//Split the whole line by ',' then put it into a String array.
            for (int i = 0; i < temp.length; i++) {
                temp[i] = temp[i].replaceAll(",", "").trim();
            }

            String taskName = temp[0];//Task name format is like that 'T1'.
            ArrayList<Integer> values = new ArrayList<>();
            //Take the values(Arrival Time,Priority,Burst Time) cast to int  and add them to the values ArrayList.
            for (int i = 1; i < 4; i++) {
                values.add(Integer.parseInt(temp[i]));
            }
            tasks.put(taskName, values);
        }
        fileReader.close();
    }
}