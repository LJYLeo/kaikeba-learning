package com.kaikeba;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * 功能描述：
 *
 * @author liujiayu
 * @version 1.0.0
 * @date 2021-01-05 15:52:00
 */
public class CuratorTest {

    private static final String CLUSTER = "47.102.217.46:2181";

    private static final String ROOT_PATH = "curator";

    public static void main(String[] args) throws Exception {

        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(CLUSTER)
                // 连接超时时间
                .connectionTimeoutMs(13000)
                // 会话超时时间
                .sessionTimeoutMs(15000)
                // 重试策略：每隔1s重试一次，最多3次
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                // 指定客户端命名空间
                .namespace(ROOT_PATH).build();

        // 开启客户端
        client.start();

        String nodePath = "/host";

        // 创建当前命名空间的子节点（默认持久，且没有数据）
        client.create().forPath("/host");
        // 包含数据内容的持久节点
        client.create().forPath("/host", "hello".getBytes());
        // 创建临时节点
        client.create().withMode(CreateMode.EPHEMERAL).forPath("/host", "hello".getBytes());
        // 递归创建，若父节点不存在，先创建父节点
        String nodeName = client.create().creatingParentContainersIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(nodePath, "hello".getBytes());
        System.out.println("新创建的节点为：" + nodeName);

        // 获取数据并注册watcher
        byte[] date = client.getData().usingWatcher((CuratorWatcher) event -> {
            System.out.println(event.getPath() + "的数据内容发生了变化");
        }).forPath(nodePath);
        System.out.println("当前节点数据内容为：" + new String(date));

        // 更新数据
        client.setData().forPath(nodePath, "world".getBytes());
        byte[] newData = client.getData().forPath(nodePath);
        System.out.println("更新后的数据内容为：" + new String(newData));

        // 获取子节点列表并注册watcher
        List<String> children = client.getChildren().usingWatcher((CuratorWatcher) event -> {
            System.out.println(event.getPath() + "的子节点列表发生了变化");
        }).forPath("/");
        System.out.println("/curator节点的子节点列表为：" + children);

        client.create().forPath("/port");
        children = client.getChildren().forPath("/");
        System.out.println("/curator节点的子节点列表为：" + children);

        if (client.checkExists().forPath("/") != null) {
            // 递归删除
            client.delete().deletingChildrenIfNeeded().forPath("/");
        }
    }

}
