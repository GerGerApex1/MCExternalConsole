package com.github.gergerapex1.MCExternalConsole.networking;

import com.github.gergerapex1.MCExternalConsoleTerminal.OutputData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ConnectionToClient {
    private final int port;
    private final String host;
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Channel channel;

    public ConnectionToClient(int port, String host) {
        this.port = port;
        this.host = host;
        init();
    }

    private void init() {
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            pipeline.addLast(new ProtobufDecoder(OutputData.ServerData.getDefaultInstance()));

                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast(new ProtobufEncoder());
                        }
                    }).option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.connect(host, port).sync();
            channel = f.channel();
            System.out.println("Connection established.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutdownClient() {
        try {
            workerGroup.shutdownGracefully();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(OutputData.ServerData message) {
        if (channel == null) {
            System.out.println("Attempting to send message but channel not initialized.");
            return;
        }
        channel.writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }
}
