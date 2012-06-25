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
import java.nio.file.*;

/**
 * Class containing methods for creating the libary folders in the user's home
 * directory.
 */
public class LibraryCreator {

   /**
    * This creates the movie folder in the user's home directory.
    */
    private static void createMovieFolder() {

        try {
            File objMovieFolder = Paths.get(System.getProperty("user.home"), "Movies").toFile();
            if (!objMovieFolder.exists ()) {
                objMovieFolder.mkdirs();
            }
        } catch (SecurityException e) {
            ErrorMessages.showErrorMessage(LocalStrings.getText("errMovieFolderCreatePermissionsError"));
        }

    }


   /**
    * This creates the movie and the music folder in the user's home directory.
    */
    private static void createFolders() {

        createMovieFolder();
        createMusicFolder();

    }


   /**
    * This creates the music folder in the user's home directory.
    */
    private static void createMusicFolder() {

        try {
            File objMusicFolder = Paths.get(System.getProperty("user.home"), "Music").toFile();
            if (!objMusicFolder.exists ()) {
                objMusicFolder.mkdirs();
            }
        } catch (SecurityException e) {
            ErrorMessages.showErrorMessage(LocalStrings.getText("errMusicFolderCreatePermissionsError"));
        }

    }

}