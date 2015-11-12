package clear.qiitar.service;

import java.io.Serializable;
import java.util.List;

/**
 * Itemクラス.
 * @author miuratat
 *
 */
public class Item implements Serializable {

	private static final long serialVersionUID = -8328283392297619488L;
	
	// ID
	public String id;
	// タイトル
	public String title;
	// コンテンツ
	public String renderedBody;
	// タグ
	public List<Tag> tags;

	/**
	 * リスト表示用タイトルを取得する.
	 * @return
	 */
	public String toTitle() {

		StringBuilder sb = new StringBuilder();
		if (tags != null && tags.size() > 0) {
			sb.append("[" + tags.get(0).name + "]");
		}
		sb.append(title);

		return sb.toString();
	}

	/**
	 * ヘッダー表示用タグを取得する.
	 * @return
	 */
	public String toTags() {

		StringBuilder sb = new StringBuilder();
		for (Tag t : tags) {
			sb.append("[" + t.name + "]");
		}

		return sb.toString();
	}
}
