package com.d3.yiqi.util;
/**
* 
* �ַ��������Ĺ�����
* 
* @author huwei(jshuwei.org.cn)
* @since 1.0
* 
*/
public class StringUtil {
    /**
     * �滻�ַ���
     * 
     * @since 1.1
     * @param strSc
     *            ��Ҫ�����滻���ַ���
     * @param oldStr
     *            Դ�ַ���
     * @param newStr
     *            �滻����ַ���
     * @return �滻���Ӧ���ַ���
     */
    public static String replace(String strSc, String oldStr, String newStr) {
        String ret = strSc;
        if (ret != null && oldStr != null && newStr != null) {
            ret = strSc.replaceAll(oldStr, newStr);
        }
        return ret;
    }

    /**
     * �滻�ַ������޸�java.lang.String���replaceAll����ʱ��һ�������ַ�����������ʱ(�磺"address".
     * replaceAll("dd","$");)���׳��쳣��java.lang.StringIndexOutOfBoundsException:
     * String index out of range: 1�����⡣
     * 
     * @since 1.2
     * @param strSc
     *            ��Ҫ�����滻���ַ���
     * @param oldStr
     *            Դ�ַ���
     * @param newStr
     *            �滻����ַ���
     * @return �滻���Ӧ���ַ���
     */
    public static String replaceAll(String strSc, String oldStr, String newStr) {
        int i = -1;
        while ((i = strSc.indexOf(oldStr)) != -1) {
            strSc = new StringBuffer(strSc.substring(0, i)).append(newStr)
                    .append(strSc.substring(i + oldStr.length())).toString();
        }
        return strSc;
    }

    /**
     * ���ַ���ת����HTML��ʽ���ַ���
     * 
     * @since 1.1
     * @param str
     *            ��Ҫ����ת�����ַ���
     * @return ת������ַ���
     */
    public static String toHtml(String str) {
        String html = str;
        if (str == null || str.length() == 0) {
            return "";
        } else {
            html = replace(html, "&", "&");
            html = replace(html, "<", "<");
            html = replace(html, ">", ">");
            html = replace(html, "\r\n", "\n");
            html = replace(html, "\n", "<br>\n");
//            html = replace(html, "\"", """);
            html = replace(html, " ", " ");
            return html;
        }
    }

    /**
     * ��HTML��ʽ���ַ���ת���ɳ�����ʾ���ַ���
     * 
     * @since 1.1
     * @param str
     *            ��Ҫ����ת�����ַ���
     * @return ת������ַ���
     */
    public static String toText(String str) {
        String text = str;
        if (str == null || str.length() == 0) {
            return "";
        } else {
            text = replace(text, "&", "&");
            text = replace(text, "<", "<");
            text = replace(text, ">", ">");
            text = replace(text, "<br>\n", "\n");
            text = replace(text, "<br>", "\n");
//            text = replace(text, """, "\"");
            text = replace(text, " ", " ");
            return text;
        }
    }

