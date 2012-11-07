package com.mridang.downbox.threads.backend.subtitles;
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
import java.io.*;
import java.util.logging.Logger;

import com.mridang.downbox.helpers.DirectorySize;
import com.mridang.downbox.helpers.FileExtention;

/**
 * Class to fetch subtitles for a the video files in a directory
 */
public class SubtitleFetcher {

   /** The logger instance that will provide all the logger functionality
    */
    private static Logger logger = Logger.getLogger("downbox");


   /**
    * Fetches the subtitles for all the video files in a directory
    *
    * @param  dirDirectory   the directory for whose videos to find subtitles for
    */
    public static Boolean hasSubtitles(File dirDirectory) throws Exception {
 
         try {
            
            if (DirectorySize.getSizeOfSubtitleFiles(dirDirectory) > 0) {
                logger.info("The directory seems to have subtitles already.");
                return true;
            }
            
            return false;
        
        } catch (Exception e) {
            throw e;
        }

    }


   /**
    * Fetches the subtitles for all the video files in a directory
    *
    * @param  dirDirectory   the directory for whose videos to find subtitles for
    */
    public static void fetchSubtitles(File dirDirectory) throws Exception {
 
         try {
            
            logger.info(String.format("Fetching subtitle for files in: %s", dirDirectory));
            
            for (File filItem : dirDirectory.listFiles()) {
                if (filItem.isDirectory()) {
                    fetchSubtitles(filItem);
                } else {
                    if (FileExtention.isVideoFile(filItem.getName())) {
                        OpenSubtitles.searchAndDownloadSubtitles(filItem);
                    }
                }
            }
            
            logger.fine("Subtitled.");
        
        } catch (Exception e) {
            throw e;
        }

    }


   /**
    * Checks and Fetches the subtitles for all the video files in a directory
    *
    * @param  dirDirectory   the directory for whose videos to find subtitles for
    */
    public static void subtitleVideos(File dirDirectory) {
 
         try {
            
            if (!hasSubtitles(dirDirectory)) {
                logger.info("The directory doesn't seem to have subtitles already.");
                fetchSubtitles(dirDirectory);
            }
            
            return;
        
        } catch (Exception e) {
            logger.warning("There was an error fetching subtitles");
        }

    }

}