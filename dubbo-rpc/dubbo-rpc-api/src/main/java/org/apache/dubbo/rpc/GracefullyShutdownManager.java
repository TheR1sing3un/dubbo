package org.apache.dubbo.rpc;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.ConcurrentHashSet;
import org.apache.dubbo.registry.integration.RegistryDirectory;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.support.AbstractClusterInvoker;
import org.apache.dubbo.rpc.proxy.InvokerInvocationHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GracefullyShutdownManager {

    public static final Logger logger = LoggerFactory.getLogger(GracefullyShutdownManager.class);

    // stopping provider service url
    private final ConcurrentHashSet<URL> stoppingProviderService = new ConcurrentHashSet<>();

    // provider be invoking count

    private final ConcurrentHashMap<URL/*provider service*/, AtomicInteger/*invoking count*/> invokingCounters = new ConcurrentHashMap<>();

    // consumer invoking count

    private final ConcurrentHashMap<URL, AtomicInteger> consumerInvokingCounters = new ConcurrentHashMap<>();
    private static final GracefullyShutdownManager instance = new GracefullyShutdownManager();

    public static GracefullyShutdownManager getInstance() {
        return instance;
    }

    public void addStoppingProviderService(URL url) {
        this.stoppingProviderService.add(url);
    }

    public boolean isStopping(URL url) {
        return this.stoppingProviderService.contains(url);
    }

    public void removedOfflineProvider(Invocation invocation) {
        URL needToBeRemovedUrl = invocation.getInvoker().getUrl();
        Object proxyObject = invocation.getServiceModel().getProxyObject();
        try {
            // get invokerInvocationHandler from proxyObject
            Field handlerField = proxyObject.getClass().getDeclaredField("handler");
            handlerField.setAccessible(true);
            InvokerInvocationHandler invokerInvocationHandler = (InvokerInvocationHandler) handlerField.get(proxyObject);
            // get invoker from proxyObject
            Field invokerField = invokerInvocationHandler.getClass().getDeclaredField("invoker");
            Invoker<?> topInvoker = (Invoker<?>) invokerField.get(invokerInvocationHandler);
            if (topInvoker instanceof AbstractClusterInvoker) {
                // only extends from AbstractClusterInvoker can it has the field "directory"
                // get directory from invoker
                Method getDirectory = topInvoker.getClass().getDeclaredMethod("getDirectory");
                Directory<?> directory = (Directory<?>) getDirectory.invoke(topInvoker);
                if (directory instanceof RegistryDirectory) {
                    RegistryDirectory registryDirectory = (RegistryDirectory) directory;
                    registryDirectory.notifyUrlDisable(Arrays.asList(needToBeRemovedUrl));
                    logger.info(String.format("notify the registryDirectory: %s, invoker url: %s is disable", registryDirectory, needToBeRemovedUrl));
                }
            }
        } catch (Exception e) {
            logger.warn(String.format("fail to remove invoker url: %s", needToBeRemovedUrl), e);
        }
    }

    public synchronized void invoking(URL url) {
        if (!this.invokingCounters.containsKey(url)) {
            this.invokingCounters.put(url, new AtomicInteger(0));
        }
        this.invokingCounters.get(url).incrementAndGet();
    }

    public synchronized void invoked(URL url) {
        this.invokingCounters.get(url).decrementAndGet();
    }


}
