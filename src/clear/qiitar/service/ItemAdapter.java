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
 * Item�A�_�v�^�[.
 * 
 * @author miuratat
 *
 */
@SuppressLint("InflateParams")
public class ItemAdapter extends ArrayAdapter<Item> {

	// ���C�A�E�g�C�t���[�^�[.
	private LayoutInflater layoutInflater_;
	// Item�}�b�v
	public static Map<String, Item> maps_ = new HashMap<String, Item>();
	// �^�O
	private String tag_;
	// ���݃y�[�W��
	private int page_ = 1;

	/**
	 * �R���X�g���N�^.
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
	 * ID����Item���擾����.
	 * @param id
	 * @return
	 */
	public Item getById(String id) {
		return maps_.get(id);
	}

	/**
	 * ���ׂĂ��N���A����.
	 */
	public void clearAll() {
		super.clear();
		maps_.clear();
		page_ = 1;
	}

	/**
	 * �^�O���擾����.
	 * @return
	 */
	public String getTag() {
		return tag_;
	}

	/**
	 * ���݃y�[�W�����擾����.
	 * @return
	 */
	public int getPage() {
		return page_;
	}

	/**
	 * ���݃y�[�W����i�߂�.
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