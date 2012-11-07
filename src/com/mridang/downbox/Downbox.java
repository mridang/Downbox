package com.mridang.downbox;

import java.nio.file.Paths;

import org.apache.commons.lang.StringUtils;

import com.mridang.downbox.helpers.ExceptionHandler;
import com.mridang.downbox.threads.ApplicationData;
import com.mridang.downbox.threads.ArgumentParser;
import com.mridang.downbox.threads.DirectoryMonitor;
import com.mridang.downbox.threads.FolderDatabase;
import com.mridang.downbox.threads.frontend.SysTray;
import com.mridang.downbox.yooayes.FolderMenu;

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

public class Downbox {

	/**
	 * The system tray class
	 */
	private static SysTray guiTray;

	/**
	 * The main procedure that is called when running the application
	 *
	 * @param strArguments
	 *            the line of arguments passed to the application
	 */
	public static void main(String[] strArguments) {

		ApplicationData.loadData();
		FolderDatabase.loadData();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}

		if (strArguments.length != 0) {

			guiTray = new SysTray();

			try {
				guiTray.start();
				Thread.sleep(1000);
			} catch (Exception e) {
				ExceptionHandler.saveError(e);
			}

			FolderMenu.getMenuItems(Paths.get(System.getProperty("user.home"),
					"Downloads")); // TODO: This should be a new thread

			ArgumentParser argParser = new ArgumentParser();

			try {
				argParser.run(StringUtils.join(strArguments));
				Thread.sleep(1000);
			} catch (Exception e) {
				ExceptionHandler.saveError(e);
			}

		} else {

			guiTray = new SysTray();

			try {
				guiTray.start();
				Thread.sleep(1000);
			} catch (Exception e) {
				ExceptionHandler.saveError(e);
				e.printStackTrace();
			}

			FolderMenu.getMenuItems(Paths.get(System.getProperty("user.home"),
					"Downloads")); // TODO: This should be a new thread

			DirectoryMonitor bakWatcher = new DirectoryMonitor();

			try {
				bakWatcher.start();
				Thread.sleep(1000);
			} catch (Exception e) {
				ExceptionHandler.saveError(e);
			}

		}

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}

		FolderDatabase.saveData();
		ApplicationData.saveData();

	}

}