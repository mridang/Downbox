package com.mridang.downbox.threads.backend.subtitles;
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
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.mridang.downbox.helpers.FileExtention;

/**
 * Class containing methods for reading and writing the list of ignored and
 * processed folders for internal use.
 */
public class OpenSubtitles {

   /** The logger instance that will provide all the logger functionality
    */
    private static Logger logger = Logger.getLogger("downbox");
   /** The API url of the server
    */
    private static String OSDB_SERVER = "http://api.opensubtitles.org/xml-rpc";
   /** The token returned by the server
    */
    private static String strToken = null;

   /**
    * Login to the server
    */
    private static void logIn() throws Exception {

       logger.info("Logging in to server...");

        XmlRpcClientConfigImpl rpcConfig = new XmlRpcClientConfigImpl();
        rpcConfig.setServerURL(new URL(OSDB_SERVER));
        XmlRpcClient rpcClient = new XmlRpcClient();
        rpcClient.setConfig(rpcConfig);
        Object[] objParams = new Object[]{"", "", "eng", "moviejukebox 1.0.15"};
        HashMap<?, ?> mapResult = (HashMap<?, ?>) rpcClient.execute("LogIn", objParams);
        strToken = (String) mapResult.get("token");

       logger.fine("Done.");

    }

   /**
    * Searches for a subtitle
    *
    * @param   filVideoFile   the video file which to download subtitles
    */
    public static void searchAndDownloadSubtitles(File filVideoFile) throws Exception {

        //Let's log in
        try {

            logIn();

        } catch (Exception e)  {
            throw e;
        }


        //Let's generate the hash of the movie file. This is a custom hashing
        //algorithm used by OpenSubtitles.
        String strHash = null;

        try (
		       FileInputStream fisInputStream = new FileInputStream(filVideoFile);
    		   FileChannel fchFileChannel = fisInputStream.getChannel())
    		   {

           logger.info(String.format("Hashing the video file: %s", filVideoFile.getName()));

            Long lngFileSize = fchFileChannel.size();

            if (fchFileChannel.size() < 65536) {
                logger.finest("The file is too small to be hashed");
            }

            MappedByteBuffer mbbByteBuffer;
            Long lngSumOfBytes = lngFileSize;

            mbbByteBuffer = fchFileChannel.map(FileChannel.MapMode.READ_ONLY, 0, 65536);
            mbbByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            for (Integer i = 0; i < 65536 / 8; i++) {
                lngSumOfBytes += mbbByteBuffer.getLong();
            }

            mbbByteBuffer = fchFileChannel.map(FileChannel.MapMode.READ_ONLY, lngFileSize - 65536, 65536);
            mbbByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            for (Integer i = 0; i < 65536 / 8; i++) {
                lngSumOfBytes += mbbByteBuffer.getLong();
            }
            strHash = String.format("%016x", lngSumOfBytes & 0xffffffffffffffffL);

            logger.fine("Hashed.");

        }
        catch (Exception e) {
           logger.warning(String.format("Error: %s", e.toString()));
        }


        // Let's search for matching subtitles
        URL urlSubtitle = null;

        try {

           logger.info(String.format("Searching for subtiles: %s, %s", strHash, filVideoFile.length()));

            XmlRpcClientConfigImpl rpcConfig = new XmlRpcClientConfigImpl();
            rpcConfig.setServerURL(new URL(OSDB_SERVER));
            XmlRpcClient rpcClient = new XmlRpcClient();
            rpcClient.setConfig(rpcConfig);

            Map<String, Object> mapQuery = new HashMap<String, Object>();
            mapQuery.put("sublanguageid", Locale.getDefault().getISO3Language());
            mapQuery.put("moviehash", new String("18379ac9af039390"));
            mapQuery.put("moviebytesize", new Double(366876694));

            Object[] objParams = new Object[]{strToken, new Object[]{mapQuery}};
            HashMap<?, ?> x = (HashMap<?, ?>) rpcClient.execute("SearchSubtitles", objParams);
            Object[] lstData = (Object[]) x.get("data");
            HashMap<?, ?> mapResult = (HashMap<?, ?>) lstData[0];

            urlSubtitle = new URL((String) mapResult.get("SubDownloadLink"));

           logger.fine("Done.");

        } catch (Exception e)  {
           logger.warning(String.format("Error: %s", e.toString()));
        }


        // Now that we have the URL, we can download the file. The file is in
        // the GZIP format so we have to uncompress it.
        File filSubtitleFile = new File(filVideoFile.getPath().substring(0, filVideoFile.getPath().length() - 4));

        HttpURLConnection objConnection = null;
        FileOutputStream objOutputStream = null;
        GZIPInputStream objGzipInputStream = null;

        try {

           objConnection = (HttpURLConnection)((urlSubtitle).openConnection());
           objOutputStream = new FileOutputStream(filSubtitleFile);
           objGzipInputStream = new GZIPInputStream(objConnection.getInputStream());

            logger.info(String.format("Downloading the subtitle: %s", urlSubtitle));

            if (objConnection.getResponseCode() != 200) {
                logger.finest("The server did not respond properly");
            }

            Integer intLength = 0;
            byte[] bytBuffer = new byte[1024];

            objOutputStream.close();
            filSubtitleFile.delete();
            if (objConnection.getHeaderField("Content-Disposition").isEmpty() == false) {
                filSubtitleFile = new File(filSubtitleFile.getPath() + "." + FileExtention.getExtention(objConnection));
            }

            objOutputStream.close();
            objOutputStream = new FileOutputStream(filSubtitleFile);
            while ((intLength = objGzipInputStream.read(bytBuffer)) > 0) {
                objOutputStream.write(bytBuffer, 0, intLength);
            }
            objConnection.disconnect();

           logger.fine("Downloaded.");

        } catch (Exception e) {
           logger.warning(String.format("Error: %s", e.toString()));
        } finally {
            objOutputStream.close();
            objGzipInputStream.close();
        }


        //Let's log out
        try {

            logOut();

        } catch (Exception e)  {
            return;
        }

    }


   /**
    * Logout from the the server
    */
    private static void logOut() throws Exception {

       logger.info("Logging out of server...");

        XmlRpcClientConfigImpl rpcConfig = new XmlRpcClientConfigImpl();
        rpcConfig.setServerURL(new URL(OSDB_SERVER));
        XmlRpcClient rpcClient = new XmlRpcClient();
        rpcClient.setConfig(rpcConfig);
        Object[] objParams = new Object[]{strToken};
        rpcClient.execute("LogOut", objParams);
        strToken = null;

       logger.fine("Done.");

    }

}