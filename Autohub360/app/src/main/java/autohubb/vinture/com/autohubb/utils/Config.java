package autohubb.vinture.com.autohubb.utils;

/**
 * Created by Salman on 22-03-2017.
 */
public class Config{

    public static String serverpath="http://104.197.80.225/Autohub/";
    public static String localpath="http://192.168.1.10/Autohub/";
    public static String currentpath="http://104.197.80.225/Autohub/";

    //User Module:
    public static final String WEB_URL_REGISTER = currentpath+"auth/register";
    public static final String WEB_URL_EMAIL_VERIFICATION = currentpath+"auth/emailValidate";
    public static final String WEB_URL_OTP_VERIFICATION = currentpath+"auth/verifyOtp";
    public static final String WEB_URL_RESENDOTP_VERIFICATION = currentpath+"auth/sendOtp";
    public static final String WEB_URL_LOGIN = currentpath+"auth/login";
    public static final String WEB_URL_LOGOUT = currentpath+"auth/logout";
    public static final String WEB_URL_PUSHNOTIFICATION= currentpath+"auth/registerDevice";

    //Vehicle Module:
    public static final String WEB_URL_GET_COMPANYLIST_DETAILS = currentpath+"vehicle/list-company/user-id/";
    public static final String WEB_URL_UPDATE_COMPANY_DETAILS = currentpath+"vehicle/update-company/company-id/";
    public static final String WEB_URL_ADD_COMPANY_DETAILS = currentpath+"vehicle/create-company";
    public static final String WEB_URL_ADD_CAR = currentpath+"vehicle/create";

    //product Module:
    public static final String WEB_URL_GET_ORDER_PARTS = currentpath+"product/vehicle-parts";
    public static final String WEB_URL_GET_PRODUCT_CONDITION_ID= currentpath+"product/conditions";
    public static final String WEB_URL_GET_PACKS = currentpath+"product/service-packs";

    //Shoping Lists:
    public static final String WEB_URL_GET_SHOPPING_LIST_ITEMS = currentpath+"product/shopping-parts";

    //Image Upload:
    public static final String WEB_URL_IMAGE_UPLOAD = currentpath+"imagemanage/upload";

    //Request Quote:
    public static final String WEB_URL_ADD_REQUEST_ITEM = currentpath+"quotreq/add-item/user-id/";
    public static final String WEB_URL_LIST_ITEM = currentpath+"quotreq/list-items/user-id/";
    public static final String WEB_URL_REQUEST_DELETE= currentpath+"quotreq/delete/quotreq-id/";

    //Quotes:
    public static final String WEB_URL_LIST_QUOTES = currentpath+"quote/listQuotes/user-id/";
    public static final String WEB_URL_DETAIL_QUOTES_ITEM = currentpath+"quote/getQuote/quote-id/";
    public static final String WEB_URL_REMOVE_QUOTES_ITEM = currentpath+"quote/removeItem/";
    public static final String WEB_URL_ACCEPT_QUOTE = currentpath+"quote/accept/quote-id/";
    public static final String WEB_URL_QUOTE_QUOTE = currentpath+"quote/create";
    public static final String WEB_URL_QUOTE_DECLINE = currentpath+"quote/decline/quote-id/";
    public static final String WEB_URL_QUOTE_PAYMENT = currentpath+"payment/create";
    public static final String WEB_URL_QUOTE_BANK_LIST = currentpath+"payment/list-bank";


    public static final String WEB_URL_LIST_ORDERS = currentpath+"order/list/user-id/";
    public static final String WEB_URL_DETAIL_ORDERS_ITEM = currentpath+"order/getOrder/order-id/";

    public static final String WEB_URL_DASHBOARD = currentpath+"vehicle/count/user-id/";
    public static final String WEB_URL_ADD_SHIPPING_DETAILS = currentpath+"user/create-shipping-address";
    public static final String WEB_URL_LIST_SHIPPING_DETAILS = currentpath+"/user/list-shipping-address/user-id/";

    public static final String WEB_URL_ADD_DRIVER_DETAILS = currentpath+"vehicle/create-driver";
    public static final String WEB_URL_GET_DRIVER_DETAILS = currentpath+"vehicle/list-driver/company-id/";
    public static final String WEB_URL_GET_CAR_DETAIL = currentpath+"vehicle/list/user-id/";
    public static final String WEB_URL_DRIVER_INFO = currentpath+"vehicle/get-driver/vehicle-id/";

    //cart
    public static final String WEB_URL_PUT_ADDCART_METHOD= currentpath+"cart/add-item/user-id/";
    public static final String WEB_URL_GET_LIST_CART_METHOD= currentpath+"cart/list-items/user-id/";
    public static final String WEB_URL_GET_DELETE= currentpath+"/cart/delete/cart-id/";


    //order
    public static final String WEB_URL_GET_SHIPPING_METHOD= currentpath+"order/list-shipping-methods";
    public static final String WEB_URL_CREATE_ORDER= currentpath+"order/create";

    //Shopping
    public static final String WEB_URL_GET_POLICY= currentpath+"policy/get";





    public static boolean isStringNullOrWhiteSpace(String value) {
        if (value == null) {
            return true;
        }

        for (int i = 0; i <value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
