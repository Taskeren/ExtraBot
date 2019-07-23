# ExtraBot
一个给 PicqBotX 开发的库。

## 消息解析： Message Parser

给指令提供更方便的操作。

下面是解析at组件的实例。

```java
public class CommandA implements EverywhereCommand {

	public CommandProperties properties() {
		return new CommandProperties("a");
	}
	
	String run(EventMessage evt, User sender, String command, ArrayList<String> args) {
		ArrayList<Component> components = Component.parseComponents(args);
		Component component = components.get(0);
		if(component instanceof ComponentAt) {
			return String.valueOf(((ComponentAt) component).getQQ());
		}
		else {
			return null;
		}
	}
}
```

### 消息组件列表

#### ComponentAt
@组件，可以使用 `getQQ()` 来获取被@的用户QQ号。

#### ComponentBFace
表情，原创表情和魔法表情，就是QQ表情商城卖的那种，可以使用 `getId()` 来获取表情ID。

#### ComponentContact
名片组件，可以使用 `getType()` 来获取类型，类型有 `QQ（用户）` 和 `GROUP（群）`，__没有讨论组__！
可以使用 `getId()` 获取QQ号（群号）。

#### ComponentDice
魔法表情，色子，可以使用 `getType()` 来获取色子点数（1-6）。

#### ComponentFace
QQ符号表情，可以使用 `getId()` 来获取表情ID。

#### ComponentImage
图片组件，可以使用 `getFile()` 来获得本地文件名称，可以使用 `getUrl()` 来获得在线图片地址。

#### ComponentLocation
位置组件，可以使用 `getLat()` 和 `getLon()` 获取经纬度，使用 `getTitle()` 获取粗略地名，例如小区名，使用 `getContent()` 获取详细地址，如“浙江省宁波市海曙区高桥镇杨家漕路2号”，使用 `getStyle()` 获取样式类型，目前不知道有什么区别。

#### ComponentRecord
语音消息，可以使用 `getFile()` 获取本地文件名称，使用 `getMagic()` 获取是否变音。

#### ComponentRich
富文本消息，懒得详细讲，有很多用，看后面。

#### ComponentRPS
魔法表情，石头剪刀布，使用 `getType()` 获取结果。

#### ComponentShake
抖一抖啦。

#### ComponentSign
签到，就是那个群签到，使用 `getLocation()` 获取签到地址，使用 `getTitle()` 获取签到消息，使用 `getImage()` 获取签到背景。

#### ComponentString
普通消息，不是消息组件，凑数用的。

#### ComponentRichMusic
音乐分享，由富文本组件再解析得到，使用 `getTitle()` 获取歌曲名称，使用 `getDesc()` 获取歌曲描述（常常是作者），使用 `getPreview()` 获取分享图片地址，使用 `getTag()` 获取APP名称，使用 `getUrlMusic()` 获取在线音乐文件地址，使用 `getUrlPage()` 获取跳转网页地址。

#### ComponentRichNews
文章，新闻，视频（Bilibili）分享，由富文本组件再解析得到，使用 `getTitle()` 获取页面名称，使用 `getDesc()` 获取页面描述，使用 `getPreview()` 获取页面预览图，使用 `getTag()` 获取APP名称，使用 `getUrl()` 获取网页地址。

## 快速注册指令和事件监听器
专门定义一个类用于存放实例，实例标上 `@AutoRegister` 组件，其中 `value()` 为实例类型，`COMMAND` 为指令，`LISTENER` 为事件监听器。

```java
public class Reference {

	@AutoRegister(Type.COMMAND)
	public static final IcqCommand A = new CommandA();

	@AutoRegister(Type.LISTENER)
	public static final IcqListener B = new MessageListener();

}
```

然后在指令管理器加载完成后注册这个类。

```java
ExtraBot.register(Reference.class, bot);
```

然后就会快速导入到机器人里了。