package com.mridang.downbox.helpers;
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
import java.util.Arrays;
import java.util.BitSet;

/**
 * Class to check file header to determine if it is a archive
 */
public class ArchiveChecker {

    /*
     * Checks whether a file is an archive
     *
     * @param   filFile  the file to check
     * @returns          a boolean value indicating the result
     */
    public static Boolean isArchive(File filFile) {

        try (FileInputStream fisFileInputStream = new FileInputStream(filFile)) {

            byte[] bytSignature = new byte[] {0x52, 0x61, 0x72, 0x21, 0x1a, 0x07, 0x00};
            byte[] bytHeader = new byte[7];
            Integer intNumberofBytesRead = fisFileInputStream.read(bytHeader);

            if (Arrays.equals(bytHeader, bytSignature)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

    /*
     * Checks whether a file is the first file in a spanned archive
     *
     * @param   filFile  the archive to check
     * @returns          a boolean value indicating the result
     */
    public static Boolean isFirstFileInSpannedArchive(File filFile) {

        try (FileInputStream fisFileInputStream = new FileInputStream(filFile)) {

            byte[] bytHeader = new byte[12];
            Integer intNumberofBytesRead = fisFileInputStream.read(bytHeader);
            BitSet bitFlags = BitSet.valueOf(new byte[]{bytHeader[10], bytHeader[11]});

            if (bitFlags.get(8)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }

}