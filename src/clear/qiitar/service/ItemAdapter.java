package clear.qiitar.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.TextSize;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import clear.qiitar.R;

/**
 * Itemアダプター.
 * 
 * @author miuratat
 *
 */
@SuppressLint("InflateParams")
public class ItemAdapter extends ArrayAdapter<Item> {

	// レイアウトイフレーター.
	private LayoutInflater layoutInflater_;
	// Itemマップ
	public static Map<String, Item> maps_ = new HashMap<String, Item>();
	// タグ
	private String tag_;
	// 現在ページ数
	private int page_ = 1;

	/**
	 * コンストラクタ.
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 * @param tag
	 */
	public ItemAdapter(Context context, int textViewResourceId, List<Item> objects, String tag) {
		super(context, textViewResourceId, objects);
		tag_ = tag;
		layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void add(Item item) {
		super.add(item);
		maps_.put(item.id, item);
	};

	/**
	 * IDからItemを取得する.
	 * @param id
	 * @return
	 */
	public Item getById(String id) {
		return maps_.get(id);
	}

	/**
	 * すべてをクリアする.
	 */
	public void clearAll() {
		super.clear();
		maps_.clear();
		page_ = 1;
	}

	/**
	 * タグを取得する.
	 * @return
	 */
	public String getTag() {
		return tag_;
	}

	/**
	 * 現在ページ数を取得する.
	 * @return
	 */
	public int getPage() {
		return page_;
	}

	/**
	 * 現在ページ数を進める.
	 */
	public void nextPage() {
		page_++;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Item item = (Item) getItem(position);

		if (null == convertView) {
			convertView = layoutInflater_.inflate(R.layout.item_list, null);
		}

		TextView textView = (TextView) convertView.findViewById(R.id.text);

		textView.setTextSize(QiitaService.getListFont());

		if (tag_.equals(QiitaService.TAG_ALL)) {
			textView.setText(item.toTitle());
		} else {
			textView.setText(item.title);
		}

		return convertView;
	}

}