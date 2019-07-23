package cn.glycol.extrabot.component;

import cn.glycol.extrabot.component.rich.ComponentRichMusic;
import cn.glycol.extrabot.component.rich.ComponentRichNews;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class ComponentRichParser {
	
	public static enum RichTextType {
		UNKNOWN, MUSIC, NEWS;
		
		public static RichTextType of(ComponentRich rich) {
			JSONObject json = JSONUtil.parseObj(rich.getContent());
			if(json.containsKey("music")) return MUSIC;
			if(json.containsKey("news"))  return NEWS;
			
			return UNKNOWN;
		}
	}
	
	public static RichTextType getType(ComponentRich rich) {
		return RichTextType.of(rich);
	}
	
	public static ComponentRichMusic toMusic(ComponentRich rich) {
		JSONObject json = JSONUtil.parseObj(rich.getContent());
		
		if(json.containsKey("music")) {
			JSONObject music = json.getJSONObject("music");
			String title    = music.getStr("title");
			String desc   = music.getStr("desc");
			String preview  = music.getStr("preview");
			String tag      = music.getStr("tag");
			String urlMusic = music.getStr("musicUrl");
			String urlPage  = music.getStr("jumpUrl");
			return new ComponentRichMusic(title, desc, preview, tag, urlMusic, urlPage);
		} else {
			return null;
		}
	}
	
	public static ComponentRichNews toNews(ComponentRich rich) {
		JSONObject json = JSONUtil.parseObj(rich.getContent());
		
		if(json.containsKey("news")) {
			JSONObject news = json.getJSONObject("news");
			String title  = news.getStr("title");
			String desc   = news.getStr("desc");
			String preview = news.getStr("preview");
			String tag     = news.getStr("tag");
			String url     = news.getStr("jumpUrl");
			return new ComponentRichNews(title, desc, preview, tag, url);
		} else {
			return null;
		}
	}
	
}
