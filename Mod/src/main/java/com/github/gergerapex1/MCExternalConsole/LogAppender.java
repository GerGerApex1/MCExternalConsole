package com.github.gergerapex1.MCExternalConsole;

import com.github.gergerapex1.MCExternalConsole.networking.ConnectionToClient;
import com.github.gergerapex1.MCExternalConsoleTerminal.OutputData;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;

public class LogAppender extends AbstractAppender {
    ConnectionToClient client;

    public LogAppender(String name) {
        super(name, null, null);
        client = new ConnectionToClient(9077, "localhost");
    }

    @Override
    public void append(LogEvent event) {
        OutputData.ServerData.Builder builder = OutputData.ServerData.newBuilder();
        builder.setTimestamp(System.currentTimeMillis());
        builder.setLoggerName(event.getLoggerName());
        builder.setMessage(event.getMessage().getFormattedMessage());
        client.sendMessage(builder.build());
    }

    public void shutdownClient() {
        client.shutdownClient();
    }
}
