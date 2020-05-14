package general;

import org.apache.log4j.Logger;
import org.junit.Test;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * ClassName FileUnit 自动生成的订单号 投资人账号啊，需要配置文件记录
 * Author weijian
 * DateTime 2020/5/1 12:16 PM
 * Version 1.0
 */
public class FileUnit {
    public static final Logger logger = Logger.getLogger (FileUnit.class);
    public static int count =0;
    public final static String ID = getNumber();
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("yyyyMMdd");
    public static Calendar c;
    public final static int YEAR = Calendar.YEAR;
    public final static int MOTH = Calendar.MONTH;
    public final static int DAY  = Calendar.DAY_OF_MONTH;

   public static String grentReturnCode(PubRes res){

       if(res.isSuccess ()){
           return "0000";
       }

       return res.getFileCode ();
   }
/**
* @Description: BusinessCode
 * @Param: [filename, issuccess, isgrent, code]
* @return: java.lang.String
* @Author: weijian
* @Date: 2020/5/3
*/
   public static String grenrBusinessCode(PubRes res){
        String code = res.getToBeresolvedInfo ().get ("BusinessCode");
       return "1" + code.substring (1,code.length ());

   }

   /**
   * @Description: TAAccountID  5L1001687309
   * @Param: [filename, code, issuccess, isgrent]
   * @return: java.lang.String
   * @Author: weijian
   * @Date: 2020/5/1
   */
   public static String grentTAAccountID(PubRes res){
       // TA code
       String taNo = res.getTANO ();
       // 随机的十位数 所有TA使用一个值

       // 4T2020000001
       return taNo+getNumber ();
   }
    /**
    * @Description: TASerialNO  3870000000008 13位，之后的字符填充空字符  20位 TA确认交易流水号
    * @Param: [filename, code, issuccess, isgrent]
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/1
    */
   public static String TASerialNO_02(PubRes res ){

       // 网点号 3 位
       String BranchCode = res.getToBeresolvedInfo ().get ("BranchCode").trim ();
       // 生成 10 位 所有TA都可以使用这个记录值
       String number = getNumber ();
       // 补7个空字符串
       number += "       ";

       return BranchCode+number;
   }

    /**
     * @Description: TASerialNO  3870000000008 13位，之后的字符填充空字符  20位 TA确认交易流水号
     * @Param: [filename, code, issuccess, isgrent]
     * @return: java.lang.String
     * @Author: weijian
     * @Date: 2020/5/1
     */
    public static String TASerialNO_04(PubRes res ){

        // 网点号 3 位
        String BranchCode = res.getToBeresolvedInfo ().get ("BranchCode").trim ();
        // 生成 10 位 所有TA都可以使用这个记录值
        String number = getNumber ();
        // 补7个空字符串
        number += "       ";

        String ret = BranchCode+number;
        if(res.isTAFlag ()){
            ret = simpleDateFormat.format (new Date ()) +"00" + getNumber ();
        }

        return ret;
    }

   /**
   * @Description: FromTAFlag  0 不是 1 是
   * @Param: [filename, issuccess, isgrent, lineData]
   * @return: java.lang.String
   * @Author: weijian
   * @Date: 2020/5/3
   */
   public static String FromTAFlag(PubRes res){

       return res.isTAFlag () ? "1" : "0";
   }
   /**
   * @Description: TA客户编号 需要入库
   * @Param: []
   * @return: java.lang.String
   * @Author: weijian
   * @Date: 2020/5/3
   */
    public static String CustomerNo(PubRes res){
        String ret = res.getToBeresolvedInfo ().get ("TAAccountID").trim ();
        // 入库之后需要查询库里面是否有这个用户的变化，如果有的话就穿，没有的话默认为空，就是投资人基金账户

       return ret.equals ("") ? "            " : ret;
    }

