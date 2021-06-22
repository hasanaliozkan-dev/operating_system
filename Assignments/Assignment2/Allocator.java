import java.io.*;

import java.util.*;


public class Allocator {
    public static String input_file_name; // it is read from the command line
    public static final String output_file_name = "output.txt";
    public static LinkedList<Integer> memory_part_size = new LinkedList<>(); // all memory partition
    public static LinkedList<Integer> process_size = new LinkedList<>(); // all proccesses size
    public static LinkedList<String> asterisk = new LinkedList<>(); // asterisks for allocated memory
    public static LinkedList<Boolean> is_set = new LinkedList<>(); // boolean value for whether a process is allocated or not
    public static File input_file; //input file object
    public static File output_file;//output file object


    public static void main(String[] args) throws IOException {
        //checking number of argument
        if (args.length == 1) {
            input_file_name = args[0];
            input_file = new File(input_file_name);
        } else {

            System.out.println("Please enter valid number of argument.");
        }
        output_file = new File(output_file_name);//output file object
        FileWriter fileWriter = new FileWriter(output_file);//file writer object all methods write with this object.
        first_fit_algorithm(fileWriter);
        best_fit_algorithm(fileWriter);
        worst_fit_algorithm(fileWriter);
        fileWriter.close();
    }
    /**
     * This function allocate memory for proccesses by using first-fit algorithm.<br>
     * First Fit Algorithm : It is the simplest technique of allocating the memory block to the processes amongst all.
     * In this algorithm, the pointer keeps track of all the free blocks in the memory
     * accepts the request of allocating a memory block to the coming process.
     *
     * @param fileWriter to write the file.
     * @throws IOException
     */
    private static void first_fit_algorithm(FileWriter fileWriter) throws IOException {

        reader();//read the file

        fileWriter.write("\nFirst-Fit Memory Allocation \n\n" +
                "-----------------------------------------------------------------------------------------------\n\n\n");
        fileWriter.write("start  => ");
        for (Integer item : memory_part_size) {
            fileWriter.write(item + " ");
        }
        fileWriter.write("\n\n");
        for (int i = 0; i < process_size.size(); i++) {
            for (int j = 0; j < memory_part_size.size(); j++) {

                /*If memory part size and process size are equal just add a asterisk for that partition
                 and set this process is_set value true.*/
                if (process_size.get(i).equals(memory_part_size.get(j)) && !(asterisk.get(j).equals("*"))) {
                    is_set.set(i, true);
                    asterisk.set(j, "*");
                    break;
                }

                //If memory part size is greater than process size
                else if (process_size.get(i) < memory_part_size.get(j) && !(asterisk.get(j).equals("*"))) {
                    //change the is_set value to true for current process.
                    is_set.set(i, true);
                    //change the asterisk value to * for that memory part size.
                    asterisk.set(j, "*");
                    //new hole after memory allocation process.
                    int new_hole = memory_part_size.get(j) - process_size.get(i);
                    //set process size to allocated memory location.
                    memory_part_size.set(j, process_size.get(i));
                    //add new hole after the size of process.
                    memory_part_size.add(j + 1, new_hole);
                    //add an asterisk value for new hole
                    asterisk.add(j + 1, "");
                    break;
                }
            }
            //if process is set write the all memory partition.
            fileWriter.write(process_size.get(i) + "   => ");
            if (is_set.get(i)) {

                int k = 0;
                for (Integer item : memory_part_size) {
                    if (item != 0) {
                        if (k == memory_part_size.size() - 1) {
                            fileWriter.write(item + asterisk.get(k) + "\n");
                        } else {
                            fileWriter.write(item + asterisk.get(k) + " ");
                        }
                        k++;
                    }
                }
            }
            //otherwise just write a message.
            else
                fileWriter.write("not allocated, must wait\n");

            fileWriter.write("\n");
        }

    }

