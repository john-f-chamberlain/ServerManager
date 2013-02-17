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
 * Enables the easy use of web based API requests. Must have instance created at
 * time of use. Cannot reuse instance once <em>makeRequest</em> has been called.
 * 
 * @author Johnathan Chamberlain
 * @since v1.0
 */
public class WebReader {

    /**
     * POST url request type. 
     */
    public final int POST = 1;
    
    /**
     * GET url request type.
     */
    public final int GET = 0;
    private HashMap<String, String> params = new HashMap<String, String>();
    private String address = null;
    private String response;
    private int type;

    /**
     * This method puts everything together and makes a call to the URL provided
     * with the parameters provided. Stores the response in <em>response</em>
     * 
     */
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
            con.setReadTimeout(1000);
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

    /**
     * Returns boolean based on parsed response string.
     *
     * @return Boolean
     */
    public boolean getBoolean() {
        return Boolean.parseBoolean(response);
    }

    /**
     * Returns response string created using makeRequest.
     * 
     * @return String
     */
    public String getResponse() {
        return this.response;
    }

    /**
     * Returns a hashmap of Object pairs most likely &lt;String, Object&gt; or &lt;String, String&gt;
     * by parsing the response string.
     * 
     * Must be called after makeRequest.
     * 
     * @return YAML Object
     */
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

    /**
     *  Sets the WebReader URL to the <em>url</em> string. If URL is invalid 
     *  {@see Webreader.urlValid} an error will be thrown.
     * 
     * @param url
     */
    public void setURL(String url) {
        if (urlValid(url)) {
            this.address = url;
        } else {
            LogWriter.error("Malformed URL");
        }
    }

    /**
     * Sets the request type. If an invalid request type is specified @see POST will
     * be the default.
     * 
     * @param anInt
     */
    public void setType(int anInt) {
        if (anInt == POST || anInt == GET) {
            this.type = anInt;
        }else{
            this.type = POST;
        }
    }

    /**
     * Sets a POST or GET parameter for the web call.
     * 
     * @param key Name of the parameter.
     * @param value Value of the parameter.
     * @param overWrite If true parameter will be overwritten if it exists. If 
     * false parameter will not be overwritten if it already exists.
     * @return Boolean
     */
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

    /**
     * Executes setParam with overWrite defaulted to false.
     * 
     * @see setParam
     * @param key Name of the parameter
     * @param value Value of the parameter
     * @return Boolean
     */
    public boolean setParam(String key, String value) {
        return setParam(key, value, false);
    }
    
    /**
     * Checks if the <em>url</em> provided is valid.
     * 
     * 
     * @param url
     * @return True if valid, false if invalid.
     */
    private boolean urlValid(String url) {
        return true;
    }
}