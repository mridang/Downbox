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
import java.io.*;
import java.nio.file.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.*;
import org.eclipse.swt.program.Program;

public class MenuEvents {

   /**
    * This launches the website.
    */
    public static void launchWebsite() {

        Program.launch("http://mridang.github.com/projects/downbox/");

    }


   /**
    * This starts the backend processor to proecess a directory
    *
    * @param   evtEvent   the event that triggered this function
    */
    public static void processDirectory(final String strDirectory) {

        MessageBox objPrompt = new MessageBox(SysTray.shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO | SWT.CANCEL);
        objPrompt.setMessage(LocalStrings.getText("processPromtQuestion"));
        objPrompt.setText(LocalStrings.getText("processPromtTitle"));

        Integer intResponse = objPrompt.open();

        if (intResponse == SWT.NO) {
            FolderMenu.delMenuItem(strDirectory);
        }
        if (intResponse == SWT.YES) {

            new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            BackendProcessor.processDirectory(new File(Paths.get(System.getProperty("user.home"), "Downloads").toFile(), strDirectory));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            ).start();
        }

    }


   /**
    * This quits the application
    */
    public static void quitApplication() {

        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                try {

                    SysTray.shell.getDisplay().dispose();
                    SysTray.shell.dispose();
                    System.exit(0);

                } catch (Exception e) {
                    return;
                }

            }

        });

    }


   /**
    * This rescans a directory for any new files
    */
    public static void recheckDirectory() {

        MessageBox objPrompt = new MessageBox(SysTray.shell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
        objPrompt.setMessage(LocalStrings.getText("recheckPromtQuestion"));
        objPrompt.setText(LocalStrings.getText("recheckPromtTitle"));

        Integer intResponse = objPrompt.open();

        if (intResponse == SWT.CANCEL) {
            //FolderMenu.delMenuItem(((MenuItem) event.widget).getText());
        }
        if (intResponse == SWT.OK) {
            new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            ).start();
        }
    }


   /**
    * This opens the folder.
    */
    public static void openFolder() {

        Program.launch(Paths.get(System.getProperty("user.home"), "Downloads").toString());

    }

}