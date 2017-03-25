package vn.sunnet.hungdh.socialmediaplatform.status;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by HUNGDH on 8/31/2015.
 */
public class MyTwitterApiClient extends TwitterApiClient {
    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    public CustomService getCustomService() {
        return getService(CustomService.class);
    }
}
