package com.mridang.downbox.threads.backend.rename;
/*
 *  (c) Copyright (c) 2012 Mridang Agarwalla
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
import java.io.File;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.mridang.downbox.helpers.DirectorySize;

/**
 * Class to rename a directory
 */
public class RenameDirectory {

   /** The logger instance that will provide all the logger functionality
    */
    private static Logger logger = Logger.getLogger("downbox");

   /**
    * Renames a directory depending upon the content type of the directory.
    *
    * @param directory The directory which needs to be renamed
    * @return
    */
    public static File getCorrectName(File dirDirectory) {

        try {

            logger.info(String.format("Renaming the directory at: %s", dirDirectory.getPath()));

            Long lngDirectorySize = DirectorySize.getFileSize(dirDirectory, null);
            File dirNewDirectory = null;

            if ((DirectorySize.getSizeOfVideoFiles(dirDirectory) * 100L / lngDirectorySize) > 75) {
                logger.fine("It seems to be a movie directory.");
                String strName = NameCorrector.correctMovieName(dirDirectory.getName());
                strName = strName.replaceAll("[.\\\\/:*?\"<>|]?[\\\\/:*?\"<>|]*", "");
                if (strName.length() == 0) {
                    throw new IllegalStateException("Empty fileName");
                }
                dirNewDirectory = Paths.get(System.getProperty("user.home"), "Movies", strName).toFile();
                dirNewDirectory.mkdirs();
            }
            else if ((DirectorySize.getSizeOfAudioFiles(dirDirectory) * 100L / lngDirectorySize) > 75) {
                logger.fine("It seems to be a music directory.");
                String strName =  NameCorrector.correctAlbumName(dirDirectory.getName());
                strName = strName.replaceAll("[.\\\\/:*?\"<>|]?[\\\\/:*?\"<>|]*", "");
                if (strName.length() == 0) {
                    throw new IllegalStateException("Empty fileName");
                }
                dirNewDirectory = Paths.get(System.getProperty("user.home"), "Music", strName).toFile();
                dirNewDirectory.mkdirs();
            }

            logger.fine("Renamed.");

            return dirNewDirectory;

        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(e.toString());
        }

        return null;

    }

}
