package com.sqindia.srinarapp.Utils;

/**
 * Created by Salman on 22-03-2017.
 */
public class Config {
    String Localpath="http://192.168.1.17/";
    public static String SQ_Loal="http://192.168.1.18:4010/admin/";
    public static String SQ_Cloud="http://104.197.80.225:4030/admin/";
    public static String srinarlocalpath="http://192.168.1.2:4030/admin/";
    public static String currentPath="http://192.168.1.2:4030/admin/";


    public static final String WEB_URL_LOGIN = currentPath+"login";
    public static final String WEB_URL_ADD_MACHINE = currentPath+"addmachine";
    public static final String WEB_URL_VIEW_MACHINE = currentPath+"getmachines";
    public static final String WEB_URL_ADD_OPERATOR = currentPath+"addoperator";
    public static final String WEB_URL_VIEW_OPERATOR_MACHINE = currentPath+"getmachineoperators";
    public static final String WEB_URL_VIEW_OPERATOR_PLATE = currentPath+"getelectroplatingoperators";
    public static final String WEB_URL_VIEW_OPERATOR_ASSEMBLING = currentPath+"getassemblingoperators";
    public static final String WEB_URL_VIEW_OPERATOR = currentPath+"getoperators";
    public static final String WEB_URL_MAIN_PART = currentPath+"getmainparts";
    public static final String WEB_URL_SUB_PART = currentPath+"getsubpartsbymainpart";
    public static final String WEB_URL_EXCEL_UPLOAD = currentPath+"uploadxl";
    public static final String WEB_URL_MACHINE_EXCEL = currentPath+"addmachinexl";
    public static final String WEB_URL_OPERATOR_EXCEL = currentPath+"addoperatorxl";
    public static final String WEB_URL_DELETE_OPERATOR = currentPath+"deleteoperator";
    public static final String WEB_URL_VIEW_PARTS = currentPath+"getallparts";
    public static final String WEB_URL_FILTER_PARTS=currentPath+"searchparts";
    public static final String WEB_URL_ADD_SINGLE_PARTS = currentPath+"addpart";
    public static final String WEB_URL_UPDATE_SINGLE_PARTS = currentPath+"editpart";
    public static final String WEB_URL_CALL_DISPATCH_MAINPARTS = currentPath+"getmainpartsfordispatch";
    public static final String WEB_URL_GET_DISPATCH_BY_MAINPARTS = currentPath+"getdispatchdetailsbymainpart";
    public static final String WEB_URL_GET_DISPATCH = currentPath+"dispatchentry";
    public static final String WEB_URL_GET_EXCEL_UPLOAD = currentPath+"generatexlreport";
    public static final String WEB_URL_GET_QC_LIST = currentPath+"getqclogsbypartid";
    public static final String WEB_URL_OC_MAINPART = currentPath+"getmainpartsforqc";
    public static final String WEB_URL_GET_SUBPARTS_BY_MAIN_PARTS_NEW = currentPath+"getsubpartsbymainpartnew";
    public static final String WEB_URL_GROUPING_SUBPART = currentPath+"getsubpartsvaluebymainpart";


    //Report
    public static final String WEB_URL_STATUS_REPORT = currentPath + "getcustomreportbymainpart";
    public static final String WEB_URL_MANUAL_REPORT = currentPath+"getmonthwisereportbymainpart";
    public static final String WEB_URL_MONTH_REPORT = currentPath+"getmonthlyreportbymainpart";


    //@@@@@@@@@@@ MACHINING MODULE
    public static final String WEB_URL_COMPLETE_MACHINE_WORK = currentPath+"machineworkcompletion";
    public static final String WEB_URL_GET_MACHINE_ENTRIES = currentPath+"getallmachineentries?limit=25&offset=";
    public static final String WEB_URL_MACHINE_APPROVAL = currentPath+"machineqtyapprovalnew";
    public static final String WEB_URL_DELETE_MACHINE_WORK = currentPath+"deletemachineworkcompletion";

    //@@@@@@@@@@@ ELECTROPLATE MODULE
    public static final String WEB_URL_ALLOCATE_ELECTROPLATE_WORK = currentPath+"electroplatingworkcompletion";
    public static final String WEB_URL_GET_ELECTROPLATE_WORK = currentPath+"getallelectroplatingentries?limit=25&offset=";
    public static final String WEB_URL_ELECTROPLATE_APPROVAL = currentPath+"electroplatingqtyapprovalnew";
    public static final String WEB_URL_DELETE_PLATE_WORK = currentPath+"deleteelectroplatingworkcompletion";

    //@@@@@@@@@@@ ASSEMBLING MODULE
    public static final String WEB_URL_ALLOCATE_ASSEMBLING_WORK = currentPath+"assemblecompletion";
    public static final String WEB_URL_GET_ASSEMBLING_WORK = currentPath+"getallassemblingentries?limit=25&offset=";
    public static final String WEB_URL_ASSEMBLING_APPROVAL = currentPath+"assemblingapprovalnew";
    public static final String WEB_URL_DELETE_ASSEMBLE_WORK = currentPath+"deleteassemblecompletion";


    public static boolean isStringNullOrWhiteSpace(String value){
        if (value == null) {
            return true;
        }

        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
