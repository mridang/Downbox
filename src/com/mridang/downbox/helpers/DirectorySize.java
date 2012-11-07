package com.mridang.downbox.helpers;
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

import com.mridang.downbox.threads.ApplicationData;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.rarfile.FileHeader;

/**
 * Class to calculate the sizes of files in a directory
 */
public class DirectorySize {

   /**
    * Calculates the size of subtitle files in the directory
    *
    * @param  dirDirectory  the directory whose size to calculate
    * @returns              the size of the directory
    */
    public static Long getSizeOfSubtitleFiles(File dirDirectory) {
        String[] subtitleExtensions;

        subtitleExtensions = ApplicationData.getProperty("filename.subtitle.types").split(", ");

        return getFileSize(dirDirectory, subtitleExtensions);
    }


   /**
    * Calculates the size of audio files in the directory
    *
    * @param  dirDirectory  the directory whose size to calculate
    * @returns              the size of the directory
    */
    public static Long getSizeOfAudioFiles(File dirDirectory) {
        String[] audioExtensions;

        audioExtensions = ApplicationData.getProperty("filename.audio.types").split(", ");

        return getFileSize(dirDirectory, audioExtensions);
    }


   /**
    * Calculates the size of a directory
    *
    * @param   dirDirectory  the directory whose size to calculate
    * @param   strExtentions the list of extensions to check
    * @param   setProcessed  the set of processed filenames
    * @returns              the size of the directory
    */
    public static Long getFileSize(File dirDirectory, String[] strExtensions) {
        Long lngSize = new Long(0);

        for (File filItem : dirDirectory.listFiles()) {
            if (filItem.isDirectory()) {
                lngSize += getFileSize(filItem, strExtensions);
            }
            else {

                if (ArchiveChecker.isArchive(filItem)) {
                    try {
                        for (FileHeader objHeader : (new Archive(filItem)).getFileHeaders()) {
                            if (strExtensions == null) {
                                lngSize += objHeader.getFullUnpackSize();
                            }
                            else {
                                for(String strExtension : strExtensions) {
                                    if (FileExtention.getExtention(objHeader.getFileNameString()).equalsIgnoreCase(strExtension)) {
                                		lngSize += objHeader.getFullUnpackSize();
                                		break;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        continue;
                    }
                } else {
                    if (strExtensions == null) {
                        lngSize += filItem.length();
                    }
                    else {
                        for(String strExtension : strExtensions) {
                            if (FileExtention.getExtention(filItem.getName()).equalsIgnoreCase(strExtension)) {
                        		lngSize += filItem.length();
                        		break;
                            }
                        }
                    }
                }
            }
        }

        return lngSize;
    }


   /**
    * Calculates the size of video files in the directory
    *
    * @param  dirDirectory  the directory whose size to calculate
    * @returns              the size of the directory
    */
    public static Long getSizeOfVideoFiles(File dirDirectory) {
        String[] videoExtensions;

        videoExtensions = ApplicationData.getProperty("filename.video.types").split(", ");

        return getFileSize(dirDirectory, videoExtensions);
    }


   /**
    * Calculates the size of playlist files in the directory
    *
    * @param  dirDirectory  the directory whose size to calculate
    * @returns              the size of the directory
    */
    public static Long getSizeOfPlaylistFiles(File dirDirectory) {
        String[] playlistExtensions;

        playlistExtensions = ApplicationData.getProperty("filename.playlist.types").split(", ");

        return getFileSize(dirDirectory, playlistExtensions);
    }

}