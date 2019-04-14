package com.smart.lamp.util;

import java.util.Stack;

/**
 * TODO 字符串加密工具
 *
 * @author fattoliu
 * @version V 1.0
 * @date on 8/3/2019 10:57 PM
 */
public class SecurityUtil {

    /** 字符串加密
     * @param source 需要加密的字符串
     * @param length 加密后字符串长度
     * @return 加密后的字符串
     */
    public static String encrypt (String source, int length) {
        String random = getRandomString(length);
        char[] randomChars = random.toCharArray();
        char[] sourceChars = source.toCharArray();
        int index = 0;
        for(int i = 0; i < randomChars.length; i++) {
            if (i % 3 == 0) {
                randomChars[i] = sourceChars[index];
                if (index == sourceChars.length -1) {
                    break;
                }
                index++;
            }
        }
        return reverse(new String(randomChars));
    }

    /**
     * 字符串解密
     * @param source 原始字符串
     * @param length 解密后的字符串长度
     * @return 解密后的字符串
     */
    public static String decrypt (String source, int length) {
        String reallySource = reverse(source);
        char[] sourceChars = reallySource.toCharArray();
        char[] resultChars = new char[length];
        int index = 0;
        for(int i = 0; i < sourceChars.length; i++) {
            if (i % 3 == 0) {
                resultChars[index] = sourceChars[i];
                if (index == resultChars.length -1) {
                    break;
                }
                index++;
            }
        }
        return new String(resultChars);
    }
    /**
     * 字符串反转
     * @param str 原始字符串
     * @return 反转后的字符串
     */
    private static String reverse(String str) {
        // base case: if string is null or empty
        if (str == null || str.equals(""))
            return str;
        // create an empty stack of characters
        Stack< Character > stack = new Stack<>();
        // push every character of the given string into the stack
        char[] ch = str.toCharArray();
        for (int i = 0; i < str.length(); i++)
            stack.push(ch[i]);
        // start from index 0
        int k = 0;
        // pop characters from the stack until it is empty
        while (!stack.isEmpty()) {
            // assign each popped character back to the character array
            ch[k++] = stack.pop();
        }
        // convert the character array into string and return it
        return String.copyValueOf(ch);
    }

    /**
     * 获取随机长度
     * @param length 长度
     * @return 随机长度
     */
    private static int getRandom(int length) {
        return (int) Math.round(Math.random() * (length));
    }

    /** 获取随机字符串
     * @param length 字符串长度
     * @return 随机字符串
     */
    private static String getRandomString(int length){
        StringBuilder buffer = new StringBuilder();
        String divisor = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890<>?!@#$" +
                "%^&*()~'’;.,";
        int len = divisor.length();
        for (int i = 0; i < length; i++) {
            buffer.append(divisor.charAt(getRandom(len-1)));
        }
        return buffer.toString();
    }
}
