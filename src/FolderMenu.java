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
import java.util.List;
import java.io.*;
import java.nio.file.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

public class FolderMenu {

    public static MenuItem[] lstMenuItems;

   /**
    * This adds a menu item to the folder menu.
    */
    public static void getMenuItems(final Path objLocation) {

        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                try {

                    for (Path objItem : Files.newDirectoryStream(objLocation)) {
                        if (objItem.toFile().isDirectory() && DirectoryChecker.isTorrentDirectory(objItem.toFile())) {
                            if (FolderDatabase.hasFolder(objItem.getFileName().toString()) == false) {
                                addMenuItem(objItem, false);
                            }
                            continue;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

    }


   /**
    * This checks the directory to match torrent name patterns
    */
    public static void addMenuItem(final Path objLocation, final Boolean booNotify) throws Exception {

        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                try {

                    MenuItem objFolderMenuItem = new MenuItem(SysTray.mnuRecent, SWT.PUSH);
                    objFolderMenuItem.setText(objLocation.getFileName().toString());
                    objFolderMenuItem.addListener(SWT.Selection, new Listener() {
                        public void handleEvent(Event event) {
                            MenuEvents.processDirectory(((MenuItem) event.widget).getText());
                        }
                    });

                    if (booNotify == true) {
                        TrayNotifications.showNotification(LocalStrings.getText("newItemsAdded"), LocalStrings.getText("itemAddedToApplication"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

    }


   /**
    * This disables a menu item from the folder menu.
    */
    public static void delMenuItem(final String objFolderName) {

        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                try {

                    for (MenuItem objFolderMenuItem : SysTray.mnuRecent.getItems()) {
                        if (objFolderMenuItem.getText().equals(objFolderName)) {
                            if (FolderDatabase.hasFolder(objFolderName) == false) {
                                FolderDatabase.addFolder(objFolderName);
                            }
                            objFolderMenuItem.dispose();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

    }

}