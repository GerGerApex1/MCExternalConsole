package com.github.gergerapex1.MCExternalConsoleTerminal;

import com.github.gergerapex1.MCExternalConsoleTerminal.OutputData.ServerData;
import com.github.gergerapex1.MCExternalConsoleTerminal.utils.OutputColor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.text.SimpleDateFormat;

public class Logger extends SimpleChannelInboundHandler<OutputData.ServerData> {
    SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd hh:mm:ss]");
    String lastMessage = "";
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ServerData msg) {
        if(lastMessage.equals(msg.getMessage())) {
            return;
        }
        String timestampParse = dateFormat.format(msg.getTimestamp()) + " ";
        String loggerName = OutputColor.color(OutputColor.YELLOW, "[" + msg.getLoggerName() + "]");
        String message = OutputColor.color(OutputColor.BLUE, "[" + msg.getMessage() + "]");
        System.out.println(timestampParse + loggerName + message);
        lastMessage = msg.getMessage();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Channel Active");
    }
}