    /**
     * @Description:确认份额 根据申请金额来定
     * @Param: []
     * @return: java.lang.String
     * @Author: weijian
     * @Date: 2020/5/3
     */
    public static String ConfirmedVol(PubRes res){
        String ApplicationVol = res.getToBeresolvedInfo ().get ("ApplicationVol");
        if(res.isSuccess ()){
            return ApplicationVol;
        }
        return "0000000000000000";
    }
    /**
    * @Description: 5=N,16,2,确认金额,ConfirmedAmount -- ApplicationAmount  有失败情况
    * @Param: [res]
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/4
    */
    public static String ConfirmedAmount(PubRes res){
        String ApplicationAmount = res.getToBeresolvedInfo ().get ("ApplicationAmount");
        if(res.isSuccess ()){
            return ApplicationAmount;
        }
        return "0000000000000000";
    }
    /**
    * @Description: 业务过程完全结束标识 默认返回结束,业务确认之后基本已经完成，直接传 1 完成
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
   public static String BusinessFinishFlag(PubRes res){
       return "1";
    }
    /**
    * @Description: 交易数据下传日期 如果申请日期和执行日期是同一天的话需要+1，如果不是 申请日期+1
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String DownLoadDate(PubRes res){
//        boolean isNow = false;
//        if(!isNow){
//            return simpleDateFormat.format (operDate (new Date (),DAY,1));
//        }
       return simpleDateFormat.format (new Date ());

    }
    /**
    * @Description: 代理费 默认 0
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String AgencyFee(PubRes res){
        return copyChar ("0",10);
    }

    /**
    * @Description: 基金单位净值 下一日净值 TradingPrice
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String NAVAandTradingPrice(PubRes res){
        return grenNav ();
    }
    /**
    * @Description: 其他费用1 基本是 0
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String OtherFee1(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 印花税 基本是 0
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String StampDuty(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 有效天数 0
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String ValidPeriod(PubRes res){
        return "00";
    }
    /**
    * @Description: 费率 可以拿净值文件里面的费率
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String RateFee(PubRes res){
        return "00";
    }
    /**
    * @Description: 后端总收费 上面的值总和 根据费率 * 申请金额
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
//    public static String TotalBackendLoad(PubRes res){
//        return null;
//    }
    /**
    * @Description: TA原确认流水号 基本是 没有，除非是TA下发的数据 方便之后的查询
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String OriginalSerialNo(PubRes res){
        return copyChar (" ",20);
    }
    /**
    * @Description: 摘要，基本是空的
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String Specification(PubRes res){

        return copyChar (" ",60);
    }
    /**
    * @Description: 对方销售人代码 取 DistributorCode
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String TargetDistributorCode(PubRes res){
        String DistributorCode = res.getToBeresolvedInfo ().get ("DistributorCode");
        return DistributorCode;
    }
    /**
    * @Description: 对方网点号 取 BranchCode
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String TargetBranchCode(PubRes res){
        String BranchCode = res.getToBeresolvedInfo ().get ("BranchCode");

        return BranchCode;
    }
    /**
    * @Description: 投资人账号
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/15
    */



