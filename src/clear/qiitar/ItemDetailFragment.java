package clear.qiitar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import clear.qiitar.service.Item;
import clear.qiitar.service.QiitaService;

/**
 * Web画面フラグメント.
 * 
 * @author miuratat
 *
 */
@SuppressWarnings("deprecation")
public class ItemDetailFragment extends Fragment {

	// ItemID取得引数
	public static final String ARG_ITEM_ID = "item_id";

	// 選択されたItem
	private Item item_;

	// WebView
	private WebView webView_;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// 選択されたItemを取得
			item_ = (Item) getArguments().getSerializable(ARG_ITEM_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

		// WebViewを取得
		webView_ = (WebView) rootView.findViewById(R.id.web_view);
		// Web文字サイズをセット
		webView_.getSettings().setTextSize(QiitaService.getWebFont());

		if (item_ != null) {
			webView_.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// リンククリックで別ウィンドウを開かない
					return false;
				}
			});

			// コンテンツをセット
			webView_.loadDataWithBaseURL(null, item_.renderedBody, "text/html", "UTF-8", null);

			// ActionBarを取得
			ActionBar bar = ((ActionBarActivity) getActivity()).getSupportActionBar();
			// タイトルをセット
			bar.setTitle(item_.title);
			// タグをセット
			bar.setSubtitle(item_.toTags());
		}

		return rootView;
	}

	/**
	 * 戻るボタンが押せるか.
	 * @return
	 */
	public boolean canGoBack() {
		return webView_ != null && webView_.canGoBack();
	}

	/**
	 * 戻る.
	 */
	public void goBack() {
		webView_.goBack();
		
		// 最初のページの場合
		if (!webView_.canGoBack()) {
			// コンテンツを再セット
			webView_.loadDataWithBaseURL(null, item_.renderedBody, "text/html", "UTF-8", null);
		}
	}
}
