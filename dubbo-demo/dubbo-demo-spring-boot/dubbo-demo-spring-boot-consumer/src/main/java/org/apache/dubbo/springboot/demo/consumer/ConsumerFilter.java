package org.apache.dubbo.springboot.demo.consumer;

import com.alibaba.dubbo.rpc.cluster.Cluster;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.registry.integration.RegistryDirectory;
import org.apache.dubbo.rpc.BaseFilter;
import org.apache.dubbo.rpc.Constants;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.ClusterInvoker;
import org.apache.dubbo.rpc.cluster.Directory;
import org.apache.dubbo.rpc.cluster.directory.AbstractDirectory;
import org.apache.dubbo.rpc.cluster.filter.FilterChainBuilder;
import org.apache.dubbo.rpc.model.ServiceModel;
import org.apache.dubbo.rpc.protocol.dubbo.DubboInvoker;
import org.apache.dubbo.rpc.proxy.InvokerInvocationHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;

@Activate(group = {CONSUMER})
public class ConsumerFilter implements Filter, BaseFilter.Listener {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
//        System.out.println(appResponse);
//        System.out.println(invoker);
//        Invoker<?> invoker2 = invocation.getInvoker();
//        ServiceModel serviceModel = invocation.getServiceModel();
//        Object proxyObject = serviceModel.getProxyObject();
//        Field[] fields = proxyObject.getClass().getDeclaredFields();
//        System.out.println(fields);
//        Field field = fields[1];
//        InvokerInvocationHandler handler = null;
//        try {
//            field.setAccessible(true);
//            handler = (InvokerInvocationHandler) field.get(proxyObject);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//        Field[] declaredFields = handler.getClass().getDeclaredFields();
//        Field invokerField;
//        try {
//            invokerField = handler.getClass().getDeclaredField("invoker");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        invokerField.setAccessible(true);
//        Invoker<?> invokerReal = null;
//        try {
//            invokerReal = (Invoker<?>) invokerField.get(handler);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        }
//        Method[] declaredMethods = invokerReal.getClass().getDeclaredMethods();
//        Method getDirectory = null;
//        try {
//            getDirectory = invokerReal.getClass().getDeclaredMethod("getDirectory");
//        } catch (NoSuchMethodException e) {
//            throw new RuntimeException(e);
//        }
//        Directory directory = null;
//
//        try {
//            directory = (Directory) getDirectory.invoke(invokerReal);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//        RegistryDirectory registryDirectory = (RegistryDirectory) directory;
//        URL url = invocation.getInvoker().getUrl();
//        registryDirectory.notifyUrlDisable(Arrays.asList(url));
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {

    }
}