    /**
    * @Description: 对方销售人处投资人基金交易帐 取 TransactionAccountID
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String TargetTransactionAccountID(PubRes res){
        String TransactionAccountID = res.getToBeresolvedInfo ().get ("TransactionAccountID");
        logger.info (" 投资人账号 ： --" + TransactionAccountID);
        return TransactionAccountID;
    }
    /**
    * @Description: 对方地区号 取 RegionCode
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String TargetRegionCode(PubRes res){
        String RegionCode = res.getToBeresolvedInfo ().get ("RegionCode");

        return RegionCode;
    }

    /**
    * @Description: 转入/转出标识 转托管业务需要标注
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String TransferDirection(PubRes res){
        return res.getTransferDirection ();
    }

    /**
    * @Description: 基金帐户利息金额  如果是认购状态的话就需要算利息，利息默认指定的值 1% 利率 转份额的话直接也可直接用这个值
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String InterestAndVolumeByInterest(PubRes res){
        String ApplicationAmount = res.getToBeresolvedInfo ().get ("ApplicationAmount");
        double amount = Double.valueOf (ApplicationAmount);
        amount += amount * 0.01;
        String ret = DoubleToString (amount,2);
        return copyChar ("0",16,ret)+ret;
    }
    /**
    * @Description: 利息税 默认返回无
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String InterestTax(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 冻结原因 基本为空
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String FrozenCause(PubRes res){
        return copyChar (" ",1);
    }
    /**
    * @Description: 税金 基本没有
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String Tax(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 目标基金的单位净值 & 目标基金的价格 -自己生成取07文件的
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String TargetNAVAndTargetFundPrice(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 目标基金的确认份额,CfmVolOfTargetFund--自己生成，看是正只基金的还是其他的
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String CfmVolOfTargetFund(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 最少收费 基本无
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String MinFee(PubRes res){
        return copyChar ("0",10);
    }
    /**
    * @Description: 基本无
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String OtherFee2(PubRes res){
        return copyChar ("0",16);
    }
    
    /**
    * @Description:过户费 默认费率 1% 0.01
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String TransferFee(PubRes res){
        return copyChar ("0",10);
    }
    /**
    * @Description: 数据明细标志， 看数据是否存库，如果存库的话就直接取数据库里面的数据，如果不存库，就没办法生成了
     * 一般 数据不是明细
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String DetailFlag(PubRes res){
        return "0";
    }
    /**
    * @Description: 预约赎回标志,RedemptionInAdvanceFlag--自己生成产品到期，券商业务
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String RedemptionInAdvanceFlag(PubRes res){
        return " ";
    }
    /**
    * @Description: 冻结方式 不明确
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String FrozenMethod(PubRes res){
        return " ";
    }
    /**
    * @Description: 赎回原因,RedemptionReason--自己生成，强赎时 基本无
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String RedemptionReason(PubRes res){
        return " ";
    }
    
    /**
    * @Description: 转换基金代码,CodeOfTargetFund--基金转换业务
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String CodeOfTargetFund(PubRes res){
        return res.getToBeresolvedInfo ().get ("CodeOfTargetFund");
    }
    /**
    * @Description: 交易确认费用合计,TotalTransFee--自己生成，算申请金额加费用
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String TotalTransFee(PubRes res){
        return copyChar ("0",10);
    }
    /**
    * @Description: 默认返回单天日期
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String AlternationDate(PubRes res){
        return simpleDateFormat.format (new Date ());
    }
    /**
    * @Description: 退款金额 退款金额 认购申购失败时？或者是赎回时？
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String RefundAmount(PubRes res){
        if(res.isSuccess()){
            return res.getToBeresolvedInfo ().get ("ApplicationAmount");
        }
        return copyChar ("0",16);
    }
    /**
    * @Description: 配售比例 看返回单笔的还是总的，总的话返回 20% 0.2
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String SalePercent(PubRes res){
        return copyChar ("0",8);
    }
    /**
    * @Description: 转换费 基金转换费用，费率默认 0.01
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String ChangeFee(PubRes res){

        return copyChar ("0",16);
    }

    /**
     * 巨额赎回
     * @param res
     * @return
     */
    public static String LargeRedemptionFlag(PubRes res){
        return "0";
    }

    /**
    * @Description: 如果你一定要买这么多份额的话就要补差，基本 0
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String RecuperateFee(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 业绩报酬，巨额赎回时下发业绩报酬 默认0.01
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String AchievementPay(PubRes res){
        String LargeRedemptionFlag = res.getToBeresolvedInfo ().get ("LargeRedemptionFlag");
        if(LargeRedemptionFlag.equals ("0")){
            double amount = Double.valueOf (res.getToBeresolvedInfo ().get ("ConfirmedAmount"));
            amount += amount * 0.01;
            String ret = DoubleToString (amount,2);
            return copyChar ("0",16,ret)+ret;
        }
        return copyChar ("0",16);
    }
    /**
    * @Description: 业绩补偿 待确认
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String AchievementCompen(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 份额强制调整标识 基本 无
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String SharesAdjustmentFlag(PubRes res){
        return "0";
    }
    /**
    * @Description: TA确认总流水号，待确认
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String GeneralTASerialNO(PubRes res){

        return copyChar (" ",20);
    }
    /**
    * @Description:货币基金未分配收益金额 待确认
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String UndistributeMonetaryIncome(PubRes res){
        return "0";
    }
    /**
    * @Description:货币基金未分配收益金额正 待确认
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String UndistributeMonetaryIncomeFlag(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 违约金,BreachFee--自己生成，认购时违约？
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String BreachFee(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 违约金归基金资产金额,BreachFeeBackToFund--自己生成  基本无
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String BreachFeeBackToFund(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 惩罚性收费，基本无
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String PunishFee(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 转换代理费 基本无
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String ChangeAgencyFee(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 补差代理费,RecuperateAgencyFee--自己生成 基本无
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String RecuperateAgencyFee(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 出错详细信息 基本无
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String ErrorDetail (PubRes res){

        return copyChar (" ",60);
    }
    
    /**
    * @Description: 巨额购买 基本无
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String LargeBuyFlag(PubRes res){
        int amount = Integer.valueOf (res.getToBeresolvedInfo ().get ("ApplicationAmount"));
        if (amount> 10000000){
            return "1";
        }
        return "0";
    }
    /**
    * @Description: 认购期间利息 默认 0.01
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String RaiseInterest(PubRes res){
        String ApplicationAmount = res.getToBeresolvedInfo ().get ("ApplicationAmount");
        ApplicationAmount = ApplicationAmount.substring (0,ApplicationAmount.length ()-2) + "." + ApplicationAmount.substring (ApplicationAmount.length ()-2,ApplicationAmount.length ());
        double amount = Double.valueOf (ApplicationAmount);
        logger.info (amount);
        amount += amount * 0.01;
        logger.info (amount);
        String ret = DoubleToString (amount,2);
        return copyChar ("0",16,ret)+ret;
    }
    
    /**
    * @Description: 计费人 默认无 0
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String FeeCalculator(PubRes res){
        return copyChar (" ",1 );
    }
    /**
    * @Description: 份额注册日期
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String ShareRegisterDate(PubRes res){
        return simpleDateFormat.format (new Date ());
    }
    /**
    * @Description: 基金份数 待确认
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String FundVolBalance(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 清算资金经清算人划出日 待确认
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String TransferDateThroughClearingAgency(PubRes res){
        return copyChar ("0",8);
    }
    /**
    * @Description: 基金冻结总份数 待确认
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String TotalFrozenVol(PubRes res){
        return copyChar ("0",16);
    }
    /**
    * @Description: 冻结金额 冻结份数 * 当天净值
    * @Param: []
    * @return: java.lang.String
    * @Author: weijian
    * @Date: 2020/5/3
    */
    public static String FrozenBalance(PubRes res){
        return copyChar ("0",16);
    }

    

