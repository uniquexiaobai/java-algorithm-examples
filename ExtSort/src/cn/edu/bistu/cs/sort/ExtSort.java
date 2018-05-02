package cn.edu.bistu.cs.sort;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * Java外部排序程序
 */
public class ExtSort {

    public enum SortMode{
        /**
         * 升序
         */
        ASC,
        /**
         * 降序
         */
        DESC
    }

    private static final Logger log = Logger.getLogger(ExtSort.class);

    private int num_count;
    private int batch_size;
    private int merge_degree;

    public ExtSort(int num_count, int batch_size) {
    	this.num_count = num_count;
        this.batch_size = batch_size;
        this.merge_degree = (int) Math.ceil((double) num_count / batch_size);;
    }
    
    // reverse int[]
    private int[] reverse(int[] arr) {
    	int[] result = new int[arr.length];
    	
    	for (int i = 0; i < arr.length; i++) {
    		result[i] = arr[arr.length - i - 1];
    	}
    	return result;
    }

    /**
     *
     * @param working_dir 工作目录
     * @param sort_file 待排序的原始文件
     * @param mode 排序模式，为ASC表示为升序排序（ascending），DESC为false表示为降序排序
     * @return 返回初始分割文件列表
     * @throws IOException 文件不存在或读写异常
     */
    private List<String> initialSortAndSplit(String working_dir, String sort_file, SortMode mode) throws IOException {
    	    List<String> fileNames = new ArrayList<String>();
    		String prefixName = working_dir + "temp-file-";
    	    int[] buffer = new int[batch_size];
    	    
    	    FileReader fr = new FileReader(sort_file);
    	    BufferedReader br = new BufferedReader(fr);
    	    
    	    int i = 0, j = 0;
    	    boolean ending = false;
    	    while (!ending) {
    	    	for (j = 0; j < batch_size; j++) {
    	    		String t = br.readLine();
    	    		if (t != null) {
    	    			buffer[j] = Integer.parseInt(t);
    	    		} else {
    	    			ending = true;
    	    			break;
    	    		}
    	    	}
    	    	
    	    	if (j == 0) break;
    	    	Arrays.sort(buffer);
    	    	if (mode == ExtSort.SortMode.DESC) {
    	    		buffer = reverse(buffer);
    	    	}
    	    	String fileName = prefixName + i + ".txt";
    	    	fileNames.add(fileName);
    	    	i++;
    	    	
    	    	// write the sorted numbers to temp file
    	    	File file = new File(fileName);
    	    	if (file.exists()) {
    	    		file.delete();
    	    	} 
    	    	FileWriter fw = new FileWriter(fileName);
    	    	PrintWriter pw = new PrintWriter(fw);
    	    	for (int k = 0; k < j; k++) {
    	    		pw.println(buffer[k]);
    	    	}
                pw.close();
                fw.close();
    	    }
    	    
    	    br.close();
    	    
    	    return fileNames;
    }

    /**
     * @param working_dir 工作目录
     * @param sort_file 待排序的原始文件
     * @param sort_file 排序后的文件
     * @param mode 排序模式，为ASC表示为升序排序（ascending），DESC为false表示为降序排序
     * @throws IOException 文件不存在或读写异常
     */
    public void extSort(String working_dir, String sort_file, String result_file, SortMode mode) throws IOException {
        // 将文件按照设定的批次数据大小进行初始切分并排序
        log.info("开始执行初始文件分割和排序");
        List<String> fileNames = initialSortAndSplit(working_dir, sort_file, mode);
        log.info("初始文件分割和排序执行完毕，共产生:" + fileNames.size() + "个分割文件");
        
        // 执行归并排序
        mergeSort(fileNames, result_file, mode);
    }

    /**
     * 归并路数由merge_degree决定
     * @param fileNames 待归并的文件列表
     * @param sort_file 排序后的文件
     * @param mode 排序模式，为ASC表示为升序排序（ascending），DESC为false表示为降序排序
     * @throws IOException 文件不存在或读写异常
     */
    private void mergeSort(List<String> fileNames, String result_file, SortMode mode) throws IOException {
    	int[] topNums = new int[merge_degree];
        BufferedReader[] brs = new BufferedReader[merge_degree];
        
        for (int i = 0; i < merge_degree; i++) {
            brs[i] = new BufferedReader(new FileReader(fileNames.get(i)));
            String t = brs[i].readLine();
            if (t != null)
                topNums[i] = Integer.parseInt(t);
            else {
        		topNums[i] = mode == ExtSort.SortMode.ASC ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        	}
        }
        
        FileWriter fw = new FileWriter(result_file);
        PrintWriter pw = new PrintWriter(fw);
        
        for (int i = 0; i < num_count; i++) {
        	int fine  = topNums[0];
        	int fineIndex = 0;
        	
        	for (int j = 0; j < merge_degree; j++) {
        		boolean isFine = mode == ExtSort.SortMode.ASC ? fine > topNums[j] : fine < topNums[j];
        		if (isFine) {
        			fine = topNums[j];
        			fineIndex = j;
        		}
        	}
        	
        	pw.println(fine);
        	String t = brs[fineIndex].readLine();
        	if (t != null) {
        		topNums[fineIndex] = Integer.parseInt(t);
        	} else {
        		topNums[fineIndex] = mode == ExtSort.SortMode.ASC ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        	}
        }
        
        for (int i = 0; i < merge_degree; i++) {
        	brs[i].close();
        }
        pw.close();
        fw.close();
    }
}