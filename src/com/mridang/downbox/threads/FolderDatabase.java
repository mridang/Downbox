package com.mridang.downbox.threads;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.mridang.downbox.helpers.DataDirectory;
import com.mridang.downbox.helpers.LocalStrings;
import com.mridang.downbox.helpers.alerters.ErrorMessages;

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
    @SuppressWarnings("unchecked")
    public static void loadData() {

        try {


              //We should try and use a folders file in the same directory if one exists. If one
              //doesn't exist, we will try and use the on in the platform's default data directory.
              File filFolders;
              if ((new File("folders.lst")).exists()) {
                  filFolders = new File("folders.lst");
              } else {
                  //If the default data directory doesn't exist, we will try and create it.
                  filFolders = new File(DataDirectory.getDefaultDataDirectory(), "folders.lst");
                  if (!filFolders.exists()) {
                      if (!filFolders.getParentFile().exists()) {
                          Boolean booSuccessfullyCreatedDirectory = filFolders.getParentFile().mkdirs();
                          if (!booSuccessfullyCreatedDirectory) {
                              throw new IOException(LocalStrings.getText("errFolderDatabaseUnableToWriteError"));
                          }
                      }
                      //We will also try and create an empty folders file.
                      Boolean booSuccessfullyCreatedFile = filFolders.createNewFile();
                      if (!booSuccessfullyCreatedFile) {
                          throw new IOException(LocalStrings.getText("errFolderDatabaseUnableToWriteError"));
                      } else {
                          FileOutputStream objFileOutputStream = new FileOutputStream(filFolders);
                          ObjectOutputStream objObjectOutputStream = new ObjectOutputStream(objFileOutputStream);
                          objObjectOutputStream.writeObject(new ArrayList<String>());
                          objObjectOutputStream.close();
                      }
                  }
              }

              //Okay, so we've probabaly found an existing folders file by now, created an empty one
              //if one didn't exist or thrown an exception we were unable to create one so we can
              //now use try to read/write serialized data from/to the file.
              FileInputStream objFileInputStream;
              objFileInputStream = new FileInputStream(filFolders);
              ObjectInputStream objObjectInputStream = new ObjectInputStream(objFileInputStream);
              objFolders = (ArrayList<String>) objObjectInputStream.readObject();
              objObjectInputStream.close();

        } catch (FileNotFoundException e) {
            ErrorMessages.show(LocalStrings.getText("errFolderDatabaseFileNotFound"));
        } catch (SecurityException e) {
            ErrorMessages.show(LocalStrings.getText("errFolderDatabaseReadPermissionsError"));
        } catch (ClassNotFoundException e) {
            ErrorMessages.show(LocalStrings.getText("errFolderDatabaseUnableToReadError"));
        } catch (IOException e) {
            ErrorMessages.show(LocalStrings.getText("errFolderDatabaseUnableToReadError"));
        }

   }


   /**
    * This removes a folder from the list of folders.
    *
    * @param    strFolderName   the name of the folder to remove
    */
    public static void delFolder(String strFolderName) {

        Iterator<String> objLooper = objFolders.iterator();

        while(objLooper.hasNext()) {

            String strElement = objLooper.next();

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


              //We should try and use a folders file in the same directory if one exists. If one
              //doesn't exist, we will try and use the on in the platform's default data directory.
              File filFolders;
              if ((new File("folders.lst")).exists()) {
                  filFolders = new File("folders.lst");
              } else {
                  //If the default data directory doesn't exist, we will try and create it.
                  filFolders = new File(DataDirectory.getDefaultDataDirectory(), "folders.lst");
                  if (!filFolders.exists()) {
                      if (!filFolders.getParentFile().exists()) {
                          Boolean booSuccessfullyCreatedDirectory = filFolders.getParentFile().mkdirs();
                          if (!booSuccessfullyCreatedDirectory) {
                              throw new IOException(LocalStrings.getText("errFolderDatabaseUnableToWriteError"));
                          }
                      }
                      //We will also try and create an empty folders file.
                      Boolean booSuccessfullyCreatedFile = filFolders.createNewFile();
                      if (!booSuccessfullyCreatedFile) {
                          throw new IOException(LocalStrings.getText("errFolderDatabaseUnableToWriteError"));
                      } else {
                          FileOutputStream objFileOutputStream = new FileOutputStream(filFolders);
                          ObjectOutputStream objObjectOutputStream = new ObjectOutputStream(objFileOutputStream);
                          objObjectOutputStream.writeObject(new ArrayList<String>());
                          objObjectOutputStream.close();
                      }
                  }
              }

              //Okay, so we've probabaly found an existing folders file by now, created an empty one
              //if one didn't exist or thrown an exception we were unable to create one so we can
              //now use try to read/write serialized data from/to the file.
              FileOutputStream objFileOutputStream;
              objFileOutputStream = new FileOutputStream(filFolders);
              ObjectOutputStream objObjectOutputStream = new ObjectOutputStream(objFileOutputStream);
              objObjectOutputStream.writeObject(objFolders);
              objObjectOutputStream.close();

        } catch (InvalidClassException e) {
            ErrorMessages.show(LocalStrings.getText("errFolderDatabaseUnableToWriteError"));
        } catch (FileNotFoundException e) {
            ErrorMessages.show(LocalStrings.getText("errFolderDatabaseFileNotFound"));
        } catch (SecurityException e) {
            ErrorMessages.show(LocalStrings.getText("errFolderDatabaseWritePermissionsError"));
        } catch (IOException e) {
            ErrorMessages.show(LocalStrings.getText("errFolderDatabaseUnableToWriteError"));
        }

   }


   /**
    * This checks if a folder exists in the list of folders.
    *
    * @param    strFolderName   the name of the folder to check
    */
    public static Boolean hasFolder(String strFolderName) {

        Iterator<String> objLooper = objFolders.iterator();

        while(objLooper.hasNext()) {

            String strElement = objLooper.next();

            if(strElement.equals(strFolderName)) {

                //objLooper.next();
                return true;

            }
        }

        return false;

    }

}