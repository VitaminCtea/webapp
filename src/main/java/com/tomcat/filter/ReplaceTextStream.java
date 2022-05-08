package com.tomcat.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

public class ReplaceTextStream extends ServletOutputStream {
    private ServletOutputStream servletOutputStream;
    private ByteArrayOutputStream byteArrayOutputStream;
    private boolean closed;
    private volatile String replaceContent;

    private String searchString;
    private String replaceString;
    private Pattern pattern = Pattern.compile("&#\\d{5};");

    public ReplaceTextStream(ServletOutputStream servletOutputStream, String searchString, String replaceString) {
        this.servletOutputStream = servletOutputStream;
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.searchString = searchString;
        this.replaceString = replaceString;
    }

    // setWriteListener和isReady方法在支持非阻塞IO的情况下才需要，在本范例中并不支持非阻塞IO，因此没有真正实现它们
    @Override public boolean isReady() { return false; }
    @Override public void setWriteListener(WriteListener writeListener) {}

    @Override public void write(int b) { byteArrayOutputStream.write(b); }

    @Override public void println(String s) throws IOException {
        if (s.startsWith("<p>") && s.contains("的留言为：")) replaceContent = s;
        s += "\n";
        byte[] bytes = s.getBytes();
        byteArrayOutputStream.write(bytes);
    }

    @Override public void close() throws IOException {
        if (!closed) {
            closed = true;
            processStream();
            servletOutputStream.close();
            byteArrayOutputStream.close();

            servletOutputStream = null;
            byteArrayOutputStream = null;

            pattern = null;
            searchString = null;
            replaceString = null;
        }
    }

    @Override public void flush() throws IOException {
        if (byteArrayOutputStream.size() != 0 && !closed) {
            processStream();
            byteArrayOutputStream = new ByteArrayOutputStream();
            replaceContent = null;
        }
    }

    private void processStream() throws IOException {
        servletOutputStream.write(replaceContent(byteArrayOutputStream.toByteArray()));
        servletOutputStream.flush();
    }

    private byte[] replaceContent(byte[] bytes) {
        String newStr = findNeedReplaceContent(new String(bytes));
        if (newStr != null) return newStr.replaceAll(searchString, replaceString).getBytes();
        return bytes;
    }

    // 留言如果有中文的话会出现&#12345;这样的特殊字符，为了得到中文，必须把数字部分转为char类型
    // 还需要考虑中文和其他进行混合，比如中文+英文，全是中文等等
    private String findNeedReplaceContent(String str) {
        StringBuilder keywords = new StringBuilder();
        String newReplaceContent = replaceContent;
        final String SPECIAL_STRING = "&#";
        int index;

        if (newReplaceContent != null && (index = newReplaceContent.indexOf(SPECIAL_STRING)) != -1) {
            String desc = newReplaceContent.substring(newReplaceContent.indexOf(">") + 1, index);
            newReplaceContent = newReplaceContent.substring(index, newReplaceContent.lastIndexOf("</p>"));
            index = 0;
            keywords.append(desc);
            int length = newReplaceContent.length();
            for (int i = index; i < length;) {
                int oneWordPosition;
                if (pattern.matcher(newReplaceContent.substring(i, Math.min(oneWordPosition = i + 8, length))).matches()) {
                    keywords.append((char) Integer.parseInt(newReplaceContent.substring(i + 2, oneWordPosition - 1)));
                } else {
                    oneWordPosition = newReplaceContent.indexOf(SPECIAL_STRING, i);
                    if (oneWordPosition == -1) {
                        keywords.append(newReplaceContent.substring(i));
                        break;
                    }
                    keywords.append(newReplaceContent, i, oneWordPosition);
                }
                i = oneWordPosition;
            }

            if (keywords.toString().contains(searchString)) return str.replaceAll(replaceContent, "<p>" + keywords + "</p>");
        }

        return null;
    }
}
