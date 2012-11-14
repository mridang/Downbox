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
package com.mridang.downbox.threads.frontend;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;

import com.mridang.downbox.Downbox;
import com.mridang.downbox.helpers.LocalStrings;
import com.mridang.downbox.yooayes.MenuEvents;

public class SysTray extends Thread {

   /** The controls in the SWT form.
    */
    public static Shell shell;
    public static MenuItem mimStatus;
    public static Menu mnuRecent;
    public static Menu mnuFresh;
    public static TrayItem itmTrayItem;
    public static Menu mnuPopup;
    public static Boolean booShow = true;

   /**
    * This creates the system tray and adds all the controls to it.
    *
    * @returns
    */
    @Override
    public void run() {

        Display display = new Display();
        shell = new Shell(display);

        //Initialize the system tray
        Image imgTrayIcon = new Image(display, Downbox.class.getClassLoader().getResourceAsStream("traytick.ico"));
        Tray trySysTray = display.getSystemTray();
        mnuPopup = new Menu(shell, SWT.POP_UP);

        itmTrayItem = new TrayItem(trySysTray, SWT.NONE);
        itmTrayItem.setToolTipText("Downbox");

        itmTrayItem.addListener(SWT.MenuDetect, new Listener() {
            @Override
            public void handleEvent(Event event) {
                mnuPopup.setVisible(booShow);
            }
        });

        MenuItem mimOpenFolder = new MenuItem(mnuPopup, SWT.PUSH);
        mimOpenFolder.setText(LocalStrings.getText("openFolder"));
        mimOpenFolder.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                MenuEvents.openFolder();
            }
        });

        MenuItem mimLaunchDownboxWebsite = new MenuItem(mnuPopup, SWT.PUSH);
        mimLaunchDownboxWebsite.setText(LocalStrings.getText("launchWebsite"));
        mimLaunchDownboxWebsite.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                MenuEvents.launchWebsite();
            }
        });

        //Separator
        @SuppressWarnings("unused")
        MenuItem mimFirstSeparator = new MenuItem(mnuPopup, SWT.SEPARATOR);

        MenuItem mimRecentlyCleanedDirectories = new MenuItem(mnuPopup, SWT.CASCADE);
        mimRecentlyCleanedDirectories.setText(LocalStrings.getText("directoriesToProcess"));
        mnuRecent = new Menu(mnuPopup);
        mimRecentlyCleanedDirectories.setMenu (mnuRecent);

        //Separator
        @SuppressWarnings("unused")
        MenuItem mimSecondSeparator = new MenuItem(mnuPopup, SWT.SEPARATOR);

        mimStatus = new MenuItem(mnuPopup, SWT.PUSH);
        mimStatus.setText(LocalStrings.getText("idleWaiting"));
        mimStatus.setEnabled(false);

        //Separator
        @SuppressWarnings("unused")
        MenuItem mimThirdSeparator = new MenuItem(mnuPopup, SWT.SEPARATOR);

        MenuItem mimFreshlyProcessedDirectories = new MenuItem(mnuPopup, SWT.CASCADE);
        mimFreshlyProcessedDirectories.setText(LocalStrings.getText("freshlyProcessedDirectories"));
        mnuFresh = new Menu(mnuPopup);
        mimFreshlyProcessedDirectories.setMenu (mnuFresh);

        //Separator
        @SuppressWarnings("unused")
        MenuItem mimFourthSeparator = new MenuItem(mnuPopup, SWT.SEPARATOR);

        MenuItem mimRecheckFolders = new MenuItem(mnuPopup, SWT.PUSH);
        mimRecheckFolders.setText(LocalStrings.getText("recheckFolders"));
        mimRecheckFolders.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                MenuEvents.recheckDirectory();
            }
        });

        MenuItem mimExit = new MenuItem(mnuPopup, SWT.PUSH);
        mimExit.setText(LocalStrings.getText("exitApplication"));
        mimExit.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                MenuEvents.quitApplication();
            }
        });

        itmTrayItem.addListener(SWT.DefaultSelection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                MenuEvents.openFolder();
            }
        });

        //Finalize the system tray
        mnuPopup.setDefaultItem(mimOpenFolder);
        itmTrayItem.setImage(imgTrayIcon);
        itmTrayItem.setToolTipText("Downbox");

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
            display.sleep();
        }

        imgTrayIcon.dispose();
        display.dispose();
    }

}