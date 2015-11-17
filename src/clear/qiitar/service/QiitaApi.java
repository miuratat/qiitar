package clear.qiitar.service;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * QiitaApi.
 * @author miuratat
 *
 */
public interface QiitaApi {

	@GET("/items")
	Observable<List<Item>> findItems(@Query("page") int page, @Query("per_page") int perPage);

	@GET("/tags/{id}/items")
	Observable<List<Item>> findItemsByTagId(@Path("id") String id, @Query("page") int page, @Query("per_page") int perPage);
}
