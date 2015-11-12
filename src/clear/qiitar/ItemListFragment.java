package clear.qiitar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import clear.qiitar.service.Item;
import clear.qiitar.service.QiitaService;

/**
 * Item���X�g�t���O�����g.
 * 
 * @author miuratat
 *
 */
public class ItemListFragment extends ListFragment {

	// �^�O.
	private String tag_;

	// Item�I���R�[���o�b�N.
	private Callbacks callbacks_;

	// Item�I���R�[���o�b�N�C���^�[�t�F�C�X
	public interface Callbacks {
		/**
		 * Item���I�����ꂽ�ꍇ.
		 * @param item
		 */
		public void onItemSelected(Item item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		tag_ = getArguments().getString("tag");

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// �v���O���X�o�[����
		View footer = View.inflate(getActivity(), R.layout.progress, null);
		// �v���O���X�o�[�����X�g�t�b�^�[�ɃZ�b�g
		getListView().addFooterView(footer);
		
		// ���X�g�̋�؂������
		getListView().setDivider(getResources().getDrawable(R.drawable.divider));
		// ��؂���̕����Z�b�g
		getListView().setDividerHeight(1);

		// �A�_�v�^�[���Z�b�g
		setListAdapter(QiitaService.getAdapter(tag_));
		
		// Scroll�C�x���g�ݒ�
		ListView listView = getListView();
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			private boolean more_;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (this.more_ && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					getMore();
					this.more_ = false;
				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
				if (totalItemCount != 0 && totalItemCount == firstVisibleItem + visibleItemCount) {
					this.more_ = true;
				}
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException("Activity must implement fragment's callbacks.");
		}

		callbacks_ = (Callbacks) activity;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		super.onListItemClick(listView, view, position, id);

		callbacks_.onItemSelected(QiitaService.getAdapter(tag_).getItem(position));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_reload:
			// �����[�h���s
			QiitaService.reload(QiitaService.getAdapter(tag_));
			return true;
		}
		return false;
	}

	/**
	 * ���̃y�[�W���擾����.
	 */
	private void getMore() {
		QiitaService.request(QiitaService.getAdapter(tag_));
	}

}
