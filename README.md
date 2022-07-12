# Android
## 1.0 事件管理App Demo
#### 使用MVVM架构
#### App中使用到Navigation、Fragment、Data BinDing、持久本地存储ROOM
## 1.1Demo使用
#### 点击 + 创建新事件
#### 在列表界面可以点击事件进入事件修改及删除  //列表界面也可以左滑删除事件
#### 排序从优先级高到低或者优先级低到高
#### 搜索是文本中只要有的输入关键字都会显示出来

## 2.0 手电筒Demo

#### 调用相机权限完成闪光灯的开/关

```
android.permission.CAMERA
```

## 2.1 Demo使用

#### 点击VIew的小图标就可以打开闪光灯，再次点击关闭

## 3.0 Retrofit Demo

#### 通过Retrofit框架访问网络

## 3.1 Demo使用

#### 打开自动访问网站生成RecyclerView里相应的内容

## 4.0 蓝牙Demo

#### 使用安卓自带的getDefaultAdapter()方法来与手机的蓝牙模块通信

## 4.2 Demo使用

#### 点击打开按钮来打开蓝牙，如果已打开会提示 “ 蓝牙已打开 ” 关闭按钮同理。

#### 可见按钮是让附近设备搜索到

#### 点击配对按钮是显示已配对的设备

## 5.0 生物识别Demo

#### 导入生物识别的依赖包

```
implementation("androidx.biometric:biometric:1.1.0")
```

#### 创建BiometricPrompt各个方法来实现是否验证成功

## 5.1 Demo使用

#### 点击验证按钮，开始识别

## 6.0 OkHttpDemo

#### okhttp请求使用流程

1. **创建请求：Request.Builder()->Request>对象**
2. **通过Request得到Call对象：client.newCall(request)->Call对象**
3. **执行Call:同步cal.execute(),异步cal.enqueue()**
4. **得到Response对象**

#### BUG：

1. **登录状态一直是登录成功，msg网页返回的值就是账号或者error，不知道为什么这个switch一直选择成功**
2. **注册没有完成写的有点问题**
