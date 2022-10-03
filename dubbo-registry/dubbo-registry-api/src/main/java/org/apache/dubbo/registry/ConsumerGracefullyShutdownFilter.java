package org.apache.dubbo.registry;

import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.BaseFilter;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;

@Activate(group = CONSUMER, value = "consumerGracefullyShutdown")
public class ConsumerGracefullyShutdownFilter implements Filter, BaseFilter.Listener {

    public static final Logger logger = LoggerFactory.getLogger(ConsumerGracefullyShutdownFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        String offline = appResponse.getAttachment("offline");
        if (StringUtils.isNotEmpty(offline)) {
            GracefullyShutdownManager.getInstance().removedOfflineProvider(invocation);
        }
    }

    @Override
    public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {

    }
}
