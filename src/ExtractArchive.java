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
import java.io.*;
import java.util.logging.Logger;

/**
 * Class to extract RAR archives from a directory
 */
public class ExtractArchive {

   /** The logger instance that will provide all the logger functionality
    */
    private static Logger logger = Logger.getLogger("downbox");

   /**
    * Uncompresses all the RAR archives in a given directory
    *
    * @param  objSourceDirectory        the source directory
    * @param  objDestinationDirectory   the destination directory
    */
    public static void extractArchives(File objSourceDirectory, File objDestinationDirectory) throws Exception {

        try {
          
            logger.info(String.format("Extracting files in: %s", objSourceDirectory));

            for (File filItem : objSourceDirectory.listFiles()) {
                if (filItem.isDirectory()) {
                    extractArchives(filItem, objDestinationDirectory);
                } else {
                    if (FileExtention.getExtention(filItem.getName()).equalsIgnoreCase("RAR")) {
                        RarExtractor.extractFiles(filItem, objDestinationDirectory);
                    }
                }
            }
            
            logger.fine("Extracted.");

        } catch (Exception e) {
            logger.warning("There was an error extracting the files");
            throw e;
        }
    }

}