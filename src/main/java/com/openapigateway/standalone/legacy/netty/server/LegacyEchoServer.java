package com.openapigateway.standalone.legacy.netty.server;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LegacyEchoServer {

    private final String PROPERTIES_FILE_NAME = "target/classes/legacy.properties";

    private final String PROPERTIES_ATTRIBUTE_REPLACE_WORDS_PORT = "legacies..port";

    private Properties legacyProperties;

    public LegacyEchoServer() {
        legacyProperties = new Properties();

        Reader reader = null;
        try {
            reader = new FileReader(PROPERTIES_FILE_NAME);
            legacyProperties.load(reader);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LegacyEchoServer legacyEchoServer = new LegacyEchoServer();

        legacyEchoServer.runEchoServer("LEGACY0001");
        legacyEchoServer.runEchoServer("LEGACY0002");
        legacyEchoServer.runEchoServer("LEGACY0003");
    }

    private void runEchoServer(String serverId) {
        String attributeName = PROPERTIES_ATTRIBUTE_REPLACE_WORDS_PORT.replace("..", "." + serverId + ".");
        int port = Integer.parseInt(legacyProperties.getProperty(attributeName));

        Runnable runnable = new LegacyEchoServerRunnable(port);
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
