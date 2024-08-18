package com.squad22podA.task_mgt.utils;

public class EmailTemplate {

    public static String getEmailMessage(String name, String url, String token){

        return "CONGRATULATIONS!!! Your User Account Has Been Successfully Created.\n"
                + "Your Account Details: \n" + "Account FullName: " + name + " \n"
                + "Confirm your email " +
                "Please click the link to confirm your registration: " + getVerificationUrl(url, token);

    }

    public static String getVerificationUrl(String baseurl, String token){
        return baseurl + "/confirmation/confirm-token-sucess.html?token=" + token ;
    }
}
