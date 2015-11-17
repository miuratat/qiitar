package clear.qiitar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.webkit.WebSettings.TextSize;
import clear.qiitar.R;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * QIITAサービス.
 * 
 * @author miuratat
 *
 */
@SuppressWarnings("deprecation")
public class QiitaService {

	// 新着タグ
	public static String TAG_ALL = "all";
	// ユーザがフォローしているフィードリスト
	private static List<String> feedList = new ArrayList<String>();
	// すべてのタグリスト
	private static List<String> tagList = new ArrayList<String>();
	// アダプターマップ
	private static Map<String, ItemAdapter> adapterMap = new HashMap<String, ItemAdapter>();
	// API
	private static QiitaApi api;
	// ページ毎の記事数
	private static int perpage_;
	// リスト文字サイズ
	private static String listfont_;
	// Web文字サイズ
	private static String webfont_;
	// 新着ラベル
	private static String allLabel_;
	// アクセスオーバー文言
	private static String access_over_;
	/**
	 * セットアップ.
	 * 
	 * @param context
	 * @param tags
	 * @param sharedPreferences
	 */
	public static void setup(Context context, String tags, SharedPreferences sharedPreferences) {
		access_over_ = context.getString(R.string.access_over);
		allLabel_ = context.getString(R.string.all_label);
		perpage_ = Integer.parseInt(sharedPreferences.getString("perpage", "50"));
		listfont_ = sharedPreferences.getString("listfont", "normal");
		webfont_ = sharedPreferences.getString("webfont", "normal");

		tagList.clear();
		feedList.clear();

		feedList.add(TAG_ALL);
		adapterMap.put(TAG_ALL, new ItemAdapter(context, 0, new ArrayList<Item>(), TAG_ALL));

		String[] tagArray = tags.split(",");
		for (String tag : tagArray) {
			tagList.add(tag);
			if (sharedPreferences.getBoolean(tag, false)) {
				feedList.add(tag);
				adapterMap.put(tag, new ItemAdapter(context, 0, new ArrayList<Item>(), tag));
			}
		}

		api = new RestAdapter.Builder().setEndpoint("https://qiita.com/api/v2")
				.setConverter(new GsonConverter(
						new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()))
				.setRequestInterceptor(new RequestInterceptor() {
					@Override
					public void intercept(RequestFacade request) {
						//request.addHeader("Authorization", "Bearer " + "ba5ebc8c140b29c8bb9dbbd468b40009feaf2a0d");
					}
				}).build().create(QiitaApi.class);

		for (String key : adapterMap.keySet()) {
			request(adapterMap.get(key));
		}
	}

	/**
	 * リスト文字サイズを取得する.
	 * 
	 * @return
	 */
	public static int getListFont() {
		if ("largest".equals(listfont_)) {
			return 20;
		} else if ("larger".equals(listfont_)) {
			return 18;
		} else if ("normal".equals(listfont_)) {
			return 12;
		} else if ("smaller".equals(listfont_)) {
			return 10;
		} else if ("smallest".equals(listfont_)) {
			return 8;
		}
		return android.R.attr.textAppearanceMedium;
	}

	/**
	 * Web文字サイズを取得する.
	 * 
	 * @return
	 */
	
	public static TextSize getWebFont() {

		if ("largest".equals(webfont_)) {
			return TextSize.LARGEST;
		} else if ("larger".equals(webfont_)) {
			return TextSize.LARGER;
		} else if ("normal".equals(webfont_)) {
			return TextSize.NORMAL;
		} else if ("smaller".equals(webfont_)) {
			return TextSize.SMALLER;
		} else if ("smallest".equals(webfont_)) {
			return TextSize.SMALLEST;
		}
		return TextSize.NORMAL;
	}

	/**
	 * フィードを取得する.
	 * 
	 * @param position
	 * @return
	 */
	public static String getFeed(int position) {
		return feedList.get(position);
	}

	/**
	 * フィード表示ラベルを取得する.
	 * 
	 * @param position
	 * @return
	 */
	public static String getFeedLabel(int position) {

		if (position == 0) {
			return allLabel_;
		} else {
			return feedList.get(position);
		}
	}

	/**
	 * すべてのタグを取得する.
	 * 
	 * @return
	 */
	public static List<String> getTags() {
		return tagList;
	}

	/**
	 * フォローしているフィード件数を取得する.
	 * 
	 * @return
	 */
	public static int getFeedSize() {
		return feedList.size();
	}

	/**
	 * 更新.
	 * 
	 * @param adapter
	 */
	public static void reload(final ItemAdapter adapter) {
		adapter.clearAll();
		request(adapter);
	}

	/**
	 * アダプターを取得する.
	 * 
	 * @param tag
	 * @return
	 */
	public static ItemAdapter getAdapter(String tag) {
		return adapterMap.get(tag);
	}

	/**
	 * リクエストを実行する.
	 * 
	 * @param adapter
	 */
	public static void request(final ItemAdapter adapter) {

		Observable<List<Item>> list = null;
		if (adapter.getTag() == TAG_ALL) {
			list = api.findItems(adapter.getPage(), perpage_);
		} else {
			list = api.findItemsByTagId(adapter.getTag(), adapter.getPage(), perpage_);
		}

		list.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Observer<List<Item>>() {
					@Override
					public void onCompleted() {
						adapter.nextPage();
					}

					@Override
					public void onError(Throwable e) {
						
						Item i = new Item();
						i.tags = new ArrayList<Tag>();
						i.title = access_over_;
						i.renderedBody = access_over_;
						adapter.add(i);
						adapter.notifyDataSetChanged();
					}

					@Override
					public void onNext(List<Item> items) {
						for (Item i : items) {
							adapter.add(i);
						}
						adapter.notifyDataSetChanged();
					}
				});
	}

}
