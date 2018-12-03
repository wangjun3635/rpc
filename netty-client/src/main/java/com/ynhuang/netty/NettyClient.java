package com.ynhuang.netty;

import com.ynhuang.netty.domain.Request;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Auther: 018399
 * @Date: 2018/12/3 13:51
 * @Description:
 */
@Slf4j
@Component
public class NettyClient {

    private static class ClientInit{
        static final NettyClient client = new NettyClient();
    }

    public static NettyClient getServerInstance(){
        return ClientInit.client;
    }

    private EventLoopGroup mainGroup;
    private Bootstrap bootstrap;
    private ChannelFuture future;

    public NettyClient(){
        mainGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(mainGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE,
                                ClassResolvers.cacheDisabled(null)));
                        pipeline.addLast(new ObjectEncoder());
                        pipeline.addLast(new ClientHandler());
                    }
                });
    }

    public void startClient() throws InterruptedException {
        future = bootstrap.connect("127.0.0.1", 8086).sync();
        log.error("客户端启动成功......");
    }

    public void sendDtaToServer(){
        Request request = new Request();
        request.setClassName("com.ynhuang.netty.interfaces.impl.QueryUserByIdImpl");
        Object[] params = new Object[]{1};
        request.setParams(params);
        request.setMethod("queryUserById");
        ClientHandler.sendDataToServer(request);
    }

}
