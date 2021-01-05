package com.kaikeba;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * 功能描述：
 *
 * @author liujiayu
 * @version 1.0.0
 * @date 2021-01-05 14:11:00
 */
public class ZkClientTest {

    private static final String CLUSTER = "47.102.217.46:2181";

    private static final String PATH = "/zkclient";

    public static void main(String[] args) throws InterruptedException {
        // 创建客户端连接
        ZkClient zkClient = new ZkClient(CLUSTER);

        // 创建持久节点
        String nodeName = zkClient.create(PATH, "hello", CreateMode.PERSISTENT);
        System.out.println("创建了一个节点：" + nodeName);

        Object data = zkClient.readData(PATH);
        System.out.println(data);

        // 创建子节点
        zkClient.create(PATH + "/aaa", "helloAAA", CreateMode.PERSISTENT_SEQUENTIAL);
        zkClient.create(PATH + "/bbb", "helloBBB", CreateMode.PERSISTENT_SEQUENTIAL);
        zkClient.create(PATH + "/ccc", "helloCCC", CreateMode.EPHEMERAL);

        // 查看子节点列表
        List<String> children = zkClient.getChildren(PATH);
        System.out.println(children);

        // 注册watcher（监听变更）
        zkClient.subscribeDataChanges(PATH, new IZkDataListener() {
            /**
             * 当数据变更时触发
             * @param s 监听路径
             * @param o 变更后的数据
             * @throws Exception
             */
            @Override
            public void handleDataChange(String s, Object o) throws Exception {
                System.out.println("节点" + s + "的数据已经更新为：" + o);
            }

            /**
             * 当数据被删除时触发
             * @param s 监听路径
             * @throws Exception
             */
            @Override
            public void handleDataDeleted(String s) throws Exception {
                System.out.println(s + "的数据内容被删除了");
            }
        });

        // 更新数据
        if (zkClient.exists(PATH)) {
            zkClient.writeData(PATH, "world");
            Object updateData = zkClient.readData(PATH);
            System.out.println("更新过的数据内容为：" + updateData);
        }
        // 稍微等待一下回调的执行
        Thread.sleep(3000L);

        if (zkClient.exists(PATH + "/aaa0000000000")) {
            zkClient.delete(PATH + "/aaa0000000000");
        }
    }

}
