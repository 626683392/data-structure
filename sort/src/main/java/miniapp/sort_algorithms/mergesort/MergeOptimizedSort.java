package miniapp.sort_algorithms.mergesort;


import miniapp.abstraction.Constant;

public class MergeOptimizedSort extends MergeSort {

    /**
     * 优化接口实现
     * @param arr
     * @return
     */
    @Override
    public int[] sort(int[] arr) {
        int length = arr.length,l = 0,r = length - 1;
        aux = arr.clone();
        mergesort(arr,aux,l,r);
        return arr;
    }

    private int[] mergesort(int[] arr, int[] aux, int l, int r) {
        // 递归终止条件
        if (l >= r - Constant.INSERTSIZE) {
            return this.insertSort(arr,l,r);
        }
        // 分治调用
        int mid = l + ((r - l) >> 1);
        mergesort(aux,arr,l,mid);
        mergesort(aux,arr,mid+1,r);
        // 局部有序 不进行合并
        if (aux[mid] <= aux[mid + 1]){
            System.arraycopy(aux,l,arr,l,(r - l + 1));
            return arr;
        }
        return fastMerge(arr,aux,l,mid,r);
    }


    public static void main(String[] args) {
        MergeOptimizedSort mergeOptimizedSort = new MergeOptimizedSort();
        long sort = mergeOptimizedSort.testSort(10000000);
        System.out.println("花费时间"+sort+"ms");
        //花费时间388ms 花费时间391ms 花费时间389ms
    }

}