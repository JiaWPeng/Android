# Android
## 1.0 事件管理App Demo
#### 使用MVVM架构
#### App中使用到Navigation、Fragment、Data BinDing、持久本地存储ROOM
## 1.1 Demo使用
#### 		点击 + 创建新事件
#### 		在列表界面可以点击事件进入事件修改及删除  //列表界面也可以左滑删除事件
#### 		排序从优先级高到低或者优先级低到高
#### 		搜索是文本中只要有的输入关键字都会显示出来

## 2.0 手电筒Demo

#### 		调用相机权限完成闪光灯的开/关

```
android.permission.CAMERA
```

## 2.1 Demo使用

#### 		点击VIew的小图标就可以打开闪光灯，再次点击关闭

## 3.0 Retrofit Demo

#### 		通过Retrofit框架访问网络

## 3.1 Demo使用

#### 		打开自动访问网站生成RecyclerView里相应的内容

## 4.0 蓝牙Demo

#### 		使用安卓自带的getDefaultAdapter()方法来与手机的蓝牙模块通信

## 4.2 Demo使用

#### 		点击打开按钮来打开蓝牙，如果已打开会提示 “ 蓝牙已打开 ” 关闭按钮同理。

#### 		可见按钮是让附近设备搜索到

#### 		点击配对按钮是显示已配对的设备

## 5.0 生物识别Demo

#### 		导入生物识别的依赖包		

```java
implementation("androidx.biometric:biometric:1.1.0")
```

#### 		创建BiometricPrompt各个方法来实现是否验证成功

## 5.1 Demo使用

#### 		点击验证按钮，开始识别

## 6.0 OkHttp Demo

#### 	okhttp请求使用流程

1. **创建请求：Request.Builder()->Request>对象**
2. **通过Request得到Call对象：client.newCall(request)->Call对象**
3. **执行Call:同步cal.execute(),异步cal.enqueue()**
4. **得到Response对象**

#### BUG：

1. **登录状态一直是登录成功，msg网页返回的值就是账号或者error，不知道为什么这个switch一直选择成功**
2. **注册没有完成写的有点问题**

## 7.0 传感器Demo

​		**使用SensorManger类来获得本机传感器**

## 8.0 WIFI Demo

​		**使用WIfiManger类来获得本机WIFI状态**

​		**点击扫描时设备需打开系统设置 -- > 位置信息后才可以显示附近WiFi网络名称，不需要打开WiFi**

​		**Android10及以上 setWifiEnabled()方法不生效**

- **此方法在 API 级别 29 中已弃用。从 Build.VERSION_CODES#Q 开始，不允许应用程序启用/禁用 Wi-Fi。**
- **兼容性说明：对于面向 Build.VERSION_CODES.Q 或更高版本的应用程序，此 API 将始终返回 false 并且无效。**
- **如果应用针对的是较旧的 SDK（Build.VERSION_CODES.P 或更低版本），它们可以继续使用此 API。**

### **解决API29 setWifiEnabled方法不生效**

```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
		Intent pen = new Intent ( Settings.Panel.ACTION_INTERNET_CONNECTIVITY );
        startActivityForResult ( pen,0 );
}
```

**调用系统自带网络连接的窗口，来选择网络的连接；可以使用WIFI，移动网络。**

## 9.0 NFC Demo

**将卡片放置手机NFC线圈初，会自动打开Demo并显示卡片类型等内容**

**本Demo使用NfcAdapter，Tag类**

**Android还自带NfcManger，NdefRecord，NderMessage等**

**其他类请看**[Google官方文档](https://developer.android.com/reference/android/nfc/package-summary)

## 10.0 Video Demo

- **使用MediaController+VideoView控件来实现视频播放**
  1. MediaConntroller提供进度条，开始，暂停等操作，无需自己添加
  2. VideoView继承SurfaceView，提供丰富的API如，seekTo()是设置从第几毫秒开始播放
- 使用MediaPlayer+SurfaceView来实现视频播放
  1. MediaPlayer主要提供音频播放，需要结合SurfaceView来播放视频
  2. SurfaceView需要自定义控件来控制开始，暂停等，难度较大

## 11.0 消息通知Demo

**使用Notification方法来设置消息通知的各个属性**

**如果是Android 8.0（API 26）以上版本需要设置NotificationChannel方法**

## 12.0 仿微信语音

**可以按住录音，松开保存，上滑取消保存，播放录音**

- **使用MediaPlayer播放音频**
- 使用MediaRecorder录音
- 对Android11的存储权限适配
