package net.yrom.screenrecorder.rtmp;

/**
 * Created by lake on 16-3-16.
 * Modified by raomengyang on 17-3-12
 */
public class RESFlvData {

    // video size
    public static final int VIDEO_WIDTH = 1080;
    public static final int VIDEO_HEIGHT = 1920;
    public static final String Server_IP = "192.168.0.101";
    //public static final String Server_IP = "192.168.31.220";
    //public static final String Server_IP = "192.168.43.155";
    public static final int Server_PORT = 53100;
    public static final int VIDEO_BITRATE = 1000000; // 500Kbps
    public static final int VIDEO_I_FRAME_INTERVAL = 0;
    public static final int FPS = 60;
    public static final int AAC_SAMPLE_RATE = 44100;
    public static final int AAC_BITRATE = 32 * 1024;

    public static final int FLV_RTMP_PACKET_TYPE_VIDEO = 9;
    public static final int FLV_RTMP_PACKET_TYPE_AUDIO = 8;
    public static final int FLV_RTMP_PACKET_TYPE_INFO = 18;
    public static final int NALU_TYPE_IDR = 5;

    public boolean droppable;

    public int dts;//解码时间戳

    public byte[] byteBuffer; //数据

    public int size; //字节长度

    public int flvTagType; //视频和音频的分类

    public int videoFrameType;

    public boolean isKeyframe() {
        return videoFrameType == NALU_TYPE_IDR;
    }

}
