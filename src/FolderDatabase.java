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
import java.util.*;

/**
 * Class containing methods for reading and writing the list of ignored and
 * processed folders for internal use.
 */
public class FolderDatabase {

   /** The array containing the list of folders
    */
    private static ArrayList<String> objFolders = new ArrayList<String>();

   /**
    * This saves the list of folders to the file
    */
    public static void loadData() {

        try {

              FileInputStream objFileInputStream = new FileInputStream("folders.lst");
              ObjectInputStream objObjectInputStream = new ObjectInputStream(objFileInputStream);
              objFolders = (ArrayList) objObjectInputStream.readObject();
              objObjectInputStream.close();

        } catch (FileNotFoundException e) {
            ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseFileNotFound"));
        } catch (SecurityException e) {
            ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseReadPermissionsError"));
        } catch (ClassNotFoundException e) {
            ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseUnableToReadError"));
        } catch (IOException e) {
            ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseUnableToReadError"));
        }

   }


   /**
    * This removes a folder from the list of folders.
    *
    * @param    strFolderName   the name of the folder to remove
    */
    public static void delFolder(String strFolderName) {

        Iterator objLooper = objFolders.iterator();

        while(objLooper.hasNext()) {

            String strElement = (String) objLooper.next();

            if(strElement.equals(strFolderName)) {

                objLooper.remove();
                break;

            }
        }

        saveData();

    }


   /**
    * This adds a folder to the list of folders.
    *
    * @param    strFolderName   the name of the folder to add
    */
    public static void addFolder(String strFolderName) {

        objFolders.add(strFolderName);
        saveData();

    }


   /**
    * This loads the list of folders from the file.
    */
    public static void saveData() {

        try {

              FileOutputStream objFileOutputStream = new FileOutputStream("folders.lst");
              ObjectOutputStream objObjectOutputStream = new ObjectOutputStream(objFileOutputStream);
              objObjectOutputStream.writeObject(objFolders);
              objObjectOutputStream.close();

        } catch (InvalidClassException e) {
            ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseUnableToWriteError"));
        } catch (FileNotFoundException e) {
            ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseFileNotFound"));
        } catch (SecurityException e) {
            ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseWritePermissionsError"));
        } catch (IOException e) {
            ErrorMessages.showErrorMessage(LocalStrings.getText("errFolderDatabaseUnableToWriteError"));
        }

   }


   /**
    * This checks if a folder exists in the list of folders.
    *
    * @param    strFolderName   the name of the folder to check
    */
    public static Boolean hasFolder(String strFolderName) {

        Iterator objLooper = objFolders.iterator();

        while(objLooper.hasNext()) {

            String strElement = (String) objLooper.next();

            if(strElement.equals(strFolderName)) {

                //objLooper.next();
                return true;

            }
        }

        return false;

    }

}