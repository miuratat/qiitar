package clear.qiitar;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import clear.qiitar.service.QiitaService;

/**
 * 設定画面アクティビティ.
 * 
 * @author miuratat
 *
 */
@SuppressWarnings("deprecation")
public class SettingActivity extends PreferenceActivity implements OnPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setPreferenceScreen(createPreferenceHierarchy());
	}

	/**
	 * 設定画面構築.
	 * 
	 * @return
	 */
	private PreferenceScreen createPreferenceHierarchy() {

		// スクリーン生成
		PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(this);

		// レイアウトカテゴリー生成
		PreferenceCategory cate1 = new PreferenceCategory(this);
		cate1.setTitle(getString(R.string.setting_layout));
		screen.addPreference(cate1);

		// リスト右表示チェックボックス
		CheckBoxPreference cb = new CheckBoxPreference(this);
		cb.setKey("right");
		cb.setTitle(getString(R.string.setting_right));
		cb.setOnPreferenceChangeListener(this);
		cate1.addPreference(cb);

		// リスト文字サイズリスト
		ListPreference listfont = new ListPreference(this);
		listfont.setTitle(getString(R.string.setting_list_font));
		listfont.setKey("listfont");
		listfont.setEntries(new String[] { getString(R.string.setting_lagest), getString(R.string.setting_lager),
				getString(R.string.setting_normal), getString(R.string.setting_smaller),
				getString(R.string.setting_smallest) });
		listfont.setEntryValues(new String[] { "largest", "larger", "normal", "smaller", "smallest" });
		listfont.setOnPreferenceChangeListener(this);
		cate1.addPreference(listfont);

		// Web文字サイズリスト
		ListPreference webfont = new ListPreference(this);
		webfont.setTitle(getString(R.string.setting_web_font));
		webfont.setKey("webfont");
		webfont.setEntries(new String[] { getString(R.string.setting_lagest), getString(R.string.setting_lager),
				getString(R.string.setting_normal), getString(R.string.setting_smaller),
				getString(R.string.setting_smallest) });
		webfont.setEntryValues(new String[] { "largest", "larger", "normal", "smaller", "smallest" });
		webfont.setOnPreferenceChangeListener(this);
		cate1.addPreference(webfont);

		// 通信カテゴリ生成
		PreferenceCategory cate2 = new PreferenceCategory(this);
		cate2.setTitle(getString(R.string.setting_network));
		screen.addPreference(cate2);

		// 記事数リスト
		ListPreference access = new ListPreference(this);
		access.setTitle(getString(R.string.setting_access));
		access.setKey("perpage");
		access.setEntries(new String[] { "20", "50", "100" });
		access.setEntryValues(new String[] { "20", "50", "100" });
		access.setOnPreferenceChangeListener(this);
		cate2.addPreference(access);

		// フォローカテゴリ生成
		PreferenceCategory cate3 = new PreferenceCategory(this);
		cate3.setTitle(getString(R.string.setting_follow));
		screen.addPreference(cate3);

		// すべてのタグ分ループ
		for (String t : QiitaService.getTags()) {

			// タグチェックボックス
			CheckBoxPreference tag = new CheckBoxPreference(this);
			tag.setKey(t);
			tag.setTitle(t);
			tag.setOnPreferenceChangeListener(this);
			cate3.addPreference(tag);
		}

		return screen;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		setResult(RESULT_OK);
		return true;
	}

}