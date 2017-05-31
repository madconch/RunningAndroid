package com.madconch.running.mvcdemo;

import org.junit.Test;

import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String str1 = "2015.12.05 10:00:00";
        String str2 = "2015/12/05 10:00:00";
        String str3 = "2015年12月05日 10时00分00秒";

        String str = str2;

        Date date = null;
        try {
            date = new Date(Date.parse(str));
        }catch (Exception e){
            //ignore
        }

        System.out.println(">>>" + date);
//        date = MadDateUtil.toGuessDate(str);
//        System.out.println(">>>" + date);
    }
}