package com.github.gergerapex1.MCExternalConsoleTerminal;

import com.github.gergerapex1.MCExternalConsoleTerminal.networking.Server;
import com.github.gergerapex1.MCExternalConsoleTerminal.utils.AsciiArt;

import java.util.Objects;

public class Launcher {
    public static void main(String[] args) {
        System.out.println(AsciiArt.generateAsciiArt("MCExternalConsole", 200, 20));
        if(Objects.equals(System.getenv("ENVIROMENT"), "_DEV")) {
            System.out.println("Running on Development Mode");
        }
        Server server = new Server(9077);
        System.out.println("Initiating Server...");
        registerShutdownEvent(server);
        server.initServer();
    }
    private static void registerShutdownEvent(Server server) {
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownServer));
    }
}
