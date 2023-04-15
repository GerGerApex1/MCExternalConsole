package com.github.gergerapex1.MCExternalConsole.mixins;

import com.github.gergerapex1.MCExternalConsole.LogAppender;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.File;
import java.util.List;

public class ClientTweaker implements ITweaker {
    private static LogAppender appender;
    private Logger logger;

    public static LogAppender getAppender() {
        return appender;
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        logger = LogManager.getLogger(ClientTweaker.class);
        registerShutdownEvent();
        openServerTerminal();
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        MixinBootstrap.init();
        MixinEnvironment environment = MixinEnvironment.getDefaultEnvironment();
        environment.setSide(MixinEnvironment.Side.CLIENT);
        addAppender();
    }

    @Override
    public String getLaunchTarget() {
        return MixinBootstrap.getPlatform().getLaunchTarget();
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[0];
    }

    private void addAppender() {
        appender = new LogAppender("Log-Appender-0");
        appender.start();
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = context.getConfiguration();

        for (LoggerConfig entry : configuration.getLoggers().values()) {
            if (entry.getName().equals("FML")) {
                return;
            }
            System.out.println(entry.getName());
            entry.addAppender(appender, Level.INFO, null);
        }
        context.updateLoggers(configuration);
    }

    private Boolean isRunningDev() {
        return (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }

    private void registerShutdownEvent() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                appender.shutdownClient();
            }
        });
    }

    private void openServerTerminal() {
        if (isRunningDev()) {
            logger.info("Detected running on Development.");

        } else {
            logger.info("Detected running on Production.");
            OpenJarFile.launchTerminalServer();
        }
    }
}
    