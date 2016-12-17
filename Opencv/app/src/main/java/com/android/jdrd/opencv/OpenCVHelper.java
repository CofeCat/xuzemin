package com.android.jdrd.opencv;


import org.opencv.core.Mat;

import java.io.FileDescriptor;
import java.util.ArrayList;

/**
 * Created by xuzemin on 16/9/8.
 */
public class OpenCVHelper {
    static {
        System.loadLibrary("OpenCVDemo");
    }
    public static native int[] gray(int[] buf, int w, int h);

    public static native int init();//��ʼ��������ͷ
    public static native int stop();//�ͷŹر�����ͷ
    public static native int getdata(long gray);//��ȡ����ͷ�ײ�һ֡����

    public static native int test();
    public static native int send();

    public static native int get();//��ʼ����ȡ����ײ������ӿ�
    public static native int keyDownPress();//���̷����¼�����
    public static native int keyDownInstitute();//���̷����¼��ɿ�
    public static native int keyUpPress();//���̷����ϼ�����
    public static native int keyUpInstitute();//���̷����ϼ��ɿ�
    public static native int keyLeftPress();//���̷����������
    public static native int keyLeftInstitute();//���̷�������ɿ�
    public static native int keyRightPress();//���̷����Ҽ�����
    public static native int keyRightInstitute();//���̷����Ҽ��ɿ�
    public static native int keyReturn();//���̻س��������ɿ�
    public static native int closeget();//���������ӿڹر�

    public static native int sendABSData(int mouse_x,int mouse_y,int mouse_press);
    public static native int sendData(int mouse_x,int mouse_y,int mouse_press);
    public static native int MousePress(int mouse_press);
    public static native int initMouse();
    public static native int closeMouse();

    public static native FileDescriptor openUsb(String path, int baudrate, int flags);
}
