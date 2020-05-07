package general;

import java.util.List;
import java.util.Map;

/**
 * ClassName PubRes
 * Author weijian
 * DateTime 2020/5/3 9:46 PM
 * Version 1.0
 */
public class PubRes {
    // 文件名字
    private String filename;
    //TA编码
    private String TANO;
    // 是否需要生成
    private String isgrent;
    // 解析的 申请 文件的值
    private Map<String,String> toBeresolvedInfo;
    // 是否成功标识
    private boolean isSuccess = true;
    // 是否是TA发起业务标识
    private boolean isTAFlag = false;
    // 文件代码
    private String fileCode = "";
    // 转入/转出标识
        private String  transferDirection = "";

    public PubRes() {
    }

    public PubRes(String filename, String TANO, String isgrent, Map<String, String> toBeresolvedInfo, boolean isSuccess, boolean isTAFlag, String fileCode, String transferDirection) {
        this.filename = filename;
        this.TANO = TANO;
        this.isgrent = isgrent;
        this.toBeresolvedInfo = toBeresolvedInfo;
        this.isSuccess = isSuccess;
        this.isTAFlag = isTAFlag;
        this.fileCode = fileCode;
     this.transferDirection = transferDirection;
    }

    public String getTransferDirection() {
        return this.transferDirection;
    }

    public void setTransferDirection(String transferDirection) {
        this.transferDirection = transferDirection;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public boolean isTAFlag() {
        return isTAFlag;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setTAFlag(boolean TAFlag) {
        isTAFlag = TAFlag;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }


    public String getTANO() {
        return TANO;
    }

    public void setTANO(String TANO) {
        this.TANO = TANO;
    }

    public String getIsgrent() {
        return isgrent;
    }

    public void setIsgrent(String isgrent) {
        this.isgrent = isgrent;
    }

    public Map<String, String> getToBeresolvedInfo() {
        return toBeresolvedInfo;
    }

    public void setToBeresolvedInfo(Map<String, String> toBeresolvedInfo) {
        this.toBeresolvedInfo = toBeresolvedInfo;
    }
}