    /**
     * This function allocate memory for proccesses by using best-fit algorithm.<br>
     * Best Fit Algorithm : It allocates the process to a partition which is the smallest sufficient partition among the free available partitions.
     *
     * @param fileWriter to write the file.
     * @throws IOException
     */
    private static void best_fit_algorithm(FileWriter fileWriter) throws IOException {

        reader();//read the file
        fileWriter.write("\n\nBest-Fit Memory Allocation \n\n" +
                "-----------------------------------------------------------------------------------------------\n\n\n");
        fileWriter.write("start  => ");
        for (Integer item : memory_part_size) {
            fileWriter.write(item + " ");
        }
        fileWriter.write("\n\n");

        //key -> real index of memory part. value -> memory's part size.
        Map<Integer, Integer> sorted_memory_sizes = new LinkedHashMap<>();

        //Sort them and set them into sorted_memory_sizes map.
        sorted_memory_sizes = set_mem_part(sorted_memory_sizes, 'a');

        //real index of sorted memory parts
        LinkedList<Integer> sorted_keys = new LinkedList<>(sorted_memory_sizes.keySet());

        for (int i = 0; i < process_size.size(); i++) {


            for (int j = 0; j < memory_part_size.size(); j++) {
                int index = sorted_keys.get(j); // the real index of memory part.

                /*If memory part size and process size are equal just add a asterisk for that partition
                 and set this process is_set value true.*/
                if (process_size.get(i).equals(sorted_memory_sizes.get(index)) && !(asterisk.get(index).equals("*"))) {
                    //change is_set value to true for current process.
                    is_set.remove(i);
                    is_set.add(i, true);
                    //change the asterisk value to * for that memory part size.
                    asterisk.remove(index);
                    asterisk.add(index, "*");
                    break;
                }
                //If memory part size is greater than process size.
                else if (process_size.get(i) < sorted_memory_sizes.get(index) && !(asterisk.get(index).equals("*"))) {
                    //change is_set value to true for current process.
                    is_set.remove(i);
                    is_set.add(i, true);
                    //new hole after memory allocation for current process
                    int new_hole = memory_part_size.get(index) - process_size.get(i);
                    //set process size to allocated memory location.
                    memory_part_size.remove(index);
                    memory_part_size.add(index, process_size.get(i));
                    //change the asterisk value to * for that memory part size.
                    asterisk.remove(index);
                    asterisk.add(index, "*");
                    //add new hole after the size of process.
                    memory_part_size.add(index + 1, new_hole);
                    //add an asterisk value for new hole
                    asterisk.add(index + 1, "");
                    break;
                }
            }
            sorted_memory_sizes = set_mem_part(sorted_memory_sizes, 'a');
            sorted_keys = new LinkedList<>(sorted_memory_sizes.keySet());
            //if process is set write the all memory partition.
            fileWriter.write(process_size.get(i) + "   => ");
            if (is_set.get(i)) {
                int k = 0;
                for (Integer item : memory_part_size) {
                    if (item != 0) {
                        if (k == memory_part_size.size() - 1) {
                            fileWriter.write(item + asterisk.get(k) + "\n");
                        } else {
                            fileWriter.write(item + asterisk.get(k) + " ");
                        }
                        k++;
                    }
                }

            }
            //otherwise write a message
            else
                fileWriter.write( "not allocated, must wait\n");

            fileWriter.write("\n");
        }
    }


