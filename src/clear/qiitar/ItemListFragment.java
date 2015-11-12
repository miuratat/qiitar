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
 * Itemリストフラグメント.
 * 
 * @author miuratat
 *
 */
public class ItemListFragment extends ListFragment {

	// タグ.
	private String tag_;

	// Item選択コールバック.
	private Callbacks callbacks_;

	// Item選択コールバックインターフェイス
	public interface Callbacks {
		/**
		 * Itemが選択された場合.
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

		// プログレスバー生成
		View footer = View.inflate(getActivity(), R.layout.progress, null);
		// プログレスバーをリストフッターにセット
		getListView().addFooterView(footer);
		
		// リストの区切り線生成
		getListView().setDivider(getResources().getDrawable(R.drawable.divider));
		// 区切り線の幅をセット
		getListView().setDividerHeight(1);

		// アダプターをセット
		setListAdapter(QiitaService.getAdapter(tag_));
		
		// Scrollイベント設定
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
			// リロード実行
			QiitaService.reload(QiitaService.getAdapter(tag_));
			return true;
		}
		return false;
	}

	/**
	 * 次のページを取得する.
	 */
	private void getMore() {
		QiitaService.request(QiitaService.getAdapter(tag_));
	}

}
