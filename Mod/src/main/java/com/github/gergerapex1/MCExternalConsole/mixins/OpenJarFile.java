package com.github.gergerapex1.MCExternalConsole.mixins;

import com.github.gergerapex1.MCExternalConsoleTerminal.Launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.jar.JarInputStream;

public class OpenJarFile {
    public static void launchTerminalServer() {
        Thread thread = new Thread(new LaunchServerThread());
        thread.start();
    }

    private static class LaunchServerThread implements Runnable {
        @Override
        public void run() {
            String javaLoc = new File(System.getProperty("java.home"), "\\bin\\java.exe").getPath();
            String modPath;
            try {
                modPath = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toString();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            isServerStandalone(modPath);
            try {
                String commandLine = "cmd /c start \"\" \"" + javaLoc + "\" -jar " + modPath;
                System.out.println(commandLine);
                Runtime.getRuntime().exec(commandLine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean isServerStandalone(String location) {
            try {
                FileInputStream inputStream = new FileInputStream(location);
                JarInputStream jarInputStream = new JarInputStream(inputStream);
                // TODO: implement sumthing btr
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
}
