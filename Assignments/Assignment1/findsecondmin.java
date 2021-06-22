public class findsecondmin {

    public static void main(String[] args) {
        int[] arr = new int[]{2,2,2,1,0};
        System.out.println(secmin(arr));


    }
    public static int secmin (int[] arr){
        int max =0;
        for (int i = 0; i <arr.length ; i++) {
            if (arr[i]>max)
                max = arr[i];
        }
        int first = arr[0];
        boolean flag = true;
        for (int i = 0; i <arr.length ; i++) {
            if (first!=arr[i]){
                flag =false;
                break;
            }
        }
        if (flag){
            System.out.println("Alll of them equal");
            return -1;
        }
        int min = max;
        int index = 0;
        for (int i = 0; i <arr.length ; i++) {
            if (arr[i]<min) {
                min = arr[i];
                index = i;
            }
        }
        arr[index] = max;
        min = max;
        first = arr[0];
        flag = true;
        for (int i = 0; i <arr.length ; i++) {
            if (first!=arr[i]){
                flag =false;
                break;
            }
        }

        if (flag)
            return arr[0];
        else{
            for (int i = 0; i <arr.length ; i++) {
                if (arr[i]<min) {
                    min = arr[i];
                    index = i;
                }
            }
            return min;
        }




    }
}
