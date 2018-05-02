package cn.edu.bistu.cs.sort;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    private static final Logger log = Logger.getLogger(Main.class);
    
    public static void generateFile(String sort_file, int count) throws IOException {
        File file = new File(sort_file);
        Random r = new Random();
        if (file.exists())
            file.delete();
        FileWriter fw = new FileWriter(file);
        for (int i = 0; i < count; i++) {
            fw.write(r.nextInt() + "\n");
        }
        fw.close();
    }

    public static void main(String[] args) throws IOException {
        String working_dir = "/Users/baihang/code/java/eclipse-workspace/ExtSort/";
        String sort_file = working_dir + "sort.txt";
        String sort_result = working_dir +  "result.txt";
        int num_count = 10000000;
        int batch_size = 11000;
        
        generateFile(sort_file, num_count);
        
        log.info("开始外部排序");
        long start = System.currentTimeMillis();
//        ExtSort extSort = new ExtSort(50, 10);
        ExtSort extSort = new ExtSort(num_count, batch_size);
        extSort.extSort(working_dir, sort_file, sort_result, ExtSort.SortMode.ASC); //升序排列
        
        log.info("排序完成，排序结果文件为:" + sort_result);
        log.info("排序耗时:"+ (System.currentTimeMillis() - start) / 1000.0 + "秒");
    }
}
