package com.example.stock.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
@Component
public class DataSavePipelineThread extends Thread{
    private Queue<String> memoryQueue;

    public DataSavePipelineThread() {
        this.memoryQueue = new LinkedList<>();
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(1);
                String res = memoryQueue.poll();
                if(null == res)
                    continue;


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void add(String str) {
        memoryQueue.offer(str);
    }
}