    /**
     * This function allocate memory for proccesses by using worst-fit algorithm.<br>
     * Worst Fit Algorithm : It allocates a process to the partition
     * which is largest sufficient among the freely available partitions available in the main memory.
     * If a large process comes at a later stage, then memory will not have space to accommodate it
     *
     * @param fileWriter to write the file.
     * @throws IOException
     */
    private static void worst_fit_algorithm(FileWriter fileWriter) throws IOException {
        reader();//read the file

        //key -> real index of memory part. value -> memory's part sizes.
        Map<Integer, Integer> sorted_memory_sizes = new LinkedHashMap<>();

        //Sort them and set them into sorted_memory_sizes map.
        sorted_memory_sizes = set_mem_part(sorted_memory_sizes, 'd');

        //real indexes of sorted memory parts
        LinkedList<Integer> sorted_keys = new LinkedList<>(sorted_memory_sizes.keySet());
        fileWriter.write("\n\nWorst-Fit Memory Allocation \n\n" +
                "-----------------------------------------------------------------------------------------------\n\n\n");
        fileWriter.write("start  => ");
        for (Integer item : memory_part_size) {
            fileWriter.write(item + " ");
        }
        fileWriter.write("\n\n");



        for (int i = 0; i < process_size.size(); i++) {


            for (int j = 0; j < memory_part_size.size(); j++) {
                int index = sorted_keys.get(j);//the real index of memory part.

                /*If memory part size and process size are equal just add a asterisk for that partition
                 and set this process is_set value true.*/
                if (process_size.get(i).equals(sorted_memory_sizes.get(index)) && !(asterisk.get(index).equals("*"))) {
                    //change is_set value to true for current process.
                    is_set.remove(i);
                    is_set.add(i, true);
                    //change the asterisk value to * for that memory part size.
                    asterisk.remove(index);
                    asterisk.add(index, "*");
                    break;
                }
                //If memory part size is greater than process size.
                else if (process_size.get(i) < sorted_memory_sizes.get(index) && !(asterisk.get(index).equals("*"))) {
                    //change is_set value to true for current process.
                    is_set.remove(i);
                    is_set.add(i, true);
                    //new hole after memory allocation for current process
                    int new_hole = memory_part_size.get(index) - process_size.get(i);
                    //set process size to allocated memory location.
                    memory_part_size.remove(index);
                    memory_part_size.add(index, process_size.get(i));
                    //change the asterisk value to * for that memory part size.
                    asterisk.remove(index);
                    asterisk.add(index, "*");
                    //add new hole after the size of process.
                    memory_part_size.add(index + 1, new_hole);
                    //add an asterisk value for new hole
                    asterisk.add(index + 1, "");
                    break;
                }
            }
            sorted_memory_sizes = set_mem_part(sorted_memory_sizes, 'd');
            sorted_keys = new LinkedList<>(sorted_memory_sizes.keySet());

            //if process is set print all memory sizes
            fileWriter.write(process_size.get(i) + "   => ");
            if (is_set.get(i)) {
                int k = 0;
                for (Integer item : memory_part_size) {
                    if (item != 0) {
                        if (k == memory_part_size.size() - 1) {
                            fileWriter.write(item + asterisk.get(k) + "\n");
                        } else {
                            fileWriter.write(item + asterisk.get(k) + " ");
                        }

                        k++;
                    }

                }

            }
            //otherwise write a message
            else
                fileWriter.write("not allocated, must wait\n");

            fileWriter.write("\n");
        }
    }

    /**
     * This method read input file and split all data to store them.
     *
     * @throws FileNotFoundException
     */

    private static void reader() throws FileNotFoundException {
        //Just to be sure all collections are clear for every methods.
        memory_part_size.clear();
        process_size.clear();
        asterisk.clear();
        is_set.clear();

        Scanner file_reader = new Scanner(input_file);//file reader object.

        LinkedList<String> lines = new LinkedList<>();//all lines of file

        //read file and store lines into lines linked-list.
        while (file_reader.hasNext()) {
            String line = file_reader.nextLine();
            if (!line.equals(""))//skip the empty lines
                lines.add(line);
        }

        String[] memory_part_array = lines.get(0).strip().split(",");
        String[] size_array = lines.get(1).strip().split(",");
        for (String mem : memory_part_array) {
            memory_part_size.add(Integer.parseInt(mem));
            asterisk.add("");//default value
        }


        for (String size : size_array) {
            process_size.add(Integer.parseInt(size));
            is_set.add(false);//default value
        }

        file_reader.close();
    }

    /**
     * This method sorts the map by its values
     *
     * @param map   to be sorted.
     * @param order char value  determine sort order ascending or descending.
     * @return sorted map.
     */
    private static Map<Integer, Integer> sort(Map<Integer, Integer> map, char order) {
        Set<Map.Entry<Integer, Integer>> entry_set = map.entrySet();

        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(entry_set);
        list.sort(new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return order == 'a' ? (o1.getValue().compareTo(o2.getValue())) : -(o1.getValue().compareTo(o2.getValue()));
            }
        });

        Map<Integer, Integer> sorted_map = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> integerEntry : list) {
            sorted_map.put(integerEntry.getKey(), integerEntry.getValue());
        }
        return sorted_map;
    }

    /**
     * This method set memory partition again for sorted memory sizes collection to help best and worst fit algorithms.
     * I wrote this method just to prevent repetition.
     *
     * @param sorted_memory_sizes map which include one step before sorted memory sizes.
     * @param order               char value  determine sort order ascending or descending.
     * @return sorted memory sizes map.
     */
    public static Map<Integer, Integer> set_mem_part(Map<Integer, Integer> sorted_memory_sizes, char order) {
        for (int i = 0; i < memory_part_size.size(); i++) {
            sorted_memory_sizes.put(i, memory_part_size.get(i));
        }
        sorted_memory_sizes = sort(sorted_memory_sizes, order);
        return sorted_memory_sizes;
    }

}
