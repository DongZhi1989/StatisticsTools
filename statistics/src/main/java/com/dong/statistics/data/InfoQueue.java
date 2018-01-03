package com.dong.statistics.data;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author <dr_dong>
 *         Time : 2017/12/14 11:44
 */
public class InfoQueue {

    public static final String TAG = InfoQueue.class.getSimpleName();
    private static InfoQueue instance;

    private BlockingQueue<StatisticsInfo> queue;
    private BlockingQueue<StatisticsInfo> queueBackups;

    private InfoQueue() {
        this.queue = new LinkedBlockingQueue<>();
        this.queueBackups = new LinkedBlockingQueue<>();
    }

    public static InfoQueue getInstance() {
        if (instance == null) {
            instance = new InfoQueue();
        }
        return instance;
    }

    /**
     * 获取队列
     *
     * @return
     */
    public BlockingQueue<StatisticsInfo> getQueue() {
        return queue;
    }

    /**
     * 添加
     *
     * @param info
     * @return
     */
    public boolean add(StatisticsInfo info) {
        return info != null && queue.offer(info);
    }

    /**
     * 获取
     *
     * @param removeSwitch 获取第一个时，是否在队列中移除他，poll 移除，peek 不移除
     * @return
     */
    public StatisticsInfo get(boolean removeSwitch) {
        if (removeSwitch) {
            return queue.poll();
        } else {
            return queue.peek();
        }
    }

    /**
     * 获取队列
     *
     * @return
     */
    public BlockingQueue<StatisticsInfo> getQueueBackups() {
        return queueBackups;
    }

    /**
     * 添加备份
     *
     * @param info 需要备份的信息
     * @return 添加备份成功返回 true
     */
    public boolean addBackups(StatisticsInfo info) {
        return info != null && queueBackups.offer(info);
    }

    /**
     * 获取
     *
     * @return StatisticsInfo
     */
    public StatisticsInfo getBackups() {
        return queueBackups.poll();
    }

}
