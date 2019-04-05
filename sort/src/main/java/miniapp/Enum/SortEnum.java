package miniapp.Enum;

import miniapp.abstraction.SortMethod;
import miniapp.sort_algorithms.bubblesort.BubbleSort;
import miniapp.sort_algorithms.bucketsort.BucketSort;
import miniapp.sort_algorithms.countingsort.CountingSort;
import miniapp.sort_algorithms.heapsort.HeapSort;
import miniapp.sort_algorithms.insertionsort.InsertionSort;
import miniapp.sort_algorithms.mergesort.MergeBuSort;
import miniapp.sort_algorithms.mergesort.MergeSort;
import miniapp.sort_algorithms.mergesort.MergeOptimizedSort;
import miniapp.sort_algorithms.quicksort.DualPivotQuickSort;
import miniapp.sort_algorithms.quicksort.Quick3waySort;
import miniapp.sort_algorithms.quicksort.QuickSort;
import miniapp.sort_algorithms.selectionsort.SelectionSort;
import miniapp.sort_algorithms.shellsort.ShellSort;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum SortEnum {
    /**
     * 插入排序
     */
    InsertSort("InsertSort"),
    /**
     * 选择排序
     */
    SelectionSort("SelectionSort"),
    /**
     * 冒泡排序
     */
    BubbleSort("BubbleSort"),
    /**
     * 桶排序
     */
    BucketSort("BucketSort"),
    /**
     * 计数排序
     */
    CountingSort("CountingSort"),
    /**
     * 堆排序
     */
    HeapSort("HeapSort"),
    /**
     * 自下而上归并排序
     */
    MergeSort("MergeSort"),
    /**
     * 自下而上归并排序
     */
    MergeBuSort("MergeBuSort"),
    /**
     * 优化归并排序
     */
    MergeOptimizedSort("MergeOptimizedSort"),
    /**
     * 快速排序
     */
    QuickSort("QuickSort"),
    /**
     * 三向切分快速排序
     */
    Quick3waySort("Quick3waySort"),
    /**
     * 快速双轴排序
     */
    DualPivotQuickSort("DualPivotQuickSort"),
    /**
     * 希尔排序
     */
    ShellSort("ShellSort");

    private String name;
    private static Map<String,SortMethod> cache;

    static {
        Map<String,SortMethod> map = new HashMap<>(32);
        map.put("InsertSort",           new InsertionSort());
        map.put("SelectionSort",        new SelectionSort());
        map.put("BubbleSort",           new BubbleSort());
        map.put("BucketSort",           new BucketSort());
        map.put("CountingSort",         new CountingSort());
        map.put("HeapSort",             new HeapSort());
        map.put("MergeSort",            new MergeSort());
        map.put("MergeBuSort",          new MergeBuSort());
        map.put("MergeOptimizedSort",   new MergeOptimizedSort());
        map.put("QuickSort",            new QuickSort());
        map.put("Quick3waySort",        new Quick3waySort());
        map.put("DualPivotQuickSort",   new DualPivotQuickSort());
        map.put("ShellSort",            new ShellSort());
        cache = Collections.unmodifiableMap(map);
    }

    SortEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public SortMethod getSortMethod() {
        return cache.get(name);
    }

    public String getCnName() {
        return cache.get(name).getCnName();
    }
}
