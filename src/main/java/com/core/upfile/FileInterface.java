package com.core.upfile;

import java.io.IOException;

public interface FileInterface {
    public void setMaxSize(int size);
    public void setSuffixlimit(String[] suffixlimit);
    public void setSavePath(String path);
    public boolean move_uploaded(byte[] filecontent,String filename) throws IOException;
}
