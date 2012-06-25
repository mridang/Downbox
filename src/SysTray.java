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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import java.nio.file.*;

public class SysTray extends Thread {

   /** The controls in the SWT form.
    */
    public static Shell shell;
    public static MenuItem mimStatus;
    public static Menu mnuRecent;
    public static Menu mnuFresh;
    public static TrayItem itmTrayItem;

   /**
    * This creates the system tray and adds all the controls to it.
    *
    * @returns
    */
    public void run() {

        Display display = new Display();
        shell = new Shell(display);

        //Initialize the system tray
        Image imgTrayIcon = new Image(display, Janitor.class.getClassLoader().getResourceAsStream("traytick.ico"));
        Tray trySysTray = display.getSystemTray();
        final Menu mnuPopup = new Menu(shell, SWT.POP_UP);

        itmTrayItem = new TrayItem(trySysTray, SWT.NONE);
        itmTrayItem.setToolTipText("Janitor");

        itmTrayItem.addListener(SWT.MenuDetect, new Listener() {
            public void handleEvent(Event event) {
                mnuPopup.setVisible(true);
            }
        });

        MenuItem mimOpenFolder = new MenuItem(mnuPopup, SWT.PUSH);
        mimOpenFolder.setText(LocalStrings.getText("openFolder"));
        mimOpenFolder.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                MenuEvents.openFolder();
            }
        });

        MenuItem mimLaunchJanitorWebsite = new MenuItem(mnuPopup, SWT.PUSH);
        mimLaunchJanitorWebsite.setText(LocalStrings.getText("launchWebsite"));
        mimLaunchJanitorWebsite.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                MenuEvents.launchWebsite();
            }
        });

        //Separator
        MenuItem mimFirstSeparator = new MenuItem(mnuPopup, SWT.SEPARATOR);

        MenuItem mimRecentlyCleanedDirectories = new MenuItem(mnuPopup, SWT.CASCADE);
        mimRecentlyCleanedDirectories.setText(LocalStrings.getText("directoriesToProcess"));
        mnuRecent = new Menu(mnuPopup);
        mimRecentlyCleanedDirectories.setMenu (mnuRecent);

        //Separator
        MenuItem mimSecondSeparator = new MenuItem(mnuPopup, SWT.SEPARATOR);

        mimStatus = new MenuItem(mnuPopup, SWT.PUSH);
        mimStatus.setText(LocalStrings.getText("idleWaiting"));
        mimStatus.setEnabled(false);

        //Separator
        MenuItem mimThirdSeparator = new MenuItem(mnuPopup, SWT.SEPARATOR);

        MenuItem mimFreshlyProcessedDirectories = new MenuItem(mnuPopup, SWT.CASCADE);
        mimFreshlyProcessedDirectories.setText(LocalStrings.getText("freshlyProcessedDirectories"));
        mnuFresh = new Menu(mnuPopup);
        mimFreshlyProcessedDirectories.setMenu (mnuFresh);

        //Separator
        MenuItem mimFourthSeparator = new MenuItem(mnuPopup, SWT.SEPARATOR);

        MenuItem mimPauseCleaning = new MenuItem(mnuPopup, SWT.PUSH);
        mimPauseCleaning.setText(LocalStrings.getText("recheckFolders"));
        mimPauseCleaning.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                FolderMenu.getMenuItems(Paths.get(System.getProperty("user.home"), "Downloads"));
            }
        });

        MenuItem mimExit = new MenuItem(mnuPopup, SWT.PUSH);
        mimExit.setText(LocalStrings.getText("exitApplication"));
        mimExit.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event) {
                MenuEvents.quitApplication();
            }
        });

        itmTrayItem.addListener(SWT.DefaultSelection, new Listener() {
            public void handleEvent(Event event) {
                MenuEvents.openFolder();
            }
        });

        //Finalize the system tray
        mnuPopup.setDefaultItem(mimOpenFolder);
        itmTrayItem.setImage(imgTrayIcon);
        itmTrayItem.setToolTipText("Janitor");

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
            display.sleep();
        }

        imgTrayIcon.dispose();
        display.dispose();
    }

}