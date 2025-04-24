package com.neo.springcloud.util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.sun.management.OperatingSystemMXBean;

/**
 * 获取服务指标的工具类
 *
 * @author neo
 * @since 2025/4/21
 * @version 1.0
 */
public class ServerMetricUtil {
    private static OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean)ManagementFactory
            .getOperatingSystemMXBean();

    /**
     * 获取服务器CPU占用率
     *
     * @return CPU占用率
     * @author neo
     * @since 2025/4/21
     */
    public static int getCPUUsage() {
        double cpuUsage = operatingSystemMXBean.getSystemCpuLoad();
        return (int)(cpuUsage * 100);
    }

    /** 
     * 获取服务器内存使用率
     *
     * @return 内存使用率
     * @author neo
     * @since 2025/4/21
     */ 
    public static int getMemoryUsage(){
        long totalMemory = operatingSystemMXBean.getTotalPhysicalMemorySize();
        long freeMemory = operatingSystemMXBean.getFreePhysicalMemorySize();

        double memoryUsage = ((totalMemory - freeMemory) * 1.0) / totalMemory;
        return (int)(memoryUsage * 100);
    }

    /**
     * 获取服务器IP地址
     *
     * @return 服务器IP
     * @author neo
     * @since 2025/4/21
     */
    public static String getHostIp() throws SocketException {
        // 遍历所有接口
        Enumeration<NetworkInterface> ifs  = NetworkInterface.getNetworkInterfaces();
        for (; ifs.hasMoreElements(); ) {
            NetworkInterface networkInterface = ifs.nextElement();

            // 遍历每次接口的IP地址
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            for (; addresses.hasMoreElements();) {
                InetAddress address = addresses.nextElement();
                if (address.isLoopbackAddress()) {
                    continue;
                }

                if (address.isSiteLocalAddress()) {
                    return address.getHostAddress();
                }
            }
        }

        return "";
    }
}