    /*
     * 总条数
     * @return
     */
   public final static String getNumber(){
       String s = String.valueOf (count++),ret = "";
       int len = s.length ();

       for (int i = 0; i <(10 - len); i++)
           ret +=0;
       return ret + s;
   }

   public static Date operDate(Date date,int type,int val){
       c = Calendar.getInstance ();
       c.setTime (date);
       c.set (type,val);
       date = c.getTime ();
       return date;
   }

    /**
     * 传入申请文件03数据 直接获取总的
     * @param
     * @return
     */
   public static String grenNav(){
       // 调用获取净值的方法 ， 这个净值某种手段传过来的
       String nav = copyChar ("0",7);
       //获取净值，如果是空的话就执行生成
       if(nav != null && !nav.equals ("")){
           return nav;
       }
       // 生成的规则是： 看今天的申请业务代码是赎回多还是申购多，1 笔申购 净值 + 0.01 一笔赎回 -0.01 根据03文件的数据生成

       return nav;
   }

    public static String grenRateFee(){
        // 调用获取净值的方法 ， 这个净值某种手段传过来的
        String nav = "";
        //获取净值，如果是空的话就执行生成
        if(nav != null && !nav.equals ("")){
            return nav;
        }
        // 生成的规则是： 看今天的申请业务代码是赎回多还是申购多，1 笔申购 净值 + 0.01 一笔赎回 -0.01 根据03文件的数据生成
        return nav;
    }


    public static Integer StringforInt(String value){
        return Integer.valueOf (value);
    }

    public static String DoubleToString(Double val, int bit){
       String s = String.valueOf (val);
       if(bit > s.length () || s.startsWith ("\\.")) return null;
//       int position = val.indexOf (".");
       String[] str = s.split ("\\.")[1].split ("");
       String ret = s.split ("\\.")[0];
       if(str.length<=bit) return s.replaceAll ("\\.","")+"0";
       for(int i = 0; i < str.length; i++){
           if (i+1 == bit){
               int c = Integer.valueOf (str[i+1]);
               ret += c > 5 ? Integer.valueOf (str[i])+1 : str[i];
               break;
           }
           ret += str[i];
       }
        logger.info (ret);

       return ret;
    }

    @Test
    public void TestStringstr(){
       logger.info (DoubleToString ( 10100.0,1));
    }

    public static String copyChar(String val, int len){
       String ret = "";
       for (int i = 0 ; i < len;i++){
           ret += val;
       }

       return ret;
    }

    public static String copyChar(String val, int len, String difference){
       if(difference != null ){
           len = len - difference.length ();
       }


        return copyChar (val,len);
    }

}
