/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.koolsource.KSSM.Includes;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author john
 */
public class WebReader {

    public final int POST = 1;
    public final int GET = 0;
    private HashMap<String, String> params = new HashMap<String, String>();
    private String address = null;
    private String response;
    private int type;

    public void makeRequest() {
        if (address == null) {
            LogWriter.error("No URL specified");
            this.response = null;
            return;
        }
        try {
            Set keys = params.keySet();
            Iterator keyIter = keys.iterator();
            String data = "";
            for (int i = 0; keyIter.hasNext(); i++) {
                Object key = keyIter.next();
                if (i != 0) {
                    data += "&";
                }
                data += key + "=" + URLEncoder.encode(params.get(key), "UTF-8");
            }

            URL url = new URL(address);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(1000);
            con.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

            wr.write(data);
            wr.flush();

            Pattern p = Pattern.compile("text/plain;\\s+charset=([^\\s]+)\\s*");
            Matcher m = p.matcher(con.getContentType());
            String charset = m.matches() ? m.group(1) : "ISO-8859-1";

            Reader r = new InputStreamReader(con.getInputStream(), charset);
            StringBuilder buf = new StringBuilder();

            while (true) {
                int ch = r.read();
                if (ch < 0) {
                    break;
                }
                buf.append((char) ch);
            }

            this.response = buf.toString();
            LogWriter.debug("-- Start Web Data --");
            LogWriter.debug(this.getResponse());
            LogWriter.debug("-- End Web Data --");

        } catch (Exception e) {
            LogWriter.error("Error fetching URL data: Reason: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean getBoolean() {
        return Boolean.parseBoolean(response);
    }

    public String getResponse() {
        return this.response;
    }

    public Object getYaml() {
        try {
            Yaml yaml = new Yaml();
            @SuppressWarnings("unchecked")
            Object retval = yaml.load(this.response);
            return retval;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void setURL(String url) {
        if (urlValid(url)) {
            this.address = url;
        } else {
            LogWriter.error("Malformed URL");
        }
    }

    public void setType(int anInt) {
        if (anInt == POST || anInt == GET) {
            this.type = anInt;
        }
    }

    public boolean setParam(String key, String value, Boolean overWrite) {
        if (params.containsKey(key)) {
            if (overWrite == false) {
                return false;
            } else {
                params.remove(key);
                params.put(key, value);
                return true;
            }
        } else {
            params.put(key, value);
            return true;
        }
    }

    public boolean setParam(String key, String value) {
        return setParam(key, value, false);
    }

    private boolean urlValid(String url) {
        return true;
    }
}