/*
 *  (c) Copyright (c) 2010 Mridang Agarwalla
 *  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
import java.util.logging.*;
import java.io.*;
import java.nio.file.*;

public class LoggingConfiguration {

   /** The logger instance that will provide all the logger functionality
    */
    private static Logger logger = Logger.getLogger("janitor");

   /** The custom log message format class for the UI log messages.
    */
    public static class CustomLogRecordFormat extends Formatter {

       public String format(LogRecord logLine) {
          return logLine.getLevel() + ": " + logLine.getMessage() + " [" + logLine.getSourceClassName() + "." + logLine.getSourceMethodName() + "]" + System.lineSeparator();
       }

    }


   /** Inititates the console logging system
    */
    private static void initConsoleLogging() {

        try {

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new CustomLogRecordFormat());
            logger.addHandler(consoleHandler);

        } catch (Exception e) {
            return;
        }

    }


   /** Inititates the logging system
    */
    public static void initLoggingSystem(File objLogDirectory) {

        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);

        for (Handler logHandler : logger.getHandlers()) {
            logger.removeHandler(logHandler);
        }

        initConsoleLogging();
        initFileLogging(objLogDirectory);

    }


   /** Inititates the file logging system
    */
    private static void initFileLogging(File objLogDirectory) {

        try {

            FileHandler fileHandler = new FileHandler(Paths.get(objLogDirectory.toString(), "janitor.log").toString(), true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new CustomLogRecordFormat());
            logger.addHandler(fileHandler);

        } catch (Exception e) {
            return;
        }

    }

}