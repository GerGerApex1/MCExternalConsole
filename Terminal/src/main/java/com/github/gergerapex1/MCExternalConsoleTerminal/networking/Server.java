package com.github.gergerapex1.MCExternalConsoleTerminal.networking;

import com.github.gergerapex1.MCExternalConsoleTerminal.Logger;
import com.github.gergerapex1.MCExternalConsoleTerminal.OutputData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class Server {
    private final int port;
    private Channel channel;
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    public Server(int port) {
        this.port = port;
    }

    public void initServer() {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            /*
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new GsonDecoder());


                             */
                            ChannelPipeline pipeline = ch.pipeline();

                            pipeline.addLast(new ProtobufVarint32FrameDecoder());
                            pipeline.addLast(new ProtobufDecoder(OutputData.ServerData.getDefaultInstance()));

                            pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                            pipeline.addLast(new ProtobufEncoder());
                            ch.pipeline().addLast(new Logger());
                            //ch.pipeline().addLast(new Adapter());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync();
            channel = f.channel();
            System.out.println("Server Initiated");
            channel.closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public void shutdownServer() {
        try {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
