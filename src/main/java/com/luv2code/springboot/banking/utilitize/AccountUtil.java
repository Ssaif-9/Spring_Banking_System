package com.luv2code.springboot.banking.utilitize;

import java.time.Year;

public class AccountUtil {

    public static final String Account_Exists_code ="001";
    public static final String Account_Exists_Message="Account already exist";
    public static final String Account_Not_Exists_code ="002";
    public static final String Account_Not_Exists_Message="User with this account does not exist";
    public static final String Account_Created_code ="003";
    public static final String Account_Created_Message="Account created";
    public static final String Account_Found_code ="004";
    public static final String Account_Found_Message="Account Founded";
    public static final String Amount_Credited_code ="005";
    public static final String Amount_Credited_Message="Amount Added";
    public static final String Balance_Not_Enough_code ="006";
    public static final String Balance_Not_Enough_Message="Amount Not Enough to debit";
    public static final String Amount_Debited_code ="007";
    public static final String Amount_Debited_Message="Amount debit..Done";
    public static final String Transfer_Source_Not_Found_code ="008";
    public static final String Transfer_Source_Not_Found_Message="User with Source account does not exist";
    public static final String Transfer_Destination_Not_Found_code ="009";
    public static final String Transfer_Destination_Not_Found_Message="User with destination account does not exist";

    public static final String Transfer_Successfully_code ="010";
    public static final String Transfer_Successfully_Message="Transfer successfully";

    public static String generateAccountNumber() {

        /*
        *
        * Account Number = Current Year + Random Number in range [100900~999100]
        *
        * */
        Year currentYear =Year.now();
        /*
        * to ensure th random number will be 6 digit
        * */
        int max = 999999;
        int min = 100000;
        /*
        * Math.random() -> range between [0.001~0.999]
        * Math.floor() -> to Round  Down
        *
        * */
        int randomNumber =(int) Math.floor(Math.random() * (max-min+1)+min);

        String stringCurrentYear =String.valueOf(currentYear);
        String stringRandomNumber =String.valueOf(randomNumber);

        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(stringCurrentYear).append(stringRandomNumber).toString();
    }
}
