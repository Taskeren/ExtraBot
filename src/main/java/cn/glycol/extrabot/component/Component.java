package cn.glycol.extrabot.component;

import cn.glycol.extrabot.component.ComponentContact.Type;
import cn.glycol.extrabot.component.ComponentRPS.RSP;
import cn.glycol.tutils.map.ParseableHashMap;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息组件，内置了消息组件解析器。
 *
 * @author Taskeren
 * @GitHub https://github.com/nitu2003/PicqBotX
 */
@ToString
public class Component {
	private static final String REGEX_CQ_CODE = "(\\[CQ:.*?,.*?])";
	private static final Pattern CQC = Pattern.compile(REGEX_CQ_CODE);

	/**
	 * 返回解析后的组件，任何错误的数据将使用默认值，基础数据为{@code -1}，其他为{@code null}。
	 *
	 * @param message 消息原文
	 * @return 解析后的组件
	 */
	public static Component parseComponent(String message) {
		String s0 = message.substring(1, message.length() - 1); // 去掉头尾的"[]"
		String[] s1 = s0.split(","); // 处理成数据对，像这样：["CQ:at", "qq=123456"]
		HashMap<String, String> map = genDataMap(s1); // 处理成Map
		ParseableHashMap<String, String> data = new ParseableHashMap<>(map);
		String type = data.get("CQ"); // 获取CQ码类型
		try {
			switch (type) {

			case "face":
				return new ComponentFace(data.getInteger("id", -1));

			case "bface":
				return new ComponentBFace(data.getInteger("p", -1), data.get("id"));

			case "image":
				return new ComponentImage(data.get("file"), data.get("url"));

			case "record":
				return new ComponentRecord(data.get("file"), data.getBoolean("magic", false));

			case "at":
				return new ComponentAt(data.getLong("qq", -1));

			case "rps":
				return new ComponentRPS(RSP.of(data.getInteger("type", -1)));

			case "dice":
				return new ComponentDice(data.getInteger("type", -1));

			case "shake":
				return new ComponentShake();

			case "sign":
				return new ComponentSign(data.get("location"), data.get("title"), data.get("image"));

			case "rich":
				return new ComponentRich(data.get("title"), data.get("text"), data.get("content"));

			case "location":
				return new ComponentLocation(data.get("lat"), data.get("lon"), data.get("title"), data.get("content"),
						data.getInteger("style", -1));

			case "contact":
				return new ComponentContact(data.getLong("id", -1), Type.of(data.get("type")));

			default:
				return new ComponentString(message);
			}
		} catch (Exception e) {
			return new ComponentString(message);
		}
	}

	/**
	 * 返回解析后的组件列表。解析方法：{@link #parseComponent(String)}。
	 *
	 * @param s 消息
	 * @return 解析后组件列表
	 */
	public static ArrayList<Component> parseComponents(String s) {
		ArrayList<Component> components = new ArrayList<>();
		int count = 0;

		Matcher matcher = CQC.matcher(s);
		int end = 0;
		while (matcher.find()) {
			// string before cq code
			if (matcher.start() > end) {
				String strBefore = s.substring(end, matcher.start());
				components.add(new ComponentString(strBefore));
			}
			String content = matcher.group();
			Component component = parseComponent(content);
			components.add(component);
			end = matcher.end();
			count++;
		}
		// string after last cq code
		if (end < s.length()) {
			String strAfter = s.substring(end);
			components.add(new ComponentString(strAfter));
			count++;
		}
		if (count == 0) {
			components.add(new ComponentString(s));
		}
		return components;
	}

	/**
	 * 返回解析后的组件列表。解析方法：{@link #parseComponent(String)}。
	 *
	 * @param args 消息列表
	 * @return 解析后组件列表
	 */
	public static ArrayList<Component> parseComponents(ArrayList<String> args) {
		ArrayList<Component> components = new ArrayList<>();

		for (String arg : args) {
			components.addAll(parseComponents(arg));
		}

		return components;
	}

	private static HashMap<String, String> genDataMap(String[] array) {
		HashMap<String, String> map = new HashMap<>();

		for (String s : array) {
			String[] s0 = s.split("[:=]", 2);
			if (s0.length < 2) continue;
			map.put(s0[0], s0[1]);
		}

		return map;
	}
}
