package com.mridang.downbox.threads.backend.ioutils;
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
import java.nio.file.Files;
import java.util.logging.Logger;

import com.mridang.downbox.helpers.FileExtention;
import com.mridang.downbox.helpers.IgnoredItem;

/**
 * Class to fetch subtitles for a the video files in a directory
 */
public class ContentCopier {

   /** The logger instance that will provide all the logger functionality
    */
    private static Logger logger = Logger.getLogger("downbox");

   /**
    * Copies all the media files in the directory to the other directory.
    *
    * @param  dirSourceDirectory       the source directory
    * @param  dirDestinationDirectory  the destination directory
    * @throws                          any exceptions encountered
    */
    public static void copyFiles(File dirSource, File dirDestination, File dirReference) throws Exception {

         try {

            logger.info(String.format("Copying all media files in: %s", dirSource));

            for (File filItem : dirSource.listFiles()) {

                if (IgnoredItem.isUseless(filItem.getName()) == false) {

                    if (filItem.isDirectory()) {

                        copyFiles(filItem, dirDestination, dirReference);

                    } else {

                        if (FileExtention.isVideoFile(filItem.getName()) || FileExtention.isAudioFile(filItem.getName())
                        || FileExtention.isSubtitleFile(filItem.getName()) || FileExtention.isPlaylistFile(filItem.getName())) {

                            logger.info(String.format("Copying the media file: %s", filItem.getName()));
                            dirDestination.toPath().resolve(dirReference.toPath().relativize(filItem.toPath())).toFile().getParentFile().mkdirs();

                            if (!dirDestination.toPath().resolve(dirReference.toPath().relativize(filItem.toPath())).toFile().exists()) {
                                Files.copy(filItem.toPath(), dirDestination.toPath().resolve(dirReference.toPath().relativize(filItem.toPath())));
                                logger.fine("Copied.");
                            } else {
                                logger.fine("Skipped.");
                                continue;
                            }

                        }

                    }

                }

            }

            logger.fine("Copied.");

        } catch (Exception e) {
            e.printStackTrace();
            logger.warning("There was an error copying the files");
            throw e;
        }

    }

}