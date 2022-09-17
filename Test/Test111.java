import java.util.Random;

public class Test111 {
    public static void main(String[] args) {
        // 定义一个长度为5的int类型的数组
        int[] arr = new int[5]{}; // 6,int[] arr = new int[5];
        Random r = new Random();
        // 随机生成5个随机数存入数组（随机数的范围为10到50，包括10和50）
        for (int i = 0; i < arr.length; i++) {
            arr[i]= r.nextInt(40)+11; // 10,arr[i]= r.nextInt(41)+10;
        }
        // 调用getNewArr方法，在控制台打印返回后的数组中的元素
        int newArr[] = getNewArr(arr); // 12,int[] newArr = getNewArr(arr);
        System.out.println("修改后的数组元素为：");
        for (int i = 0; i < newArr.length; i++) {
            System.out.println(newArr[i]);
        }

    }
    //定义 geNewArr()静态方法：要求传入一个int类型的数组arr
    private static int[] getNewArr(int[] arr) {
        // 遍历数组
        for (int i = 0; i < arr.length; i++) {
            //筛选出数组中十位数字是2的倍数的元素
            if ((arr[i] % 10) % 2 == 0){  // 25,if ((arr[i] / 10) % 2 == 0){
                // 将十位数字是2的倍数的元素替换为0
                arr[i] = '0'; // 27,arr[i] = 0;
            }
        }
        return arr;
    }
}