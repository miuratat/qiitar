package clear.qiitar.service;

import java.io.Serializable;
import java.util.List;

/**
 * Item�N���X.
 * @author miuratat
 *
 */
public class Item implements Serializable {

	private static final long serialVersionUID = -8328283392297619488L;
	
	// ID
	public String id;
	// �^�C�g��
	public String title;
	// �R���e���c
	public String renderedBody;
	// �^�O
	public List<Tag> tags;

	/**
	 * ���X�g�\���p�^�C�g�����擾����.
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
	 * �w�b�_�[�\���p�^�O���擾����.
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
