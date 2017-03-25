package vn.sunnet.hungdh.socialmediaplatform.status;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.models.Tweet;
import java.util.List;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by HUNGDH on 8/31/2015.
 */
public interface CustomService {

    @GET("/1.1/trends/place.json")
    void trends(@Query("id") Integer var1, @Query("exclude") Boolean var2, Callback<List> var3);

    @FormUrlEncoded
    @POST("/1.1/statuses/update.json")
    void update(@Field("status") String var1, @Field("in_reply_to_status_id") Long var2, @Field("possibly_sensitive") Boolean var3,
                @Field("lat") Double var4, @Field("long") Double var5, @Field("place_id") String var6,
                @Field("display_cooridnates") Boolean var7, @Field("trim_user") Boolean var8, Callback<Tweet> var9);

}
