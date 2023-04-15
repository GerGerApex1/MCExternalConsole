package com.github.gergerapex1.MCExternalConsole;

import com.github.gergerapex1.MCExternalConsole.mixins.ClientTweaker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

@Mod(modid = MCExternalConsole.MODID, version = MCExternalConsole.VERSION)
public class MCExternalConsole {
    public static final String MODID = "mc-externalconsole";
    public static final String VERSION = "1.0.0-SNAPSHOT";
    public static Logger logger;
    private LogAppender appender;
    private LoggerContext context;
    private Configuration configuration;

    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        logger = LogManager.getLogger(MCExternalConsole.class);
        appender = ClientTweaker.getAppender();
        context = (LoggerContext) LogManager.getContext(false);
        configuration = context.getConfiguration();

        logger.info("Registering loggers (Pre-Initialization)");
        unregisterLogAppenders();
        registerLogAppenders();
    }

    @EventHandler
    public void postInit(FMLInitializationEvent event) {
        logger.info("Registering loggers... again (Post-Initialization)");
        unregisterLogAppenders();
        registerLogAppenders();
    }

    private synchronized void registerLogAppenders() {
        for (LoggerConfig entry : configuration.getLoggers().values()) {
            if (entry.getName().equals("FML")) {
                return;
            }
            logger.info("Registering Logger to Log-Appender-0: " + entry.getName());
            entry.addAppender(appender, Level.INFO, null);
        }
        context.updateLoggers(configuration);
    }

    private synchronized void unregisterLogAppenders() {
        for (LoggerConfig entry : configuration.getLoggers().values()) {
            entry.removeAppender("Log-Appender-0");
            logger.info("Unregistering Logger to Log-Appender-0: " + entry.getName());
        }
        context.updateLoggers(configuration);
    }
}
