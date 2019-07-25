# ExtraBot
一个给 PicqBotX 开发的库。

## MixinBot：高度自定义化机器人

MixinBot 基于 PicqBotX，并对其进行了修改和升级，使之更加客制化，用户友好化。

MixinBot 主要对 PicqBotX 的部分管理器进行反射修改，见下表。

| PicqBotX 组件 | MixinBot 组件 | 是否必须 |
| :-: | :-: | :-: |
| CommandManager | MixinCommandManager | 必须 |
| EventManager | MixinEventManager | 必须 |
| PicqHttpServer | MixinHttpServer | 可选 |
| AccountManager | MixinAccountManager | 必须 |

### 实例化机器人和快捷方法

实例化 BotTweaker 与 PicqBotX 类似。

```java
// MixinBotConfiguration 继承于 PicqConfig，可以对其内容进行操作，但是需要强制转型为 MixinBotConfiguration
MixinBotConfiguration config = new MixinBotConfiguration(25560);
BotTweaker tweaker = BotTweaker.DEFAULT;
MixinBot bot = new MixinBot(config); // 不使用 BotTweaker
MixinBot botTweakable = new MixinBot(config, tweaker); // 使用 BotTweaker
```

MixinBot 提供了指令和事件监听器的快捷注册方法

```java
MixinBot bot = new MixinBot(config);
bot.addCommand(command); // 常规注册指令
bot.addEventListener(listener); // 常规注册事件
bot.doAutoRegister(clazz); // AutoRegister 注册，见下方 AutoRegister 描述
```

### 异常抛出与捕捉自定义

MixinBot 目前暂时只提供了 addAcount 和 startBot 的异常捕捉。当没有设置异常处理器时，会继续向上抛出异常。

```java
MixinBotConfiguration config = new MixinBotConfiguration(25560)
.setAddAccountExceptionHandler(
	(bot, exception) -> {
		bot.getLogger().errorf("在 addAccount 时捕捉到了一个异常：%s", exception.getMessage());
		bot.getLogger().error(exception);
	}
)
.setStartBotExceptionHandler(
	(bot, exception) -> {
		// do something
	}
);
```

### BotTweaker：机器人本地事件监听器

BotTweaker 用于对机器人非信息处理的一些操作进行修改，如指令注册事件。__BotTweaker 仅支持 MixinBot！__

BotTweaker 提供了几种方法，当这些方法返回 `false` 的时，则表示停止，或者取消这些操作，默认都是 `true`。下面我以指令添加事件作为实例，进行演示。

```java
public static final BotTweaker tweaker = new BotTweaker() {

	public boolean onAddCommand(MixinBot bot, IcqCommand command) {
		// 表示当指令名称为 “MyName” 时，禁止它的注册。
		if(command.properties().getName().contentEquals("MyName")) {
			return false;
		}
	}

}

// 在机器人实例化时导入 BotTweaker
MixinBot bot = new MixinBot(new MixinBotConfiguration(25560), tweaker);
```

### MixinHttpServer：更加客制化的 HttpServer

MixinHttpServer 的内容为可选内容。

MixinHttpServer 提供了关闭服务器的接口，用于特殊用途。

```java
MixinBot bot = new MixinBot(config);
bot.stopBot();
```

MixinHttpServer 还提供了当被非酷QHTTPAPI访问时返回的信息自定义。

```java
MixinBotConfiguration config = new MixinBotConfiguration(25560).setHelloMessage("Hey, 404 here!");
```

同时 MixinHttpServer 暴露了 HttpServer（com.sun.net.httpserver.HttpServer），用于特殊开发。

```java
MixinBot bot = new MixinBot(config);
PicqHttpServer server = bot.getHttpServer();
if(server instanceof MixinHttpServer) {
	MixinHttpServer ms = (MixinHttpServer) server;
	HttpServer = ms.getServer();

	// 操作示例
	ms.createContext("/yoyoyoyo", handler);
}

```

### MixinAccountManager：更多获取 IcqHttpApi 的方法

MixinAccountManager 提供了机器人名称和机器人QQ号获取 IcqHttpApi 的检索方法。

```java
MixinBot bot = ...;

bot.addAccount("Geo", "127.0.0.1", 25560); // 此时酷Q登陆的账号的QQ号是 10000

MixinAccountManager am = bot.getAccountManager();
BotAccount acc1 = am.getAccountByUID(10000L); // 通过QQ号获取
BotAccount acc2 = am.getAccountByName("Geo"); // 通过机器人名称获取
```

另类获取方法，使用 BotAccountFinder。这种方法可以在某些需要的地方用上。_讲道理我也不知道能用在哪里..._

```java
// 用by()方法传入名称和QQ号
BotAccountFinder finder = new BotAccountFinder().by(10000L).by("Geo");
// 传入一个机器人来获取账号
BotAccount acc = finder.find(bot);
```

### Timer in Mixin：计划任务

就是普普通通的 TimerTask。下面展示如何获取 Timer。

```java
MixinBot bot = ...;
Timer timer = bot.getTimer(); // 获取 Timer 就这么简单
```

但是咱有特别的 TimerTask。下面展示信息发送任务。

```java
Timer timer = bot.getTimer();

// 传入的参数分别是：机器人，信息，号码（QQ号/群号/讨论组号），消息类型（人，群，讨论组），账号查找器
MixinTaskMessage task = 
	new MixinTaskMessage(bot, "Hello", 3070190799L, MessageType.PRIVATE, new BotAccountFinder().by("Geo"));

// 这些信息都可以进行修改
task.setMessage("Bye~");

timer.schedule(task, 0, 1000*60*60*24); // 然后送到调度器里去就好了
```

### State of Mixin：机器人状态

```java
int state = bot.getState();
```

| 状态码（int） | 意义 |
| :-: | :-: |
| 0 | 初始化阶段，组件尚未加载完成 |
| 1 | 挂起状态，不能监听HttpApi消息，但是可以发送消息 |
| 2 | 运行状态，能够处理HttpApi消息 |

#### 如果你还有什么底层的内容需要访问，请创建一个 issue。

### MixinBotInjector：给开发者用的反射工具包

MixinBotInjector 内置了修改 PicqBotX 管理器的方法，支持的变量见下表。修改时机建议是机器人初始化完成后立即修改，不然容易造成数据丢失，如指令注册丢失。

| 方法 | 内容 |
| :-: | :-: |
| setHttpServer | 设置HTTP监听服务器 |
| setEventManager | 设置事件管理器 |
| setAccountManager | 设置机器人账号管理器 |
| setUserManager | 设置用户对象缓存管理器 |
| setGroupManager | 设置群对象缓存管理器 |
| setGroupUserManager | 设置群用户对象缓存管理器 |
| setCommandManager | 设置指令管理器 |
| setLoggerInstanceManager | 设置Logger实例管理器 |

## 消息解析：Message Parser

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