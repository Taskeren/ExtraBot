package cn.glycol.extrabot.bot.server;

import static cc.moecraft.icq.PicqConstants.HTTP_API_VERSION_DETECTION;
import static cc.moecraft.icq.event.events.local.EventLocalHttpFail.Reason.INCORRECT_APPLICATION_TYPE;
import static cc.moecraft.icq.event.events.local.EventLocalHttpFail.Reason.INCORRECT_CHARSET;
import static cc.moecraft.icq.event.events.local.EventLocalHttpFail.Reason.INCORRECT_REQUEST_METHOD;
import static cc.moecraft.icq.event.events.local.EventLocalHttpFail.Reason.INCORRECT_SHA1;
import static cc.moecraft.icq.event.events.local.EventLocalHttpFail.Reason.INCORRECT_VERSION;
import static cc.moecraft.icq.utils.NetUtils.read;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import cc.moecraft.icq.event.events.local.EventLocalHttpFail;
import cc.moecraft.icq.event.events.local.EventLocalHttpFail.Reason;
import cc.moecraft.icq.exceptions.HttpServerException;
import cc.moecraft.icq.receiver.PicqHttpServer;
import cc.moecraft.icq.utils.SHA1Utils;
import cn.glycol.extrabot.bot.MixinBot;
import lombok.Getter;

/**
 * {@link PicqHttpServer} 升级版，支持关闭服务器。
 * 
 * @author Taskeren
 */
public class MixinHttpServer extends PicqHttpServer {

	protected final MixinBot bot;
	protected final int port;
	
	@Getter
	protected final MixinHttpHandler handler;

	public MixinHttpServer(MixinBot bot, int port) {
		super(port, bot);
		this.bot = bot;
		this.port = port;
		
		this.handler = new MixinHttpHandler(); // init handler
	}

	@Override
	public MixinBot getBot() {
		return bot;
	}

	@Getter
	protected HttpServer server;

	@Override
	public void start() {
		bot.getBotTweaker().onBotStarting(bot);
		try {
			server = HttpServer.create(new InetSocketAddress(port), 0);
			server.createContext("/", handler);
			server.start();
		} catch (IOException e) {
			throw new HttpServerException(logger, e);
		}
		bot.getBotTweaker().onBotStarted(bot);
	}

	/**
	 * 关闭 Http 服务器
	 */
	public void stop() {
		bot.getBotTweaker().onBotStopping(bot);
		server.stop(0);
		bot.getBotTweaker().onBotStopped(bot);
	}

	/**
	 * Http 监听器
	 */
	private class MixinHttpHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			// 是否暂停
			if (bot.getConfig().isHttpPaused()) {
				respondAndClose(exchange, 503, "");
				return;
			}

			// 验证 HTTP头
			if (!validateHeader(exchange)) {
				respondAndClose(exchange, 200, bot.getConfig().getHelloMessage());
				return;
			}

			// 获取请求数据
			String data = read(exchange.getRequestBody());

			// 验证 SHA1
			if (!validateSHA1(exchange, data)) {
				respondAndClose(exchange, 403, "");
				return;
			}

			// 输出Debug
			printDebug(exchange, data);

			// 调用事件
			bot.getEventManager().getEventParser().call(data);

			// 回复成功
			respondAndClose(exchange, 204, "");
		}
	}

	/**
	 * 验证一个请求
	 *
	 * @param exchange 请求
	 * @return 是否为 CoolQ Http 请求
	 */
	private boolean validateHeader(HttpExchange exchange) {
		// 必须是 POST
		if (!exchange.getRequestMethod().toLowerCase().equals("post")) {
			return failed(INCORRECT_REQUEST_METHOD, "Not POST");
		}

		// 获取头
		Headers headers = exchange.getRequestHeaders();
		String contentType = headers.getFirst("content-type");
		String userAgent = headers.getFirst("user-agent");

		// 必须是 UTF-8
		if (!contentType.toLowerCase().contains("charset=utf-8")) {
			return failed(INCORRECT_CHARSET, "Not UTF-8");
		}

		// 必须是 JSON
		if (!contentType.contains("application/json")) {
			return failed(INCORRECT_APPLICATION_TYPE, "Not JSON");
		}

		// 判断版本
		if (!bot.getConfig().isNoVerify() && !userAgent.matches(HTTP_API_VERSION_DETECTION)) {
			reportIncorrectVersion(userAgent);
			return failed(INCORRECT_VERSION, "Supported Version: " + HTTP_API_VERSION_DETECTION);
		}

		return true;
	}

	/**
	 * 验证 HAMC SHA1 (如果有的话)
	 *
	 * @param exchange 请求
	 * @param data     数据
	 * @return 如果有, 是否验证成功
	 */
	private boolean validateSHA1(HttpExchange exchange, String data) {
		// 是否启用验证
		if (bot.getConfig().getSecret().isEmpty()) {
			return true;
		}

		// 获取 SHA1
		String signature = exchange.getRequestHeaders().getFirst("x-signature");

		// CoolQ HTTP 是否有发送 SHA1
		if (signature == null || signature.isEmpty()) {
			return failed(INCORRECT_SHA1, "Signature Empty");
		}

		// 获取 SHA1 里面的 SHA1 嗯x
		signature = signature.replace("sha1=", "");

		// 生成 SHA1
		String generatedSignature = SHA1Utils.generateHAMCSHA1(data, bot.getConfig().getSecret());

		// 判断生成的和获取的是不是一样的
		if (!signature.equals(generatedSignature)) {
			return failed(INCORRECT_SHA1,
					"Signature Mismatch: \n" + "- Sent: " + signature + "\n- Generated: " + generatedSignature);
		}
		return true;
	}

	/**
	 * 报告失败
	 *
	 * @param reason 失败原因
	 */
	private boolean failed(Reason reason, String text) {
		getBot().getEventManager().call(new EventLocalHttpFail(reason));
		logger.debug("Http Failed: {}: {}", reason, text);
		return false;
	}

	/**
	 * 报告版本错误
	 *
	 * @param currentVersion 当前版本
	 */
	private void reportIncorrectVersion(String currentVersion) {
		logger.error("HTTP API请求版本不正确, 设置的兼容版本为: " + HTTP_API_VERSION_DETECTION);
		logger.error("当前版本为: " + currentVersion);
		logger.error("推荐更新这个类库或者HTTP API的版本");
		logger.error("如果要无视版本检查, 请修改 HTTP_API_VERSION_DETECTION");
	}

	/**
	 * 回复输出
	 *
	 * @param exchange 请求
	 * @param code     HTTP返回码 (204 = CoolQ 处理成功)
	 * @param response 回复
	 */
	private void respondAndClose(HttpExchange exchange, int code, String response) throws IOException {
		byte[] bytes = response.getBytes();

		exchange.sendResponseHeaders(code, bytes.length == 0 ? -1 : bytes.length);

		OutputStream out = exchange.getResponseBody();
		out.write(bytes);
		out.close();
	}

	/**
	 * 输出Debug消息
	 *
	 * @param exchange 请求
	 * @param data     数据
	 */
	private void printDebug(HttpExchange exchange, String data) {
		if (!bot.getConfig().isDebug())
			return;

		logger.debug("收到新请求: {}", exchange.getRequestHeaders().getFirst("user-agent"));
		logger.debug("- 数据: {}", data);
	}

}
