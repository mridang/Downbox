package com.mridang.downbox.threads.backend.rename.lookups;

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
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.mridang.downbox.threads.backend.rename.BestMatch;

public class MovieSearcher {

   /**
    * This fetches the best match from AllMovie.
    *
    * @return
    */
    public final static BestMatch getBestMatch(String strTitle) {

        try {

			String strURL = "http://www.allmovie.com/search/movies/" + URLEncoder.encode(strTitle, "utf-8");
            Document objDocument = Jsoup.connect(strURL).get();

            for (Element objResult : objDocument.select("ul[class=results]").select("li")) {

                if (objResult.select("img").attr("src").equals("")) {
                    return new BestMatch(objResult.select("dt").select("a").text(), null);
                } else {
                    return new BestMatch(objResult.select("dt").select("a").text(), objResult.select("img").attr("src"));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

}