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
import java.util.logging.Logger;

import com.mridang.downbox.helpers.ExceptionHandler;
import com.mridang.downbox.threads.ApplicationData;
import com.mridang.downbox.threads.backend.rename.lookups.MovieSearcher;
import com.mridang.downbox.threads.backend.rename.lookups.MusicSearcher;

public class NameCorrector {

   /**
    * The logger instance that will provide all the logger functionality
    */
    private static Logger logger = Logger.getLogger("downbox");

   /**
    * Cleans the name of a movie filename
    *
    * @param  strFileName  the file name from which to strip URLs
    * @return returns the cleaned file name
    */
    public static String correctMovieName(String strFileName) {
        logger.info("Cleaning the movie name");

        strFileName = strFileName.toUpperCase();
        strFileName = removeBrackets(strFileName);
        strFileName = removeSpecialChars(strFileName);
        strFileName = stripSpam(strFileName);
        // strFileName = removeURLs(strFileName);
        strFileName = getMovieTitle(strFileName);

        logger.finer(String.format("The cleaned name is: %s", strFileName));

        return strFileName;
    }

   /**
    * Gets the movie name from AllMovie
    *
    * @param  strFileName  the file name to search from AllMovie
    * @return returns the cleaned file name
    */
    private static String getMovieTitle(String strFileName) {

        try {
            logger.finer("Fetching correct name for the movie");

            for (Integer intId = 0; intId < strFileName.trim().replaceAll("\\s+", " ").split(" ").length; intId++) {

                String strQuery = strFileName.trim().replaceAll("\\s+", " ");

                for (Integer intCounter = 0; intCounter < intId; intCounter++) {
                    strQuery = strQuery.replaceFirst("\\s?\\w+\\s?$", "");
                }

                logger.finest(String.format("Searching for %s", strQuery));

                // TODO Use the year for comparison too to get a more accurate match
                BestMatch objMatch = MovieSearcher.getBestMatch(strQuery);

                if (objMatch != null) {
                    strFileName = objMatch.getTitle();
                    break;
                }

            }

            logger.finer(String.format("Fetched correct name: %s", strFileName));

        } catch (Exception e) {
            logger.warning("An error occurred when get the movie title");
            ExceptionHandler.saveError(e);
        }

        return strFileName;

    }

   /**
    * Strips spam words from the filename
    *
    * @param  strFileName  the file name from which to strip spam words
    * @return the filename with the spam words stripped off
    */
    private static String stripSpam(String strFileName) {

        try {

            logger.finer("Stripping spam words");

            String spam[] = ApplicationData.getProperty("cleaner.rename.spam").split(", ");

            for (Integer iFilt = 0; iFilt < spam.length; iFilt++) {
                Integer nFoundPos = strFileName.indexOf(" " + spam[iFilt] + " ");
                if (nFoundPos != -1) {
                    strFileName = strFileName.substring(0, nFoundPos);
                }
            }

            logger.finer(String.format("Stripped spam words from the filename: %s", strFileName));

        } catch (Exception e) {
            logger.warning("An error occurred when trying to strip spam words");
            ExceptionHandler.saveError(e);
        }

        return strFileName;

    }

   /**
    * Removes all the special characters from the file name.
    *
    * @param  strFileName  the file name from which to remove special characters
    * @return returns the cleaned file name
    */
    private static String removeSpecialChars(String strFileName) {

        try {

            logger.finer("Removing special characters");

            strFileName = strFileName.replace(".", " ");
            strFileName = strFileName.replace("-", "");
            strFileName = strFileName.replace("_", " ");

            logger.finer(String.format("Removed special characters from the filename: %s",
                    strFileName));

        } catch (Exception e) {
            logger.warning("An error occurred when trying to remove remove special characters");
            ExceptionHandler.saveError(e);
        }

        return strFileName;

    }

   /**
    * Gets the the album name from AllMusic
    *
    * @param  strFileName  the file name to search from AllMusic
    * @return returns the cleaned file name
    */
    private static String getAlbumName(String strFileName) {

        try {

            logger.finer("Fetching correct name for the album");

            for (Integer intId = 0; intId < strFileName.trim().replaceAll("\\s+", " ").split(" ").length; intId++) {

                String strQuery = strFileName.trim().replaceAll("\\s+", " ");

                for (Integer intCounter = 0; intCounter < intId; intCounter++) {
                    strQuery = strQuery.replaceFirst("\\s?\\w+\\s?$", "");
                }

                logger.finest(String.format("Searching for %s", strQuery));

                // TODO Use the year for comparison too to get a more accurate
                // match
                BestMatch objMatch = MusicSearcher.getBestMatch(strQuery);

                if (objMatch != null) {
                    strFileName = objMatch.getTitle();
                    break;
                }

            }

            logger.finer(String.format("Fetched correct name: %s", strFileName));

        } catch (Exception e) {
            logger.warning("An error occurred when trying to get the album name");
            ExceptionHandler.saveError(e);
        }

        return strFileName;

    }

   /**
    * Removes all the brackets from the file name.
    *
    * @param  strFileName  the file name from which to remove brackets
    * @return returns the cleaned file name
    */
    private static String removeBrackets(String strFileName) {

        try {

            logger.fine("Removing brackets");

            strFileName = strFileName.replaceAll("\\((.*?)\\)", " $1 ");
            strFileName = strFileName.replaceAll("\\[(.*?)\\]", " $1 ");
            strFileName = strFileName.replaceAll("\\{(.*?)\\}", " $1 ");

            logger.finer(String.format("Removed brackets from the filename: %s", strFileName));

        } catch (Exception e) {
            logger.warning("An error occurred when trying to remove the brackets");
            ExceptionHandler.saveError(e);
        }

        return strFileName;

    }

   /**
    * Cleans the name of a music filename
    *
    * @param  strFileName  the file name from which to strip URLs
    * @return returns the cleaned file name
    */
    public static String correctAlbumName(String strFileName) {
        logger.info("Cleaning the album name");

        strFileName = strFileName.toUpperCase();
        strFileName = removeBrackets(strFileName);
        strFileName = removeSpecialChars(strFileName);
        strFileName = stripSpam(strFileName);
        // strFileName = removeURLs(strFileName);
        strFileName = getAlbumName(strFileName);

        logger.finer(String.format("The cleaned name is: %s", strFileName));

        return strFileName;
    }

}