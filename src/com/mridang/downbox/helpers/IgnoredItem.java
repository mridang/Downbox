package com.mridang.downbox.helpers;

import com.mridang.downbox.threads.ApplicationData;

/**
 * Class to check if a file or directory is useless and should be ignored
 */
public class IgnoredItem {

   /**
    * Checks is a file is a useless or not.
    *
    * @param    strFilename   the file which to check
    * @returns                a boolean value indicating the match
    */
    public static Boolean isUseless(String strFilename) {

        try {

            for (String strPattern : ApplicationData.getProperty("filename.ignore.patterns").split(", ")) {

                if (strFilename.toLowerCase().contains(strPattern.toLowerCase())) {
                    return true;

                }
            }
            return false;

        } catch (Exception e) {
            return null;
        }

    }

}