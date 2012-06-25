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

public class BackendProcessor {

   /** The logger instance that will provide all the logger functionality
    */
    private static Logger logger = Logger.getLogger("downbox");

   /* Processes a directory and extracts the files, fetches subtitles for all
    * the video files and renames the directory.
    *
    * @param  objOldDirectory   The directory which needs to be processed
    */
    public static void processDirectory(File objOldDirectory) {

        LoggingConfiguration.initLoggingSystem(objOldDirectory);

        ProcessingStatus.startProcessing(objOldDirectory.getName());

        logger.info(String.format("Processing directory: %s", objOldDirectory.getPath()));

        File objTempDirectory = new File(System.getProperty("java.io.tmpdir"), objOldDirectory.getName());
        File objNewDirectory = null;

        try {

            logger.info("Creating temporary directory.");
            objTempDirectory.mkdirs();
            logger.finer("Temporary directory created.");

        } catch (Exception ex) {
            try {
                objTempDirectory.delete();
            } catch (SecurityException e) {
                e.printStackTrace();
            } finally {
                ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseUnableToReadError"));
                return;
            }
        }

        try {

            logger.info("Copying files.");
            ContentCopier.copyFiles(objOldDirectory, objTempDirectory, objOldDirectory);
            logger.finer("All files copied.");

        } catch (Exception ex) {
            try {
                objTempDirectory.delete();
            } catch (SecurityException e) {
                e.printStackTrace();
            } finally {
                ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseUnableToReadError"));
                return;
            }
        }


        try {

            logger.info("Extracting files.");
            ExtractArchive.extractArchives(objOldDirectory, objTempDirectory);
            logger.finer("All files extracted.");

        } catch (Exception ex) {
            try {
                objTempDirectory.delete();
            } catch (SecurityException e) {
                e.printStackTrace();
            } finally {
                ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseUnableToReadError"));
                return;
            }
        }


        try {

            logger.info("Fetching subtitles.");
            SubtitleFetcher.subtitleVideos(objTempDirectory);
            logger.finer("All subtitles fetched.");

        } catch (Exception ex) {
            ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseUnableToReadError"));
        }


        try {

            logger.info("Renaming directory.");
            objNewDirectory = RenameDirectory.getCorrectName(objTempDirectory); //TODO: This doesn't seem to throw an exception. Why?
            logger.finer("All directories renamed.");

        } catch (Exception ex) {
            try {
                objTempDirectory.delete();
            } catch (SecurityException e) {
                e.printStackTrace();
            } finally {
                ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseUnableToReadError"));
            }
        }


        try {

            logger.info("Moving files.");
            ContentMover.moveFiles(objTempDirectory, objNewDirectory, objTempDirectory);
            logger.finer("All files moved.");

        } catch (Exception ex) {
            try {
                objTempDirectory.delete();
            } catch (SecurityException e) {
                e.printStackTrace();
            } finally {
                ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseUnableToReadError"));
                return;
            }
        }

        System.gc();


        try {

            logger.info("Deleting temporary directory.");
            objTempDirectory.delete();
            logger.finer("Temporary directory deleted.");

        } catch (Exception ex) {
            ex.printStackTrace();
        }


        logger.fine("Processed.");

        ProcessingStatus.stopProcessing(objOldDirectory.getName());

        FolderMenu.delMenuItem(objOldDirectory.getName());

    }

}