    /**
     * ��һ�ַ���������ĳ�ض����ַ�����Ϊ�ָ�������ַ���
     * 
     * @since 1.0
     * @param strs
     *            �ַ�������
     * @param token
     *            �ָ��ַ���
     * @return ��tokenΪ�ָ����ַ���
     */
    public static String join(String[] strs, String token) {
        if (strs == null)
            return null;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            if (i != 0)
                sb.append(token);
            sb.append(strs[i]);
        }
        return sb.toString();
    }

    /**
     * ��һ�ַ�����ĳ�ض����ַ�����Ϊ�ָ�������ַ�������
     * 
     * @since 1.0
     * @param str
     *            ��Ҫ��ֵ��ַ���("@12@34@56")
     * @param token
     *            �ָ��ַ���("@")
     * @return ��tokenΪ�ָ��Ĳ�ֿ����ַ�������
     */
    public static String[] split(String str, String token) {
        String temp = str.substring(1, str.length());
        return temp.split(token);
    }

    /**
     * ��֤�ַ����Ϸ���
     * 
     * @since 1.0
     * @param str
     *            ��Ҫ��֤���ַ���
     * @param test
     *            �Ƿ��ַ������磺"~!#$%^&*()',;:?"��
     * @return true:�Ƿ�;false:�Ϸ�
     */
    public static boolean check(String str, String test) {
        if (str == null || str.equals(""))
            return true;
        boolean flag = false;
        for (int i = 0; i < test.length(); i++) {
            if (str.indexOf(test.charAt(i)) != -1) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * ����ֵ���ַ���ת����Integer��
     * 
     * @since 1.0
     * @param str
     *            ��Ҫת�����ַ����ַ���
     * @param ret
     *            ת��ʧ��ʱ���ص�ֵ
     * @return �ɹ��򷵻�ת�����Integer��ֵ��ʧ���򷵻�ret
     */
    public static Integer String2Integer(String str, Integer ret) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return ret;
        }
    }

    /**
     * ����ֵ��ת�����ַ���
     * 
     * @since 1.0
     * @param it
     *            ��Ҫת����Integer��ֵ
     * @param ret
     *            ת��ʧ�ܵķ���ֵ
     * @return �ɹ��򷵻�ת������ַ�����ʧ���򷵻�ret
     */
    public static String Integer2String(Integer it, String ret) {
        try {
            return Integer.toString(it);
        } catch (NumberFormatException e) {
            return ret;
        }
    }

    /**
     * �Ƚ����ַ�����С(ASCII��˳��)
     * 
     * @since 1.1
     * @param str1
     *            ����Ƚϵ��ַ���1
     * @param str2
     *            ����Ƚϵ��ַ���2
     * @return str1>str2:1;str1<str2:-1;str1=str2:0
     */
    public static int compare(String str1, String str2) {//
        if (str1.equals(str2)) {
            return 0;
        }
        int str1Length = str1.length();
        int str2Length = str2.length();
        int length = 0;
        if (str1Length > str2Length) {
            length = str2Length;
        } else {
            length = str1Length;
        }
        for (int i = 0; i < length; i++) {
            if (str1.charAt(i) > str2.charAt(i)) {
                return 1;
            }
        }
        return -1;
    }

    /**
     * �����������ֵ�Ǯ��ת�������ķ�ʽ
     * 
     * @since 1.1
     * @param num
     *            ��Ҫת����Ǯ�İ�����������ʽ
     * @return ת�����������ʽ
     */
    public static String num2Chinese(double num) {
        String result = "";
        String str = Double.toString(num);
        if (str.contains(".")) {
            String begin = str.substring(0, str.indexOf("."));
            String end = str.substring(str.indexOf(".") + 1, str.length());
            byte[] b = begin.getBytes();
            int j = b.length;
            for (int i = 0, k = j; i < j; i++, k--) {
                result += getConvert(begin.charAt(i));
                if (!"��".equals(result.charAt(result.length() - 1) + "")) {
                    result += getWei(k);
                }
                System.out.println(result);

            }
            for (int i = 0; i < result.length(); i++) {
                result = result.replaceAll("����", "��");
            }
            if ("��".equals(result.charAt(result.length() - 1) + "")) {
                result = result.substring(0, result.length() - 1);
            }
            result += "Ԫ";
            byte[] bb = end.getBytes();
            int jj = bb.length;
            for (int i = 0, k = jj; i < jj; i++, k--) {
                result += getConvert(end.charAt(i));
                if (bb.length == 1) {
                    result += "��";
                } else if (bb.length == 2) {
                    result += getFloat(k);
                }
            }
        } else {
            byte[] b = str.getBytes();
            int j = b.length;
            for (int i = 0, k = j; i < j; i++, k--) {
                result += getConvert(str.charAt(i));
                result += getWei(k);
            }
        }
        return result;
    }

    private static String getConvert(char num) {
        if (num == '0') {
            return "��";
        } else if (num == '1') {
            return "һ";
        } else if (num == '2') {
            return "��";
        } else if (num == '3') {
            return "��";
        } else if (num == '4') {
            return "��";
        } else if (num == '5') {
            return "��";
        } else if (num == '6') {
            return "��";
        } else if (num == '7') {
            return "��";
        } else if (num == '8') {
            return "��";
        } else if (num == '9') {
            return "��";
        } else {
            return "";
        }
    }

    private static String getFloat(int num) {
        if (num == 2) {
            return "��";
        } else if (num == 1) {
            return "��";
        } else {
            return "";
        }
    }

    private static String getWei(int num) {
        if (num == 1) {
            return "";
        } else if (num == 2) {
            return "ʮ";
        } else if (num == 3) {
            return "��";
        } else if (num == 4) {
            return "ǧ";
        } else if (num == 5) {
            return "��";
        } else if (num == 6) {
            return "ʮ";
        } else if (num == 7) {
            return "��";
        } else if (num == 8) {
            return "ǧ";
        } else if (num == 9) {
            return "��";
        } else if (num == 10) {
            return "ʮ";
        } else if (num == 11) {
            return "��";
        } else if (num == 12) {
            return "ǧ";
        } else if (num == 13) {
            return "��";
        } else {
            return "";
        }
    }
    /**
     * ���ַ���������ĸ��Ϊ��д
     * 
     * @since 1.2
     * @param str
     *            ��Ҫ��д���ַ���
     * @return ��д����ַ���
     */
    public static String firstToUpper(String str){
        return str.substring(0,1).toUpperCase()+str.substring(1);
    }
}