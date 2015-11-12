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
 * Web��ʃt���O�����g.
 * 
 * @author miuratat
 *
 */
@SuppressWarnings("deprecation")
public class ItemDetailFragment extends Fragment {

	// ItemID�擾����
	public static final String ARG_ITEM_ID = "item_id";

	// �I�����ꂽItem
	private Item item_;

	// WebView
	private WebView webView_;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// �I�����ꂽItem���擾
			item_ = (Item) getArguments().getSerializable(ARG_ITEM_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

		// WebView���擾
		webView_ = (WebView) rootView.findViewById(R.id.web_view);
		// Web�����T�C�Y���Z�b�g
		webView_.getSettings().setTextSize(QiitaService.getWebFont());

		if (item_ != null) {
			webView_.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// �����N�N���b�N�ŕʃE�B���h�E���J���Ȃ�
					return false;
				}
			});

			// �R���e���c���Z�b�g
			webView_.loadDataWithBaseURL(null, item_.renderedBody, "text/html", "UTF-8", null);

			// ActionBar���擾
			ActionBar bar = ((ActionBarActivity) getActivity()).getSupportActionBar();
			// �^�C�g�����Z�b�g
			bar.setTitle(item_.title);
			// �^�O���Z�b�g
			bar.setSubtitle(item_.toTags());
		}

		return rootView;
	}

	/**
	 * �߂�{�^���������邩.
	 * @return
	 */
	public boolean canGoBack() {
		return webView_ != null && webView_.canGoBack();
	}

	/**
	 * �߂�.
	 */
	public void goBack() {
		webView_.goBack();
		
		// �ŏ��̃y�[�W�̏ꍇ
		if (!webView_.canGoBack()) {
			// �R���e���c���ăZ�b�g
			webView_.loadDataWithBaseURL(null, item_.renderedBody, "text/html", "UTF-8", null);
		}
	}
}
